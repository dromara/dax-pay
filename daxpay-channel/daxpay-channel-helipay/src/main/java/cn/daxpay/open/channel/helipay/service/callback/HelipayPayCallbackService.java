package cn.daxpay.open.channel.helipay.service.callback;

import cn.daxpay.open.channel.helipay.client.HelipayChannelClient;
import cn.daxpay.open.channel.helipay.client.credential.HelipaySdkCredential;
import cn.daxpay.open.channel.helipay.client.req.HelipayCallbackParseReq;
import cn.daxpay.open.channel.helipay.client.resp.HelipayCallbackParseResp;
import cn.daxpay.open.channel.helipay.code.HelipayCode;
import cn.daxpay.open.channel.helipay.dao.HelipayKeyConfigManager;
import cn.daxpay.open.channel.helipay.entity.HelipayKeyConfig;
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

/// # 合利宝支付回调处理服务
///
/// 合利宝异步通知(form 参数形态) → 主应用收集请求参数 → 携凭证转发子应用验签与解析
/// (MD5 + 扫码产品签名密钥) → 构建 [CallbackData] 交由 [PayCallbackService] 更新订单状态。
///
/// 凭 rt2_orderId 反查 PayTrade; 主应用零加密代码, 验签/解析集中在子应用 dax-pay-channel-two。
/// 合利宝退款为同步结果模式不设退款回调, 本服务仅处理支付回调。
///
/// 异步通知应答: 处理成功返回纯文本 `success`, 其他视为失败重试(合利宝按固定策略重发)。
@Slf4j
@Service
@RequiredArgsConstructor
public class HelipayPayCallbackService {

    private final HelipayChannelClient helipayChannelClient;

    private final HelipayKeyConfigManager helipayKeyConfigManager;

    private final PayCallbackService payCallbackService;

    private final PayCallbackRecordService payCallbackRecordService;

    /// 支付回调处理
    public String payHandle(String channelMchNo, HttpServletRequest request) {
        // 1. 收集回调参数(合利宝通知为 form 参数形态, 含 sign 字段)
        Map<String, String> params = collectParams(request);
        Map<String, Object> notify = new HashMap<>();
        notify.put("params", params);

        // 2. 解析验签凭证(通道商户维度密钥, 只读查询)
        HelipayKeyConfig keyConfig = helipayKeyConfigManager.findByChannelMchNo(channelMchNo)
                .orElse(null);
        if (Objects.isNull(keyConfig) || Objects.isNull(keyConfig.getSignKey())) {
            log.error("合利宝支付回调: 通道密钥未配置, 无法验签, channelMchNo={}", channelMchNo);
            CallbackData failData = new CallbackData();
            failData.setCallbackData(notify);
            failData.setCallbackStatus(CallbackStatusEnum.FAIL);
            failData.setCallbackErrorMsg("合利宝支付回调: 通道密钥未配置");
            payCallbackRecordService.savePay(channelMchNo, failData);
            return HelipayCode.NOTIFY_FAIL;
        }

        // 3. 转发子应用验签解析
        HelipayCallbackParseResp resp = parse(params, keyConfig);
        if (Objects.isNull(resp) || !Boolean.TRUE.equals(resp.getSuccess())) {
            log.error("合利宝支付回调验签失败: channelMchNo={}", channelMchNo);
            CallbackData failData = new CallbackData();
            failData.setCallbackData(notify);
            failData.setCallbackStatus(CallbackStatusEnum.FAIL);
            failData.setCallbackErrorMsg("合利宝支付回调验签失败");
            payCallbackRecordService.savePay(channelMchNo, failData);
            return HelipayCode.NOTIFY_FAIL;
        }

        // 4. 构建 CallbackData 交框架更新订单状态
        CallbackData callbackData = new CallbackData();
        // resp.outTradeNo 是支付时传入的 P2_orderId = 平台 tradeNo
        callbackData.setTradeNo(resp.getOutTradeNo());
        // resp.outOrderNo 是合利宝系统流水号 rt3_systemSerial
        callbackData.setOutTradeNo(resp.getOutOrderNo());
        callbackData.setFinishTime(resp.getFinishTime());
        // 交易状态映射(rt4_status 标准化: SUCCESS/FAIL)
        if (Objects.equals("SUCCESS", resp.getTradeStatus())) {
            callbackData.setTradeStatus(CallbackStatusEnum.SUCCESS.getCode());
        } else {
            callbackData.setCallbackErrorMsg("合利宝回调状态非成功: " + resp.getTradeStatus());
        }
        callbackData.setCallbackData(notify);
        try {
            payCallbackService.payCallback(callbackData);
        } catch (Exception e) {
            log.error("合利宝支付回调业务处理失败: tradeNo={}", callbackData.getTradeNo(), e);
            callbackData.setCallbackStatus(CallbackStatusEnum.EXCEPTION).setCallbackErrorMsg(e.getMessage());
            payCallbackRecordService.savePay(channelMchNo, callbackData);
            return HelipayCode.NOTIFY_FAIL;
        }
        payCallbackRecordService.savePay(channelMchNo, callbackData);
        return HelipayCode.NOTIFY_SUCCESS;
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
    private HelipayCallbackParseResp parse(Map<String, String> params, HelipayKeyConfig keyConfig) {
        HelipaySdkCredential credential = new HelipaySdkCredential();
        credential.setCustomerNumber(keyConfig.getCustomerNumber());
        credential.setSubCustomerNumber(keyConfig.getSubCustomerNumber());
        credential.setSignKey(keyConfig.getSignKey());
        HelipayCallbackParseReq req = new HelipayCallbackParseReq();
        req.setCredential(credential);
        req.setParams(params);
        var result = helipayChannelClient.parsePayCallback(req);
        if (result.getCode() != 0) {
            log.error("合利宝回调: 子应用解析失败: {}", result.getMsg());
            return null;
        }
        return result.getData();
    }
}
