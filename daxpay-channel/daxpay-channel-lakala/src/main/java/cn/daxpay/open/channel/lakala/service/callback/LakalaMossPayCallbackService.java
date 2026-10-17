package cn.daxpay.open.channel.lakala.service.callback;

import cn.daxpay.open.channel.lakala.client.LakalaChannelClient;
import cn.daxpay.open.channel.lakala.client.credential.MossSdkCredential;
import cn.daxpay.open.channel.lakala.client.req.MossCallbackParseReq;
import cn.daxpay.open.channel.lakala.client.resp.MossCallbackParseResp;
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

/// # 拉卡拉 MOSS 支付回调处理服务
///
/// MOSS 异步通知 → 主应用接收原始 body → 转发子应用验签与解析
/// (平台公钥 SHA1withRSA 验签 + 商户私钥解密 requestEncrypted) →
/// 构建 [CallbackData] 交由 [PayCallbackService] 更新订单状态。
///
/// 应答契约(与服务商模式返回裸 success 不同): MOSS 要求应答为
/// `{head:{code:"000000"}, sign}` 结构的**签名 JSON**, 主应用将
/// [MossCallbackParseResp#getResponseBody] 原样作为 HTTP 响应体返回拉卡拉。
@Slf4j
@Service
@RequiredArgsConstructor
public class LakalaMossPayCallbackService {

    /// 无法签名时应答的兜底失败 JSON(密钥未配置等场景, 拉卡拉将重推)
    public static final String NOTIFY_FAIL_BODY = "{\"head\":{\"code\":\"000001\",\"desc\":\"failure\"}}";

    private final LakalaChannelClient lakalaChannelClient;
    private final LakalaIsvKeyConfigManager lakalaIsvKeyConfigManager;
    private final PayCallbackService payCallbackService;
    private final PayCallbackRecordService payCallbackRecordService;

    /// 支付回调处理
    public String payHandle(String channelMchNo, HttpServletRequest request) {
        // 1. 提取回调原始数据
        String body = JakartaServletUtil.getBody(request);
        Map<String, Object> notify = new HashMap<>();
        notify.put("body", body);

        // 2. 获取 MOSS 密钥配置(appid + 商户私钥 + 平台公钥, 只读查询)
        LakalaIsvKeyConfig keyConfig = lakalaIsvKeyConfigManager.findByProduct(ProductEnum.LAKALA_MOSS.getCode())
                .orElse(null);
        if (Objects.isNull(keyConfig) || StrUtil.hasBlank(keyConfig.getLklAppId(), keyConfig.getPrivateKey())) {
            log.error("拉卡拉MOSS支付回调: 密钥未配置, 无法验签解密");
            CallbackData failData = new CallbackData();
            failData.setCallbackData(notify);
            failData.setCallbackStatus(CallbackStatusEnum.FAIL);
            failData.setCallbackErrorMsg("拉卡拉MOSS支付回调: 密钥未配置");
            payCallbackRecordService.savePay(channelMchNo, failData);
            return NOTIFY_FAIL_BODY;
        }

        // 3. 转发子应用验签解析
        MossCallbackParseResp resp = parse(body, false, keyConfig);
        if (Objects.isNull(resp) || !Boolean.TRUE.equals(resp.getSuccess())) {
            log.error("拉卡拉MOSS支付回调验签失败");
            CallbackData failData = new CallbackData();
            failData.setCallbackData(notify);
            failData.setCallbackStatus(CallbackStatusEnum.FAIL);
            failData.setCallbackErrorMsg("拉卡拉MOSS支付回调验签失败");
            payCallbackRecordService.savePay(channelMchNo, failData);
            return Objects.isNull(resp) ? NOTIFY_FAIL_BODY : StrUtil.blankToDefault(resp.getResponseBody(), NOTIFY_FAIL_BODY);
        }

        // 4. 构建 CallbackData 交框架更新订单状态
        CallbackData callbackData = new CallbackData();
        callbackData.setTradeNo(resp.getOutTradeNo());
        callbackData.setOutTradeNo(resp.getTradeNo());
        if (Objects.equals(resp.getTradeStatus(), "SUCCESS")) {
            callbackData.setTradeStatus(CallbackStatusEnum.SUCCESS.getCode());
        } else {
            callbackData.setCallbackErrorMsg("拉卡拉MOSS回调状态非成功: " + resp.getTradeStatus());
        }
        callbackData.setFinishTime(resp.getFinishTime());
        notify.put("outTradeNo", resp.getOutTradeNo());
        notify.put("tradeNo", resp.getTradeNo());
        notify.put("tradeStatus", resp.getTradeStatus());
        callbackData.setCallbackData(notify);
        try {
            payCallbackService.payCallback(callbackData);
        } catch (Exception e) {
            log.error("拉卡拉MOSS支付回调业务处理失败: tradeNo={}", callbackData.getTradeNo(), e);
            callbackData.setCallbackStatus(CallbackStatusEnum.EXCEPTION).setCallbackErrorMsg(e.getMessage());
            payCallbackRecordService.savePay(channelMchNo, callbackData);
            return NOTIFY_FAIL_BODY;
        }
        payCallbackRecordService.savePay(channelMchNo, callbackData);
        // 5. 返回签名应答报文(拉卡拉以此判断通知送达)
        return StrUtil.blankToDefault(resp.getResponseBody(), NOTIFY_FAIL_BODY);
    }

    /// 转发子应用验签解析(支付/退款共用)
    MossCallbackParseResp parse(String body, boolean refund, LakalaIsvKeyConfig keyConfig) {
        MossSdkCredential credential = new MossSdkCredential();
        credential.setAppId(keyConfig.getLklAppId());
        credential.setMchPrivateKey(keyConfig.getPrivateKey());
        credential.setPlatformPublicKey(keyConfig.getPublicKey());
        MossCallbackParseReq req = new MossCallbackParseReq();
        req.setCredential(credential);
        req.setBody(body);
        var result = refund
                ? lakalaChannelClient.mossParseRefundCallback(req)
                : lakalaChannelClient.mossParsePayCallback(req);
        if (result.getCode() != 0) {
            log.error("拉卡拉MOSS回调: 子应用解析失败: {}", result.getMsg());
            return null;
        }
        return result.getData();
    }
}
