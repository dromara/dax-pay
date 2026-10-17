package cn.daxpay.open.channel.ysep.service.callback;

import cn.daxpay.open.channel.ysep.client.YsepChannelClient;
import cn.daxpay.open.channel.ysep.client.credential.YsepSdkCredential;
import cn.daxpay.open.channel.ysep.client.req.YsepCallbackParseReq;
import cn.daxpay.open.channel.ysep.client.resp.YsepCallbackParseResp;
import cn.daxpay.open.channel.ysep.dao.YsepKeyConfigManager;
import cn.daxpay.open.channel.ysep.entity.YsepKeyConfig;
import cn.daxpay.open.payment.trade.runtime.bo.CallbackData;
import cn.daxpay.open.payment.trade.record.service.PayCallbackRecordService;
import cn.daxpay.open.payment.trade.runtime.service.callback.PayCallbackService;
import cn.daxpay.open.platform.core.enums.pay.notice.CallbackStatusEnum;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/// # 银盛支付回调处理服务(两产品共用)
///
/// 银盛异步通知(form-urlencoded POST) → 主应用提取表单参数 → 携凭证转发子应用验签与解析
/// (SHA1withRSA + 银盛平台公钥证书) → 构建 [CallbackData] 交由 [PayCallbackService] 更新订单状态。
///
/// 两产品回调共用本端点与凭证体系: 通道商户维度密钥(ysep_key_config)一套证书两族验签通用,
/// 子应用按报文形态(是否含 bizResponseJson)自动分流开放平台/e企通验签协议。
/// 凭 out_trade_no / requestNo 反查 PayTrade。银盛两产品退款无独立异步通知。
///
/// 应答串: 成功 `success`, 失败 `fail`(银盛要求原文应答, 失败重发最多 10 次)。
@Slf4j
@Service
@RequiredArgsConstructor
public class YsepPayCallbackService {

    private final YsepChannelClient ysepChannelClient;
    private final YsepKeyConfigManager ysepKeyConfigManager;
    private final PayCallbackService payCallbackService;
    private final PayCallbackRecordService payCallbackRecordService;

    /// 支付成功应答串(银盛要求原文应答)
    private static final String NOTIFY_SUCCESS = "success";
    /// 支付失败应答串
    private static final String NOTIFY_FAIL = "fail";

    /// 支付回调处理
    public String payHandle(String channelMchNo, HttpServletRequest request) {
        // 1. 提取回调表单参数(银盛通知为 form-urlencoded POST)
        Map<String, String> formParams = new HashMap<>();
        request.getParameterMap().forEach((key, values) -> {
            if (values != null && values.length > 0) {
                formParams.put(key, values[0]);
            }
        });

        // 2. 组装验签凭证(通道商户维度密钥, 只读查询)
        YsepSdkCredential credential = resolveVerifyCredential(channelMchNo);
        if (Objects.isNull(credential) || Objects.isNull(credential.getYsepayPublicCert())) {
            log.error("银盛支付回调: 通道密钥未配置, 无法验签, channelMchNo={}", channelMchNo);
            return saveFail(channelMchNo, formParams, "银盛支付回调: 通道密钥未配置");
        }

        // 3. 转发子应用验签解析(按报文形态自动分流两族协议)
        YsepCallbackParseResp resp = parse(formParams, credential);
        if (Objects.isNull(resp) || !Boolean.TRUE.equals(resp.getSuccess())) {
            log.error("银盛支付回调验签失败");
            return saveFail(channelMchNo, formParams, "银盛支付回调验签失败");
        }

        // 4. 构建 CallbackData 交框架更新订单状态
        CallbackData callbackData = new CallbackData();
        callbackData.setTradeNo(resp.getOutTradeNo());
        callbackData.setOutTradeNo(resp.getTradeNo());
        if (Objects.equals(resp.getTradeStatus(), "TRADE_SUCCESS")
                || Objects.equals(resp.getTradeStatus(), "SUCCESS")) {
            callbackData.setTradeStatus(CallbackStatusEnum.SUCCESS.getCode());
        } else {
            callbackData.setCallbackErrorMsg("银盛回调状态非成功: " + resp.getTradeStatus());
        }
        callbackData.setFinishTime(resp.getFinishTime());
        callbackData.setBuyerId(resp.getBuyerId());
        Map<String, Object> notify = new HashMap<>(formParams);
        notify.put("outTradeNo", resp.getOutTradeNo());
        notify.put("tradeNo", resp.getTradeNo());
        notify.put("amount", resp.getAmount());
        notify.put("tradeStatus", resp.getTradeStatus());
        callbackData.setCallbackData(notify);
        try {
            payCallbackService.payCallback(callbackData);
        } catch (Exception e) {
            log.error("银盛支付回调业务处理失败: tradeNo={}", callbackData.getTradeNo(), e);
            callbackData.setCallbackStatus(CallbackStatusEnum.EXCEPTION).setCallbackErrorMsg(e.getMessage());
            payCallbackRecordService.savePay(channelMchNo, callbackData);
            return NOTIFY_FAIL;
        }
        payCallbackRecordService.savePay(channelMchNo, callbackData);
        return NOTIFY_SUCCESS;
    }

    /// 组装回调验签凭证(通道商户维度密钥, 验签用银盛平台公钥证书; 只读查询)
    private YsepSdkCredential resolveVerifyCredential(String channelMchNo) {
        YsepKeyConfig keyConfig = ysepKeyConfigManager.findByChannelMchNo(channelMchNo)
                .orElse(null);
        if (Objects.isNull(keyConfig)) {
            return null;
        }
        // 组装凭证(验签只需平台公钥证书, 其余字段一并下发保持凭证完整)
        YsepSdkCredential credential = new YsepSdkCredential();
        credential.setSponsorMchNo(keyConfig.getSponsorMchNo());
        credential.setPayeeMchNo(keyConfig.getPayeeMchNo());
        credential.setBusinessCode(keyConfig.getBusinessCode());
        credential.setMerchantCertPfx(keyConfig.getMerchantCertPfx());
        credential.setMerchantCertPassword(keyConfig.getMerchantCertPassword());
        credential.setYsepayPublicCert(keyConfig.getYsepayPublicCert());
        return credential;
    }

    /// 转发子应用验签解析
    private YsepCallbackParseResp parse(Map<String, String> formParams, YsepSdkCredential credential) {
        YsepCallbackParseReq req = new YsepCallbackParseReq();
        req.setCredential(credential);
        req.setFormParams(formParams);
        var result = ysepChannelClient.parsePayCallback(req);
        if (result.getCode() != 0) {
            log.error("银盛回调: 子应用解析失败: {}", result.getMsg());
            return null;
        }
        return result.getData();
    }

    /// 保存失败回调记录并返回失败应答
    private String saveFail(String channelMchNo, Map<String, String> formParams, String errorMsg) {
        CallbackData failData = new CallbackData();
        failData.setCallbackData(new HashMap<>(formParams));
        failData.setCallbackStatus(CallbackStatusEnum.FAIL);
        failData.setCallbackErrorMsg(errorMsg);
        payCallbackRecordService.savePay(channelMchNo, failData);
        return NOTIFY_FAIL;
    }
}
