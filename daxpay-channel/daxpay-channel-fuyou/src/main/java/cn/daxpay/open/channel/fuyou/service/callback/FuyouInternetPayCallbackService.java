package cn.daxpay.open.channel.fuyou.service.callback;

import cn.daxpay.open.channel.fuyou.client.FuyouInternetChannelClient;
import cn.daxpay.open.channel.fuyou.client.credential.FuyouRsaSdkCredential;
import cn.daxpay.open.channel.fuyou.client.req.FuyouInternetCallbackParseReq;
import cn.daxpay.open.channel.fuyou.client.resp.FuyouCallbackParseResp;
import cn.daxpay.open.channel.fuyou.entity.rsa.FuyouRsaKeyConfig;
import cn.daxpay.open.channel.fuyou.service.rsa.FuyouRsaKeyConfigService;
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

/// # 富友互联网直连支付回调处理服务
///
/// 富友异步通知(JSON `{mchnt_cd, message}` 报文体) → 主应用接收原始报文 → 转发子应用
/// 商户私钥解密与解析 → 凭 order_id(下单时的平台交易号)直查 PayTrade →
/// 构建 [CallbackData] 交由 [PayCallbackService] 更新订单状态。
///
/// 富友回调仅在成功时通知, 解密且 order_st=1 即视为 SUCCESS。
/// 主应用零加密代码, 解密/解析集中在子应用 dax-pay-channel-two。
@Slf4j
@Service
@RequiredArgsConstructor
public class FuyouInternetPayCallbackService {

    /// 富友回调成功响应
    public static final String RESP_SUCCESS = "success";
    /// 富友回调失败响应
    public static final String RESP_FAIL = "fail";

    private final FuyouInternetChannelClient fuyouInternetChannelClient;
    private final FuyouRsaKeyConfigService fuyouRsaKeyConfigService;
    private final PayTradeManager payTradeManager;
    private final PayCallbackService payCallbackService;
    private final PayCallbackRecordService payCallbackRecordService;

    /// 支付回调处理
    public String payHandle(String channelMchNo, String body) {
        Map<String, Object> notify = new HashMap<>();
        notify.put("body", body);
        if (StrUtil.isBlank(body)) {
            log.error("富友互联网支付回调: 报文体为空");
            CallbackData failData = new CallbackData();
            failData.setCallbackData(notify);
            failData.setCallbackStatus(CallbackStatusEnum.FAIL);
            failData.setCallbackErrorMsg("富友互联网支付回调: 报文体为空");
            payCallbackRecordService.savePay(channelMchNo, failData);
            return RESP_FAIL;
        }

        // 1. 获取通道商户 RSA 密钥(商户私钥解密用, 只读查询)
        FuyouRsaKeyConfig keyConfig;
        try {
            keyConfig = fuyouRsaKeyConfigService.getByChannelMchNoForPay(channelMchNo);
        } catch (Exception e) {
            log.error("富友互联网支付回调: RSA 密钥未配置, 无法解密", e);
            CallbackData failData = new CallbackData();
            failData.setCallbackData(notify);
            failData.setCallbackStatus(CallbackStatusEnum.FAIL);
            failData.setCallbackErrorMsg("富友互联网支付回调: RSA 密钥未配置");
            payCallbackRecordService.savePay(channelMchNo, failData);
            return RESP_FAIL;
        }

        // 2. 转发子应用解密解析
        FuyouCallbackParseResp resp = parse(body, keyConfig);
        if (Objects.isNull(resp) || !Boolean.TRUE.equals(resp.getSuccess())) {
            log.error("富友互联网支付回调解密解析失败");
            CallbackData failData = new CallbackData();
            failData.setCallbackData(notify);
            failData.setCallbackStatus(CallbackStatusEnum.FAIL);
            failData.setCallbackErrorMsg("富友互联网支付回调解密解析失败");
            payCallbackRecordService.savePay(channelMchNo, failData);
            return RESP_FAIL;
        }

        // 3. 凭商户订单号(order_id = 下单时的平台交易号)直查 PayTrade
        String tradeNo = resp.getOutTradeNo();
        PayTrade trade = payTradeManager.findByTradeNo(tradeNo).orElse(null);
        if (Objects.isNull(trade)) {
            log.error("富友互联网支付回调: 未找到对应订单 tradeNo={}", tradeNo);
            CallbackData failData = new CallbackData();
            notify.put("tradeNo", tradeNo);
            failData.setCallbackData(notify);
            failData.setCallbackStatus(CallbackStatusEnum.FAIL);
            failData.setCallbackErrorMsg("富友互联网支付回调: 未找到对应订单");
            payCallbackRecordService.savePay(channelMchNo, failData);
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
            payCallbackRecordService.savePay(channelMchNo, callbackData);
            return RESP_SUCCESS;
        } catch (Exception e) {
            log.error("富友互联网支付回调业务处理失败: tradeNo={}", trade.getTradeNo(), e);
            callbackData.setCallbackStatus(CallbackStatusEnum.EXCEPTION).setCallbackErrorMsg(e.getMessage());
            payCallbackRecordService.savePay(channelMchNo, callbackData);
            return RESP_FAIL;
        }
    }

    /// 转发子应用解密解析
    private FuyouCallbackParseResp parse(String body, FuyouRsaKeyConfig keyConfig) {
        FuyouRsaSdkCredential credential = new FuyouRsaSdkCredential();
        credential.setMerchantNo(keyConfig.getMchntCd());
        credential.setPrivateKey(keyConfig.getPrivateKey());
        credential.setPublicKey(keyConfig.getPublicKey());
        FuyouInternetCallbackParseReq req = new FuyouInternetCallbackParseReq();
        req.setCredential(credential);
        req.setReqParam(body);
        var result = fuyouInternetChannelClient.parsePayCallback(req);
        if (result.getCode() != 0) {
            log.error("富友互联网回调: 子应用解析失败: {}", result.getMsg());
            return null;
        }
        return result.getData();
    }
}
