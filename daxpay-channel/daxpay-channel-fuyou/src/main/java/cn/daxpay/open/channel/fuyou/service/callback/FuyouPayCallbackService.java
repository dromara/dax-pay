package cn.daxpay.open.channel.fuyou.service.callback;

import cn.daxpay.open.channel.fuyou.client.FuyouChannelClient;
import cn.daxpay.open.channel.fuyou.client.credential.FuyouSdkCredential;
import cn.daxpay.open.channel.fuyou.client.req.FuyouCallbackParseReq;
import cn.daxpay.open.channel.fuyou.client.resp.FuyouCallbackParseResp;
import cn.daxpay.open.channel.fuyou.dao.isv.FuyouIsvKeyConfigManager;
import cn.daxpay.open.channel.fuyou.entity.isv.FuyouIsvKeyConfig;
import cn.daxpay.open.payment.trade.runtime.bo.CallbackData;
import cn.daxpay.open.payment.trade.order.dao.PayTradeManager;
import cn.daxpay.open.payment.trade.order.entity.PayTrade;
import cn.daxpay.open.payment.trade.record.service.PayCallbackRecordService;
import cn.daxpay.open.payment.trade.runtime.service.callback.PayCallbackService;
import cn.daxpay.open.platform.core.enums.pay.channel.ChannelEnum;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import cn.daxpay.open.platform.core.enums.pay.notice.CallbackStatusEnum;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/// # 富友支付回调处理服务
///
/// 富友异步通知 → 主应用接收 `req` 参数(URL编码的XML, GBK) → 转发子应用验签与解析
/// (富友公钥 MD5withRSA + GBK) → 凭 mchnt_order_no(关联订单号) 反查 PayTrade →
/// 构建 [CallbackData] 交由 [PayCallbackService] 更新订单状态。
///
/// 富友回调用关联订单号(mchnt_order_no)而非平台交易号, 故主应用保留 DB 反查步骤。
/// 主应用零加密代码, 验签/解析集中在子应用 dax-pay-channel-two。
@Slf4j
@Service
@RequiredArgsConstructor
public class FuyouPayCallbackService {

    /// 富友回调成功响应
    public static final String RESP_SUCCESS = "1";
    /// 富友回调失败响应
    public static final String RESP_FAIL = "0";

    private final FuyouChannelClient fuyouChannelClient;
    private final FuyouIsvKeyConfigManager fuyouIsvKeyConfigManager;
    private final PayTradeManager payTradeManager;
    private final PayCallbackService payCallbackService;
    private final PayCallbackRecordService payCallbackRecordService;

