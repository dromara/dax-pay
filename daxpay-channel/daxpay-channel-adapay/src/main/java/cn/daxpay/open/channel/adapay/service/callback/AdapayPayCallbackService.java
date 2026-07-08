package cn.daxpay.open.channel.adapay.service.callback;

import cn.daxpay.open.channel.adapay.code.AdapayCode;
import cn.daxpay.open.payment.common.callback.CallbackData;
import cn.daxpay.open.payment.core.trade.service.PayCallbackService;
import cn.daxpay.open.platform.core.enums.pay.notice.CallbackStatusEnum;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.JakartaServletUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/// # Adapay 支付回调处理服务
///
/// Adapay 异步通知 → 主应用接收 {data, signature} → 用平台公钥 SHA1withRSA 验签 data →
/// 构建 [CallbackData] 交由 [PayCallbackService] 更新订单状态。
///
/// 验签只需全局平台公钥(AdapayCode.PLATFORM_PUBLIC_KEY), 不需 channelMchNo,
/// 主应用直接验签(不转发子应用), 凭 order_no 反查 PayTrade。
@Slf4j
@Service
@RequiredArgsConstructor
public class AdapayPayCallbackService {

    private final PayCallbackService payCallbackService;

    /// 支付回调处理
    public String payHandle(HttpServletRequest request) {
        // 1. 提取回调原始 body
        String body = JakartaServletUtil.getBody(request);
        if (StrUtil.isBlank(body)) {
            log.error("Adapay 支付回调: body 为空");
            return AdapayCode.NOTIFY_FAIL;
        }

        // 2. 解析外层 {data, signature}
        JSONObject outer;
        try {
            outer = JSONUtil.parseObj(body);
        } catch (Exception e) {
            log.error("Adapay 支付回调 JSON 解析失败: body={}", body);
            return AdapayCode.NOTIFY_FAIL;
        }
        String data = outer.getStr("data");
        String signature = outer.getStr("signature");
        if (StrUtil.isBlank(data) || StrUtil.isBlank(signature)) {
            log.error("Adapay 支付回调: 缺少 data 或 signature 字段");
            return AdapayCode.NOTIFY_FAIL;
        }

        // 3. 验签(平台公钥 SHA1withRSA)
        if (!verifySign(data, signature)) {
            log.error("Adapay 支付回调验签失败");
            return AdapayCode.NOTIFY_FAIL;
        }

        // 4. 解析 data 并构建 CallbackData
        CallbackData callbackData = parseCallback(data);
        if (callbackData == null) {
            return AdapayCode.NOTIFY_FAIL;
        }
        try {
            // 5. 交由框架更新订单状态
            payCallbackService.payCallback(callbackData);
        } catch (Exception e) {
            log.error("Adapay 支付回调业务处理失败: tradeNo={}", callbackData.getTradeNo(), e);
            return AdapayCode.NOTIFY_FAIL;
        }
        return AdapayCode.NOTIFY_SUCCESS;
    }

    /// 验证回调签名(SHA1withRSA + 平台公钥)
    private boolean verifySign(String data, String signature) {
        try {
            byte[] keyBytes = Base64.getDecoder().decode(AdapayCode.PLATFORM_PUBLIC_KEY);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
            PublicKey pubKey = KeyFactory.getInstance("RSA").generatePublic(keySpec);
            Signature sign = Signature.getInstance("SHA1withRSA");
            sign.initVerify(pubKey);
            sign.update(data.getBytes(StandardCharsets.UTF_8));
            return sign.verify(Base64.getDecoder().decode(signature));
        } catch (Exception e) {
            log.error("Adapay 回调验签异常", e);
            return false;
        }
    }

    /// 解析 data JSON 为 CallbackData
    private CallbackData parseCallback(String data) {
        try {
            JSONObject dataObj = JSONUtil.parseObj(data);
            CallbackData callbackData = new CallbackData();
            // order_no = 下单时传入的平台 tradeNo
            callbackData.setTradeNo(dataObj.getStr("order_no"));
            // id = Adapay 支付对象 ID
            callbackData.setOutTradeNo(dataObj.getStr("id"));
            // 交易状态映射(succeeded → SUCCESS, 其他原样透传)
            String status = dataObj.getStr("status");
            if ("succeeded".equals(status)) {
                callbackData.setTradeStatus(CallbackStatusEnum.SUCCESS.getCode());
            } else {
                callbackData.setTradeStatus(status);
                callbackData.setCallbackErrorMsg("Adapay 回调状态非成功: " + status);
            }
            return callbackData;
        } catch (Exception e) {
            log.error("Adapay 回调 data 解析失败: data={}", data, e);
            return null;
        }
    }
}
