package cn.daxpay.open.channel.fuyou.service.callback;

import cn.daxpay.open.channel.fuyou.client.FuyouH5ChannelClient;
import cn.daxpay.open.channel.fuyou.client.credential.FuyouRsaSdkCredential;
import cn.daxpay.open.channel.fuyou.client.req.FuyouH5CallbackParseReq;
import cn.daxpay.open.channel.fuyou.client.resp.FuyouCallbackParseResp;
import cn.daxpay.open.channel.fuyou.entity.rsa.FuyouRsaKeyConfig;
import cn.daxpay.open.channel.fuyou.service.rsa.FuyouRsaKeyConfigService;
import cn.daxpay.open.payment.trade.runtime.bo.CallbackData;
import cn.daxpay.open.payment.trade.runtime.bo.RefundCallbackData;
import cn.daxpay.open.payment.trade.order.dao.PayTradeManager;
import cn.daxpay.open.payment.trade.order.entity.PayTrade;
import cn.daxpay.open.payment.trade.record.service.PayCallbackRecordService;
import cn.daxpay.open.payment.trade.runtime.service.callback.PayCallbackService;
import cn.daxpay.open.payment.trade.runtime.service.callback.RefundCallbackService;
import cn.daxpay.open.platform.core.enums.pay.notice.CallbackStatusEnum;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/// # 富友 H5 聚合收银台回调处理服务
///
/// 富友异步通知(JSON `{mchnt_cd, message}` 报文体, 支付与退款同构) → 主应用接收原始报文 →
/// 转发子应用商户私钥解密与解析:
/// - 支付回调: 凭 order_id(下单时的平台交易号)直查 PayTrade, 构建 [CallbackData] 更新订单
/// - 退款回调: 退款为同步终态(受理成功即完成), 通知仅作兜底核对, 构建 [RefundCallbackData] 幂等更新
///
/// 主应用零加密代码, 解密/解析集中在子应用 dax-pay-channel-two。
@Slf4j
@Service
@RequiredArgsConstructor
public class FuyouH5CallbackService {

    /// 富友回调成功响应
    public static final String RESP_SUCCESS = "success";
    /// 富友回调失败响应
    public static final String RESP_FAIL = "fail";

    private final FuyouH5ChannelClient fuyouH5ChannelClient;
    private final FuyouRsaKeyConfigService fuyouRsaKeyConfigService;
    private final PayTradeManager payTradeManager;
    private final PayCallbackService payCallbackService;
    private final RefundCallbackService refundCallbackService;
    private final PayCallbackRecordService payCallbackRecordService;