    /// 支付回调处理
    public String payHandle(String channelMchNo, String reqParam) {
        Map<String, Object> notify = new HashMap<>();
        notify.put("req", reqParam);
        if (StrUtil.isBlank(reqParam)) {
            log.error("富友支付回调: req 参数为空");
            CallbackData failData = new CallbackData();
            failData.setCallbackData(notify);
            failData.setCallbackStatus(CallbackStatusEnum.FAIL);
            failData.setCallbackErrorMsg("富友支付回调: req 参数为空");
            payCallbackRecordService.savePay(ChannelEnum.FUYOU_PAY.getCode(), channelMchNo, failData);
            return RESP_FAIL;
        }

        // 1. 获取全局服务商公钥(只读查询)
        FuyouIsvKeyConfig keyConfig = fuyouIsvKeyConfigManager.findByProduct(ProductEnum.FUYOU_PAY.getCode())
                .orElse(null);
        if (keyConfig == null || StrUtil.isBlank(keyConfig.getPublicKey())) {
            log.error("富友支付回调: 服务商密钥未配置, 无法验签");
            CallbackData failData = new CallbackData();
            failData.setCallbackData(notify);
            failData.setCallbackStatus(CallbackStatusEnum.FAIL);
            failData.setCallbackErrorMsg("富友支付回调: 服务商密钥未配置");
            payCallbackRecordService.savePay(ChannelEnum.FUYOU_PAY.getCode(), channelMchNo, failData);
            return RESP_FAIL;
        }

        // 2. 转发子应用验签解析
        FuyouCallbackParseResp resp = parse(reqParam, keyConfig.getPublicKey(), false);
        if (resp == null || !Boolean.TRUE.equals(resp.getSuccess())) {
            log.error("富友支付回调验签失败");
            CallbackData failData = new CallbackData();
            failData.setCallbackData(notify);
            failData.setCallbackStatus(CallbackStatusEnum.FAIL);
            failData.setCallbackErrorMsg("富友支付回调验签失败");
            payCallbackRecordService.savePay(ChannelEnum.FUYOU_PAY.getCode(), channelMchNo, failData);
            return RESP_FAIL;
        }

        // 3. 凭关联订单号(mchnt_order_no)直接反查 PayTrade
        // relationOrderNo 是 PayTrade 上的字段(回调/关退同步反查权威), 跨 tradeType(normal/gateway)统一,
        // 避免硬编码查 NormalPayOrder 导致 tradeType=gateway 时跨表查不到容器(与支付宝回调同源 bug)
        String relationOrderNo = resp.getOutTradeNo();
        PayTrade trade = payTradeManager.findByRelationOrderNo(relationOrderNo).orElse(null);
        if (trade == null) {
            log.error("富友支付回调: 未找到关联订单 relationOrderNo={}", relationOrderNo);
            CallbackData failData = new CallbackData();
            notify.put("relationOrderNo", relationOrderNo);
            failData.setCallbackData(notify);
            failData.setCallbackStatus(CallbackStatusEnum.FAIL);
            failData.setCallbackErrorMsg("富友支付回调: 未找到关联订单");
            payCallbackRecordService.savePay(ChannelEnum.FUYOU_PAY.getCode(), channelMchNo, failData);
            return RESP_FAIL;
        }

        // 4. 构建 CallbackData 交框架更新订单
        CallbackData callbackData = new CallbackData();
        callbackData.setTradeNo(trade.getTradeNo());
        callbackData.setOutTradeNo(resp.getTradeNo());
        // 富友支付回调通知仅出现在支付成功时
        if (Objects.equals(resp.getTradeStatus(), "SUCCESS")) {
            callbackData.setTradeStatus(CallbackStatusEnum.SUCCESS.getCode());
        } else {
            callbackData.setCallbackErrorMsg("富友回调状态非成功: " + resp.getTradeStatus());
        }
        callbackData.setFinishTime(resp.getFinishTime());
        notify.put("tradeNo", resp.getTradeNo());
        notify.put("tradeStatus", resp.getTradeStatus());
        callbackData.setCallbackData(notify);
        try {
            payCallbackService.payCallback(callbackData);
            payCallbackRecordService.savePay(ChannelEnum.FUYOU_PAY.getCode(), channelMchNo, callbackData);
            return RESP_SUCCESS;
        } catch (Exception e) {
            log.error("富友支付回调业务处理失败: tradeNo={}", trade.getTradeNo(), e);
            callbackData.setCallbackStatus(CallbackStatusEnum.EXCEPTION).setCallbackErrorMsg(e.getMessage());
            payCallbackRecordService.savePay(ChannelEnum.FUYOU_PAY.getCode(), channelMchNo, callbackData);
            return RESP_FAIL;
        }
    }

    /// 转发子应用验签解析
    FuyouCallbackParseResp parse(String reqParam, String publicKey, boolean refund) {
        FuyouSdkCredential credential = new FuyouSdkCredential();
        credential.setPublicKey(publicKey);
        FuyouCallbackParseReq req = new FuyouCallbackParseReq();
        req.setCredential(credential);
        req.setReqParam(reqParam);
        var result = refund
                ? fuyouChannelClient.parseRefundCallback(req)
                : fuyouChannelClient.parsePayCallback(req);
        if (result.getCode() != 0) {
            log.error("富友回调: 子应用解析失败: {}", result.getMsg());
            return null;
        }
        return result.getData();
    }
}
