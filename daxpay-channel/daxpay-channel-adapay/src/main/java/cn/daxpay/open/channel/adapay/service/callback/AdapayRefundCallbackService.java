package cn.daxpay.open.channel.adapay.service.callback;

import cn.daxpay.open.channel.adapay.code.AdapayCode;
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
import java.util.Objects;

/// # 汇付天下退款回调处理服务
///
/// 汇付退款异步通知 → 主应用接收 {data, signature} → 用平台公钥验签 → 记录退款结果。
///
/// TODO 退款单状态更新待接入退款回调框架(平台目前无独立 RefundCallbackService,
///      后续可通过 PayRefundService 或新增退款回调入口完成状态流转)。
@Slf4j
@Service
@RequiredArgsConstructor
public class AdapayRefundCallbackService {

    /// 退款回调处理
    public String refundHandle(HttpServletRequest request) {
        String body = JakartaServletUtil.getBody(request);
        if (StrUtil.isBlank(body)) {
            log.error("汇付天下退款回调: body 为空");
            return AdapayCode.NOTIFY_FAIL;
        }

        JSONObject outer;
        try {
            outer = JSONUtil.parseObj(body);
        } catch (Exception e) {
            log.error("汇付天下退款回调 JSON 解析失败: body={}", body);
            return AdapayCode.NOTIFY_FAIL;
        }
        String data = outer.getStr("data");
        String signature = outer.getStr("signature");
        if (StrUtil.isBlank(data) || StrUtil.isBlank(signature)) {
            log.error("汇付天下退款回调: 缺少 data 或 signature 字段");
            return AdapayCode.NOTIFY_FAIL;
        }

        // 验签
        if (!verifySign(data, signature)) {
            log.error("汇付天下退款回调验签失败");
            return AdapayCode.NOTIFY_FAIL;
        }

        // 解析退款结果
        JSONObject dataObj = JSONUtil.parseObj(data);
        String outRefundNo = dataObj.getStr("refund_order_no");
        String status = dataObj.getStr("status");
        log.info("汇付天下退款回调: outRefundNo={}, status={}", outRefundNo, status);
        if (Objects.equals("succeeded", status)) {
            // TODO 退款成功, 更新退款单状态(待接入退款回调框架)
            log.info("汇付天下退款成功: outRefundNo={}", outRefundNo);
        }
        return AdapayCode.NOTIFY_SUCCESS;
    }

    /// 验证回调签名(SHA1withRSA + 平台公钥, 与支付回调一致)
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
            log.error("汇付天下退款回调验签异常", e);
            return false;
        }
    }
}
