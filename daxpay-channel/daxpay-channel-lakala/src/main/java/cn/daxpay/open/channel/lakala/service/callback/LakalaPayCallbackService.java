package cn.daxpay.open.channel.lakala.service.callback;

import cn.daxpay.open.channel.lakala.client.LakalaChannelClient;
import cn.daxpay.open.channel.lakala.client.credential.LakalaSdkCredential;
import cn.daxpay.open.channel.lakala.client.req.LakalaCallbackParseReq;
import cn.daxpay.open.channel.lakala.client.resp.LakalaCallbackParseResp;
import cn.daxpay.open.channel.lakala.dao.isv.LakalaIsvKeyConfigManager;
import cn.daxpay.open.channel.lakala.entity.isv.LakalaIsvKeyConfig;
import cn.daxpay.open.payment.trade.runtime.bo.CallbackData;
import cn.daxpay.open.payment.trade.record.service.PayCallbackRecordService;
import cn.daxpay.open.payment.trade.runtime.service.callback.PayCallbackService;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import cn.daxpay.open.platform.core.enums.pay.notice.CallbackStatusEnum;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.JakartaServletUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/// # 拉卡拉支付回调处理服务
///
/// 拉卡拉异步通知 → 主应用接收原始 header + body → 转发子应用验签与解析
/// (Authorization 头 SHA256withRSA + 拉卡拉公钥证书) →
/// 构建 [CallbackData] 交由 [PayCallbackService] 更新订单状态。
///
/// 验签只需全局服务商公钥(从 LakalaIsvKeyConfig 读取), 凭 out_trade_no 反查 PayTrade。
/// 主应用零加密代码, 验签/解析集中在子应用 dax-pay-channel-two。
@Slf4j
@Service
@RequiredArgsConstructor
public class LakalaPayCallbackService {

    private static final String NOTIFY_SUCCESS = "success";
    private static final String NOTIFY_FAIL = "fail";

    private final LakalaChannelClient lakalaChannelClient;
    private final LakalaIsvKeyConfigManager lakalaIsvKeyConfigManager;
    private final PayCallbackService payCallbackService;
    private final PayCallbackRecordService payCallbackRecordService;

    /// 支付回调处理
    public String payHandle(String channelMchNo, HttpServletRequest request) {
        // 1. 提取回调原始数据
        String body = JakartaServletUtil.getBody(request);
        Map<String, String> headerMap = JakartaServletUtil.getHeaderMap(request);
        Map<String, Object> notify = new HashMap<>();
        notify.put("body", body);
        notify.put("headers", headerMap);

        // 2. 获取全局服务商公钥(只读查询)
        LakalaIsvKeyConfig keyConfig = lakalaIsvKeyConfigManager.findByProduct(ProductEnum.LAKALA_PAY.getCode())
                .orElse(null);
        if (keyConfig == null || keyConfig.getPublicKey() == null) {
            log.error("拉卡拉支付回调: 服务商密钥未配置, 无法验签");
            CallbackData failData = new CallbackData();
            failData.setCallbackData(notify);
            failData.setCallbackStatus(CallbackStatusEnum.FAIL);
            failData.setCallbackErrorMsg("拉卡拉支付回调: 服务商密钥未配置");
            payCallbackRecordService.savePay(channelMchNo, failData);
            return NOTIFY_FAIL;
        }

        // 3. 转发子应用验签解析
        LakalaCallbackParseResp resp = parse(body, headerMap, keyConfig.getPublicKey(), false);
        if (resp == null || !Boolean.TRUE.equals(resp.getSuccess())) {
            log.error("拉卡拉支付回调验签失败");
            CallbackData failData = new CallbackData();
            failData.setCallbackData(notify);
            failData.setCallbackStatus(CallbackStatusEnum.FAIL);
            failData.setCallbackErrorMsg("拉卡拉支付回调验签失败");
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
            callbackData.setCallbackErrorMsg("拉卡拉回调状态非成功: " + resp.getTradeStatus());
        }
        callbackData.setFinishTime(resp.getFinishTime());
        notify.put("outTradeNo", resp.getOutTradeNo());
        notify.put("tradeNo", resp.getTradeNo());
        notify.put("tradeStatus", resp.getTradeStatus());
        callbackData.setCallbackData(notify);
        try {
            payCallbackService.payCallback(callbackData);
        } catch (Exception e) {
            log.error("拉卡拉支付回调业务处理失败: tradeNo={}", callbackData.getTradeNo(), e);
            callbackData.setCallbackStatus(CallbackStatusEnum.EXCEPTION).setCallbackErrorMsg(e.getMessage());
            payCallbackRecordService.savePay(channelMchNo, callbackData);
            return NOTIFY_FAIL;
        }
        payCallbackRecordService.savePay(channelMchNo, callbackData);
        return NOTIFY_SUCCESS;
    }

    /// 转发子应用验签解析
    LakalaCallbackParseResp parse(String body, Map<String, String> headerMap, String publicKey, boolean refund) {
        LakalaSdkCredential credential = new LakalaSdkCredential();
        credential.setPublicKey(publicKey);
        LakalaCallbackParseReq req = new LakalaCallbackParseReq();
        req.setCredential(credential);
        req.setBody(body);
        req.setHeaders(headerMap);
        var result = refund
                ? lakalaChannelClient.parseRefundCallback(req)
                : lakalaChannelClient.parsePayCallback(req);
        if (result.getCode() != 0) {
            log.error("拉卡拉回调: 子应用解析失败: {}", result.getMsg());
            return null;
        }
        return result.getData();
    }

    /// 提取 header(大小写兼容)
    static String getHeader(Map<String, String> headerMap, String name) {
        String value = headerMap.get(name);
        return value != null ? value : headerMap.get(StrUtil.isBlank(name) ? name : name.toLowerCase());
    }
}
