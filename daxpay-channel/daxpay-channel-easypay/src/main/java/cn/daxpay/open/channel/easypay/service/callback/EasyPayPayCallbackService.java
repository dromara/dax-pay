package cn.daxpay.open.channel.easypay.service.callback;

import cn.daxpay.open.channel.easypay.client.EasyPayChannelClient;
import cn.daxpay.open.channel.easypay.client.credential.EasyPaySdkCredential;
import cn.daxpay.open.channel.easypay.client.req.EasyPayCallbackParseReq;
import cn.daxpay.open.channel.easypay.client.resp.EasyPayCallbackParseResp;
import cn.daxpay.open.channel.easypay.dao.EasyPayKeyConfigManager;
import cn.daxpay.open.channel.easypay.entity.EasyPayKeyConfig;
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

/// # 易支付支付回调处理服务
///
/// 易支付异步通知(form 参数形态) → 主应用收集请求参数 → 携凭证转发子应用验签与解析
/// (SHA256withRSA + 平台公钥) →
/// 构建 [CallbackData] 交由 [PayCallbackService] 更新订单状态。
///
/// 凭 out_trade_no 反查 PayTrade; 主应用零加密代码, 验签/解析集中在子应用 dax-pay-channel-two。
/// 易支付无退款异步通知(退款为同步接口), 本服务仅处理支付回调。
@Slf4j
@Service
@RequiredArgsConstructor
public class EasyPayPayCallbackService {

    private static final String NOTIFY_SUCCESS = "success";

    private static final String NOTIFY_FAIL = "fail";

    private final EasyPayChannelClient easyPayChannelClient;

    private final EasyPayKeyConfigManager easyPayKeyConfigManager;

    private final PayCallbackService payCallbackService;

    private final PayCallbackRecordService payCallbackRecordService;

    /// 支付回调处理
    public String payHandle(String channelMchNo, HttpServletRequest request) {
        // 1. 收集回调参数(易支付通知为 form 参数形态, 含 sign 字段)
        Map<String, String> params = collectParams(request);
        Map<String, Object> notify = new HashMap<>();
        notify.put("params", params);

        // 2. 解析验签凭证(通道商户维度密钥, 只读查询)
        EasyPayKeyConfig keyConfig = easyPayKeyConfigManager.findByChannelMchNo(channelMchNo)
                .orElse(null);
        if (Objects.isNull(keyConfig) || Objects.isNull(keyConfig.getPlatformPublicKey())) {
            log.error("易支付支付回调: 通道密钥未配置, 无法验签, channelMchNo={}", channelMchNo);
            CallbackData failData = new CallbackData();
            failData.setCallbackData(notify);
            failData.setCallbackStatus(CallbackStatusEnum.FAIL);
            failData.setCallbackErrorMsg("易支付支付回调: 通道密钥未配置");
            payCallbackRecordService.savePay(channelMchNo, failData);
            return NOTIFY_FAIL;
        }

        // 3. 转发子应用验签解析
        EasyPayCallbackParseResp resp = parse(params, keyConfig);
        if (Objects.isNull(resp) || !Boolean.TRUE.equals(resp.getSuccess())) {
            log.error("易支付支付回调验签失败: channelMchNo={}", channelMchNo);
            CallbackData failData = new CallbackData();
            failData.setCallbackData(notify);
            failData.setCallbackStatus(CallbackStatusEnum.FAIL);
            failData.setCallbackErrorMsg("易支付支付回调验签失败");
            payCallbackRecordService.savePay(channelMchNo, failData);
            return NOTIFY_FAIL;
        }

        // 4. 构建 CallbackData 交框架更新订单状态
        CallbackData callbackData = new CallbackData();
        callbackData.setTradeNo(resp.getOutTradeNo());
        callbackData.setOutTradeNo(resp.getTradeNo());
        if (Objects.equals(resp.getTradeStatus(), "SUCCESS")) {
            callbackData.setTradeStatus(CallbackStatusEnum.SUCCESS.getCode());
        } else {
            callbackData.setCallbackErrorMsg("易支付回调状态非成功: " + resp.getTradeStatus());
        }
        callbackData.setFinishTime(resp.getFinishTime());
        notify.put("outTradeNo", resp.getOutTradeNo());
        notify.put("tradeNo", resp.getTradeNo());
        notify.put("amount", resp.getAmount());
        notify.put("tradeStatus", resp.getTradeStatus());
        callbackData.setCallbackData(notify);
        try {
            payCallbackService.payCallback(callbackData);
        } catch (Exception e) {
            log.error("易支付支付回调业务处理失败: tradeNo={}", callbackData.getTradeNo(), e);
            callbackData.setCallbackStatus(CallbackStatusEnum.EXCEPTION).setCallbackErrorMsg(e.getMessage());
            payCallbackRecordService.savePay(channelMchNo, callbackData);
            return NOTIFY_FAIL;
        }
        payCallbackRecordService.savePay(channelMchNo, callbackData);
        return NOTIFY_SUCCESS;
    }

    /// 收集回调请求参数(form 参数/query 均兼容, 键值转为字符串形态)
    private Map<String, String> collectParams(HttpServletRequest request) {
        Map<String, String> params = new HashMap<>();
        request.getParameterMap().forEach((key, values) -> {
            if (values != null && values.length > 0) {
                params.put(key, values[0]);
            }
        });
        return params;
    }

    /// 组装凭证并转发子应用验签解析
    private EasyPayCallbackParseResp parse(Map<String, String> params, EasyPayKeyConfig keyConfig) {
        EasyPaySdkCredential credential = new EasyPaySdkCredential();
        credential.setServerUrl(keyConfig.getServerUrl());
        credential.setPartnerId(keyConfig.getPartnerId());
        credential.setMerchantPrivateKey(keyConfig.getMerchantPrivateKey());
        credential.setPlatformPublicKey(keyConfig.getPlatformPublicKey());
        EasyPayCallbackParseReq req = new EasyPayCallbackParseReq();
        req.setCredential(credential);
        req.setParams(params);
        var result = easyPayChannelClient.parsePayCallback(req);
        if (result.getCode() != 0) {
            log.error("易支付回调: 子应用解析失败: {}", result.getMsg());
            return null;
        }
        return result.getData();
    }
}
