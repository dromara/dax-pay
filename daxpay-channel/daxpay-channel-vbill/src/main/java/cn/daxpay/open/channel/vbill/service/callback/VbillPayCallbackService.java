package cn.daxpay.open.channel.vbill.service.callback;

import cn.daxpay.open.channel.vbill.client.VbillChannelClient;
import cn.daxpay.open.channel.vbill.client.credential.VbillSdkCredential;
import cn.daxpay.open.channel.vbill.client.req.VbillCallbackParseReq;
import cn.daxpay.open.channel.vbill.client.resp.VbillCallbackParseResp;
import cn.daxpay.open.channel.vbill.dao.isv.VbillIsvKeyConfigManager;
import cn.daxpay.open.channel.vbill.entity.isv.VbillIsvKeyConfig;
import cn.daxpay.open.payment.common.callback.CallbackData;
import cn.daxpay.open.payment.core.trade.service.PayCallbackService;
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

/// # 随行付支付回调处理服务
///
/// 随行付(天阙科技)异步通知 → 主应用接收 JSON body → 转发子应用用天阙公钥 SHA1withRSA 验签 →
/// 构建 [CallbackData] 交由 [PayCallbackService] 更新订单状态。
///
/// 验签只需全局服务商公钥(从 VbillIsvKeyConfig 读取), 凭 ordNo 反查 PayTrade。
/// 主应用零加密代码, 验签/解析集中在子应用 dax-pay-channel-two。
///
/// 成功响应: 返回 `{"code":"success","msg":"成功"}` JSON; 验签/解析失败返回 code=fail。
@Slf4j
@Service
@RequiredArgsConstructor
public class VbillPayCallbackService {

    /// 回调成功响应 code
    public static final String RESP_CODE_SUCCESS = "success";
    public static final String RESP_CODE_FAIL = "fail";

    private final VbillChannelClient vbillChannelClient;
    private final VbillIsvKeyConfigManager vbillIsvKeyConfigManager;
    private final PayCallbackService payCallbackService;

    /// 支付回调处理
    public Map<String, String> payHandle(HttpServletRequest request) {
        Map<String, String> resp = new HashMap<>(4);
        String body = JakartaServletUtil.getBody(request);
        if (StrUtil.isBlank(body)) {
            log.error("随行付支付回调: body 为空");
            resp.put("code", RESP_CODE_FAIL);
            resp.put("msg", "body 为空");
            return resp;
        }

        // 获取全局服务商公钥(只读查询)
        VbillIsvKeyConfig keyConfig = vbillIsvKeyConfigManager.findByProduct(ProductEnum.VBILL_PAY.getCode())
                .orElse(null);
        if (keyConfig == null || keyConfig.getPublicKey() == null) {
            log.error("随行付支付回调: 服务商密钥未配置, 无法验签");
            resp.put("code", RESP_CODE_FAIL);
            resp.put("msg", "密钥未配置");
            return resp;
        }

        // 转发子应用验签解析
        VbillCallbackParseResp parseResp = parse(body, keyConfig.getPublicKey(), false);
        if (parseResp == null || !Boolean.TRUE.equals(parseResp.getSuccess())) {
            log.error("随行付支付回调验签失败");
            resp.put("code", RESP_CODE_FAIL);
            resp.put("msg", "验签失败");
            return resp;
        }

        // 构建 CallbackData 交框架更新订单状态
        CallbackData callbackData = new CallbackData();
        // ordNo = 下单时传入的平台 tradeNo
        callbackData.setTradeNo(parseResp.getOutTradeNo());
        callbackData.setOutTradeNo(parseResp.getTradeNo());
        if (Objects.equals(parseResp.getTradeStatus(), "SUCCESS")) {
            callbackData.setTradeStatus(CallbackStatusEnum.SUCCESS.getCode());
        } else {
            callbackData.setCallbackErrorMsg("随行付回调状态非成功: " + parseResp.getTradeStatus());
        }
        callbackData.setFinishTime(parseResp.getFinishTime());
        try {
            payCallbackService.payCallback(callbackData);
            resp.put("code", RESP_CODE_SUCCESS);
            resp.put("msg", "成功");
        } catch (Exception e) {
            log.error("随行付支付回调业务处理失败: tradeNo={}", callbackData.getTradeNo(), e);
            resp.put("code", RESP_CODE_FAIL);
            resp.put("msg", "业务处理失败");
        }
        return resp;
    }

    /// 转发子应用验签解析
    VbillCallbackParseResp parse(String body, String publicKey, boolean refund) {
        VbillSdkCredential credential = new VbillSdkCredential();
        credential.setPublicKey(publicKey);
        VbillCallbackParseReq req = new VbillCallbackParseReq();
        req.setCredential(credential);
        req.setBody(body);
        var result = refund
                ? vbillChannelClient.parseRefundCallback(req)
                : vbillChannelClient.parsePayCallback(req);
        if (result.getCode() != 0) {
            log.error("随行付回调: 子应用解析失败: {}", result.getMsg());
            return null;
        }
        return result.getData();
    }
}
