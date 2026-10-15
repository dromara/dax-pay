package cn.daxpay.open.channel.fuyou.service.callback;

import cn.daxpay.open.channel.fuyou.client.FuyouDirectChannelClient;
import cn.daxpay.open.channel.fuyou.client.credential.FuyouDirectSdkCredential;
import cn.daxpay.open.channel.fuyou.client.req.FuyouDirectCallbackParseReq;
import cn.daxpay.open.channel.fuyou.client.resp.FuyouCallbackParseResp;
import cn.daxpay.open.channel.fuyou.dao.direct.FuyouDirectKeyConfigManager;
import cn.daxpay.open.channel.fuyou.entity.direct.FuyouDirectKeyConfig;
import cn.daxpay.open.payment.trade.runtime.bo.CallbackData;
import cn.daxpay.open.payment.trade.order.dao.PayTradeManager;
import cn.daxpay.open.payment.trade.order.entity.PayTrade;
import cn.daxpay.open.payment.trade.record.service.PayCallbackRecordService;
import cn.daxpay.open.payment.trade.runtime.service.callback.PayCallbackService;
import cn.daxpay.open.platform.core.enums.pay.notice.CallbackStatusEnum;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/// # 富友直连支付回调处理服务
///
/// 富友直连异步通知 → 主应用接收原始报文体(JSON, UTF-8) → 转发子应用验签与解析
/// (商户密钥 mchnt_key MD5) → 凭 mchnt_order_no(关联订单号) 反查 PayTrade →
/// 构建 [CallbackData] 交由 [PayCallbackService] 更新订单状态。
///
/// 验签密钥按通道商户取([FuyouDirectKeyConfig#mchntKey]), 与服务商模式的全局公钥不同。
/// 直连仅主扫/公众号支付有异步通知, 无退款结果通知(退款终态走退款查询轮询)。
@Slf4j
@Service
@RequiredArgsConstructor
public class FuyouDirectPayCallbackService {

    /// 富友回调成功响应
    public static final String RESP_SUCCESS = "1";
    /// 富友回调失败响应
    public static final String RESP_FAIL = "0";

    private final FuyouDirectChannelClient fuyouDirectChannelClient;
    private final FuyouDirectKeyConfigManager fuyouDirectKeyConfigManager;
    private final PayTradeManager payTradeManager;
    private final PayCallbackService payCallbackService;
    private final PayCallbackRecordService payCallbackRecordService;

    /// 支付回调处理
    public String payHandle(String channelMchNo, String reqParam) {
        Map<String, Object> notify = new HashMap<>();
        notify.put("req", reqParam);
        if (StrUtil.isBlank(reqParam)) {
            log.error("富友直连支付回调: 报文体为空");
            return failRecord(channelMchNo, notify, "富友直连支付回调: 报文体为空");
        }

        // 1. 取该通道商户的直连密钥(只读查询)
        FuyouDirectKeyConfig keyConfig = fuyouDirectKeyConfigManager.findByChannelMchNo(channelMchNo)
                .orElse(null);
        if (Objects.isNull(keyConfig) || StrUtil.isBlank(keyConfig.getMchntKey())) {
            log.error("富友直连支付回调: 直连商户密钥未配置, 无法验签");
            return failRecord(channelMchNo, notify, "富友直连支付回调: 直连商户密钥未配置");
        }

        // 2. 转发子应用验签解析
        FuyouCallbackParseResp resp = parse(reqParam, keyConfig.getMchntKey());
        if (Objects.isNull(resp) || !Boolean.TRUE.equals(resp.getSuccess())) {
            log.error("富友直连支付回调验签失败");
            return failRecord(channelMchNo, notify, "富友直连支付回调验签失败");
        }

        // 3. 凭关联订单号(mchnt_order_no)直接反查 PayTrade
        String relationOrderNo = resp.getOutTradeNo();
        PayTrade trade = payTradeManager.findByRelationOrderNo(relationOrderNo).orElse(null);
        if (Objects.isNull(trade)) {
            log.error("富友直连支付回调: 未找到关联订单 relationOrderNo={}", relationOrderNo);
            notify.put("relationOrderNo", relationOrderNo);
            return failRecord(channelMchNo, notify, "富友直连支付回调: 未找到关联订单");
        }

        // 4. 构建 CallbackData 交框架更新订单
        CallbackData callbackData = new CallbackData();
        callbackData.setTradeNo(trade.getTradeNo());
        callbackData.setOutTradeNo(resp.getTradeNo());
        // 富友直连支付回调通知仅出现在支付成功时
        if (Objects.equals(resp.getTradeStatus(), "SUCCESS")) {
            callbackData.setTradeStatus(CallbackStatusEnum.SUCCESS.getCode());
        } else {
            callbackData.setCallbackErrorMsg("富友直连回调状态非成功: " + resp.getTradeStatus());
        }
        callbackData.setFinishTime(resp.getFinishTime());
        notify.put("tradeNo", resp.getTradeNo());
        notify.put("tradeStatus", resp.getTradeStatus());
        callbackData.setCallbackData(notify);
        try {
            payCallbackService.payCallback(callbackData);
            payCallbackRecordService.savePay(channelMchNo, callbackData);
            return RESP_SUCCESS;
        } catch (Exception e) {
            log.error("富友直连支付回调业务处理失败: tradeNo={}", trade.getTradeNo(), e);
            callbackData.setCallbackStatus(CallbackStatusEnum.EXCEPTION).setCallbackErrorMsg(e.getMessage());
            payCallbackRecordService.savePay(channelMchNo, callbackData);
            return RESP_FAIL;
        }
    }

    /// 记录回调失败
    private String failRecord(String channelMchNo, Map<String, Object> notify, String errorMsg) {
        CallbackData failData = new CallbackData();
        failData.setCallbackData(notify);
        failData.setCallbackStatus(CallbackStatusEnum.FAIL);
        failData.setCallbackErrorMsg(errorMsg);
        payCallbackRecordService.savePay(channelMchNo, failData);
        return RESP_FAIL;
    }

    /// 转发子应用验签解析
    FuyouCallbackParseResp parse(String reqParam, String mchntKey) {
        FuyouDirectSdkCredential credential = new FuyouDirectSdkCredential();
        credential.setMchntKey(mchntKey);
        FuyouDirectCallbackParseReq req = new FuyouDirectCallbackParseReq();
        req.setCredential(credential);
        req.setReqParam(reqParam);
        var result = fuyouDirectChannelClient.parsePayCallback(req);
        if (result.getCode() != 0) {
            log.error("富友直连回调: 子应用解析失败: {}", result.getMsg());
            return null;
        }
        return result.getData();
    }
}