    /// 支付回调处理
    public String payHandle(String channelMchNo, String body) {
        Map<String, Object> notify = new HashMap<>();
        notify.put("body", body);
        if (StrUtil.isBlank(body)) {
            log.error("富友H5支付回调: 报文体为空");
            payCallbackRecordService.savePay(channelMchNo, buildFailPayData(notify, "富友H5支付回调: 报文体为空"));
            return RESP_FAIL;
        }

        // 1. 获取通道商户 RSA 密钥 + 转发子应用解密解析
        FuyouCallbackParseResp resp = parse(channelMchNo, body, false);
        if (Objects.isNull(resp) || !Boolean.TRUE.equals(resp.getSuccess())) {
            log.error("富友H5支付回调解密解析失败");
            payCallbackRecordService.savePay(channelMchNo,
                    buildFailPayData(notify, "富友H5支付回调解密解析失败"));
            return RESP_FAIL;
        }

        // 2. 凭商户订单号(order_id = 下单时的平台交易号)直查 PayTrade
        String tradeNo = resp.getOutTradeNo();
        PayTrade trade = payTradeManager.findByTradeNo(tradeNo).orElse(null);
        if (Objects.isNull(trade)) {
            log.error("富友H5支付回调: 未找到对应订单 tradeNo={}", tradeNo);
            notify.put("tradeNo", tradeNo);
            payCallbackRecordService.savePay(channelMchNo,
                    buildFailPayData(notify, "富友H5支付回调: 未找到对应订单"));
            return RESP_FAIL;
        }

        // 3. 构建 CallbackData 交框架更新订单
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
            log.error("富友H5支付回调业务处理失败: tradeNo={}", trade.getTradeNo(), e);
            callbackData.setCallbackStatus(CallbackStatusEnum.EXCEPTION).setCallbackErrorMsg(e.getMessage());
            payCallbackRecordService.savePay(channelMchNo, callbackData);
            return RESP_FAIL;
        }
    }

    /// 退款回调处理(退款为同步终态, 通知仅作兜底核对, 幂等更新)
    public String refundHandle(String channelMchNo, String body) {
        Map<String, Object> notify = new HashMap<>();
        notify.put("body", body);
        if (StrUtil.isBlank(body)) {
            log.error("富友H5退款回调: 报文体为空");
            RefundCallbackData failData = new RefundCallbackData();
            failData.setCallbackData(notify);
            failData.setCallbackStatus(CallbackStatusEnum.FAIL);
            failData.setCallbackErrorMsg("富友H5退款回调: 报文体为空");
            payCallbackRecordService.saveRefund(channelMchNo, failData);
            return RESP_FAIL;
        }

        // 1. 获取通道商户 RSA 密钥 + 转发子应用解密解析
        FuyouCallbackParseResp resp = parse(channelMchNo, body, true);
        if (Objects.isNull(resp) || !Boolean.TRUE.equals(resp.getSuccess())) {
            log.error("富友H5退款回调解密解析失败");
            RefundCallbackData failData = new RefundCallbackData();
            failData.setCallbackData(notify);
            failData.setCallbackStatus(CallbackStatusEnum.FAIL);
            failData.setCallbackErrorMsg("富友H5退款回调解密解析失败");
            payCallbackRecordService.saveRefund(channelMchNo, failData);
            return RESP_FAIL;
        }

        // 2. 退款单反查(refund_order_id = 退款时上送的平台退款关联号)
        RefundCallbackData callbackData = new RefundCallbackData();
        callbackData.setRefundNo(resp.getOutRefundNo());
        callbackData.setOutRefundNo(resp.getOutRefundNo());
        if (Objects.equals(resp.getTradeStatus(), "SUCCESS")) {
            callbackData.setTradeStatus(CallbackStatusEnum.SUCCESS.getCode());
        } else {
            callbackData.setTradeErrorMsg("富友退款状态非成功: " + resp.getTradeStatus());
        }
        callbackData.setFinishTime(resp.getFinishTime());
        notify.put("outRefundNo", resp.getOutRefundNo());
        notify.put("tradeStatus", resp.getTradeStatus());
        callbackData.setCallbackData(notify);
        try {
            refundCallbackService.refundCallback(callbackData);
        } catch (Exception e) {
            log.error("富友H5退款回调业务处理失败: refundNo={}", callbackData.getRefundNo(), e);
            callbackData.setCallbackStatus(CallbackStatusEnum.EXCEPTION).setCallbackErrorMsg(e.getMessage());
            payCallbackRecordService.saveRefund(channelMchNo, callbackData);
            return RESP_FAIL;
        }
        payCallbackRecordService.saveRefund(channelMchNo, callbackData);
        return RESP_SUCCESS;
    }

    /// 转发子应用解密解析(支付/退款共用)
    private FuyouCallbackParseResp parse(String channelMchNo, String body, boolean refund) {
        // 获取通道商户 RSA 密钥(商户私钥解密用, 只读查询)
        FuyouRsaKeyConfig keyConfig;
        try {
            keyConfig = fuyouRsaKeyConfigService.getByChannelMchNoForPay(channelMchNo);
        } catch (Exception e) {
            log.error("富友H5回调: RSA 密钥未配置, 无法解密", e);
            return null;
        }
        FuyouRsaSdkCredential credential = new FuyouRsaSdkCredential();
        credential.setMerchantNo(keyConfig.getMchntCd());
        credential.setPrivateKey(keyConfig.getPrivateKey());
        credential.setPublicKey(keyConfig.getPublicKey());
        FuyouH5CallbackParseReq req = new FuyouH5CallbackParseReq();
        req.setCredential(credential);
        req.setReqParam(body);
        var result = refund
                ? fuyouH5ChannelClient.parseRefundCallback(req)
                : fuyouH5ChannelClient.parsePayCallback(req);
        if (result.getCode() != 0) {
            log.error("富友H5回调: 子应用解析失败: {}", result.getMsg());
            return null;
        }
        return result.getData();
    }

    /// 构建支付回调失败记录
    private CallbackData buildFailPayData(Map<String, Object> notify, String errorMsg) {
        CallbackData failData = new CallbackData();
        failData.setCallbackData(notify);
        failData.setCallbackStatus(CallbackStatusEnum.FAIL);
        failData.setCallbackErrorMsg(errorMsg);
        return failData;
    }
}
