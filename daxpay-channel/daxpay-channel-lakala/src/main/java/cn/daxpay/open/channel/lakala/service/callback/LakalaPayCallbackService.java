package cn.daxpay.open.channel.lakala.service.callback;

import cn.daxpay.open.channel.lakala.dao.isv.LakalaIsvKeyConfigManager;
import cn.daxpay.open.channel.lakala.entity.isv.LakalaIsvKeyConfig;
import cn.daxpay.open.payment.common.callback.CallbackData;
import cn.daxpay.open.payment.core.trade.service.PayCallbackService;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import cn.daxpay.open.platform.core.enums.pay.notice.CallbackStatusEnum;
import cn.hutool.extra.servlet.JakartaServletUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.Signature;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Map;
import java.util.Objects;

/// # 拉卡拉支付回调处理服务
///
/// 拉卡拉异步通知 → 主应用接收原始 header + body → 用全局服务商公钥 RSA2 验签 →
/// 构建 [CallbackData] 交由 [PayCallbackService] 更新订单状态。
///
/// 拉卡拉回调签名机制(安全统一通知规范):
/// - Authorization header: `LKLAPI-SHA256withRSA timestamp="...",nonce_str="...",signature="..."`
/// - 验签原文: `timestamp + "\n" + nonce_str + "\n" + body + "\n"`
/// - 公钥为 X509 证书(PEM), 与响应验签共用
///
/// 主应用直接验签(签名算法简单, 无需转发子应用)。
@Slf4j
@Service
@RequiredArgsConstructor
public class LakalaPayCallbackService {

    private static final String NOTIFY_SUCCESS = "success";
    private static final String NOTIFY_FAIL = "fail";
    /// 拉卡拉属国内通道, 回调时间字段(trade_time)为东八区本地时间字面量(yyyyMMddHHmmss),
    /// 显式钉死 +08:00 偏移, 避免 OffsetDateTime.parse 因缺 OFFSET_SECONDS 解析失败
    private static final DateTimeFormatter LKL_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("yyyyMMddHHmmss").withZone(ZoneOffset.ofHours(8));

    private final LakalaIsvKeyConfigManager lakalaIsvKeyConfigManager;
    private final PayCallbackService payCallbackService;

    /// 支付回调处理
    public String payHandle(HttpServletRequest request) {
        // 1. 提取回调原始数据
        String body = JakartaServletUtil.getBody(request);
        Map<String, String> headerMap = JakartaServletUtil.getHeaderMap(request);

        // 2. 获取全局服务商公钥(只读查询, 不创建记录)
        LakalaIsvKeyConfig keyConfig = lakalaIsvKeyConfigManager.findByProduct(ProductEnum.LAKALA_PAY.getCode())
                .orElse(null);
        if (keyConfig == null || keyConfig.getPublicKey() == null) {
            log.error("拉卡拉支付回调: 服务商密钥未配置, 无法验签");
            return NOTIFY_FAIL;
        }

        // 3. 验签
        if (!verifyCallbackSign(headerMap, body, keyConfig.getPublicKey())) {
            log.error("拉卡拉支付回调验签失败");
            return NOTIFY_FAIL;
        }

        // 4. 解析回调数据并构建 CallbackData
        CallbackData callbackData = parseCallback(body);
        if (callbackData == null) {
            log.error("拉卡拉支付回调解析失败: body={}", body);
            return NOTIFY_FAIL;
        }
        try {
            // 5. 交由框架更新订单状态
            payCallbackService.payCallback(callbackData);
        } catch (Exception e) {
            log.error("拉卡拉支付回调业务处理失败: tradeNo={}", callbackData.getTradeNo(), e);
            return NOTIFY_FAIL;
        }
        return NOTIFY_SUCCESS;
    }

    /// 验证回调签名(从 Authorization header 解析 timestamp/nonce_str/signature)
    @SuppressWarnings("unchecked")
    private boolean verifyCallbackSign(Map<String, String> headerMap, String body, String publicKey) {
        // Authorization 可能是任意大小写 header 名
        String authorization = headerMap.getOrDefault("Authorization",
                headerMap.getOrDefault("authorization", ""));
        if (authorization.isEmpty()) {
            return false;
        }
        // 解析 authorization: LKLAPI-SHA256withRSA timestamp="xxx",nonce_str="xxx",signature="xxx"
        Map<String, String> authMap = parseAuthorization(authorization);
        String timestamp = authMap.get("timestamp");
        String nonceStr = authMap.get("nonce_str");
        String signature = authMap.get("signature");
        if (signature == null || timestamp == null || nonceStr == null) {
            return false;
        }
        // 验签原文: timestamp\n nonce_str\n body\n
        String source = timestamp + "\n" + nonceStr + "\n" + body + "\n";
        try {
            X509Certificate cert = loadCertificate(publicKey);
            Signature sign = Signature.getInstance("SHA256withRSA");
            sign.initVerify(cert);
            sign.update(source.getBytes(StandardCharsets.UTF_8));
            return sign.verify(Base64.getDecoder().decode(signature));
        } catch (Exception e) {
            log.error("拉卡拉回调验签异常", e);
            return false;
        }
    }

    /// 解析 Authorization header 为 key-value Map
    private Map<String, String> parseAuthorization(String authorization) {
        // 去掉方案前缀 LKLAPI-SHA256withRSA
        int spaceIdx = authorization.indexOf(' ');
        String kvPart = spaceIdx > 0 ? authorization.substring(spaceIdx + 1) : authorization;
        Map<String, String> map = new java.util.HashMap<>();
        for (String pair : kvPart.split(",")) {
            int eqIdx = pair.indexOf('=');
            if (eqIdx > 0) {
                String key = pair.substring(0, eqIdx).trim();
                // 去掉值的引号
                String value = pair.substring(eqIdx + 1).trim();
                if (value.startsWith("\"") && value.endsWith("\"")) {
                    value = value.substring(1, value.length() - 1);
                }
                map.put(key, value);
            }
        }
        return map;
    }

    /// 加载 X509 公钥证书(PEM)
    private X509Certificate loadCertificate(String publicKey) throws Exception {
        String key = publicKey
                .replace("-----BEGIN CERTIFICATE-----", "")
                .replace("-----END CERTIFICATE-----", "")
                .replaceAll("\\s+", "");
        byte[] decoded = Base64.getDecoder().decode(key);
        CertificateFactory cf = CertificateFactory.getInstance("X509");
        return (X509Certificate) cf.generateCertificate(new java.io.ByteArrayInputStream(decoded));
    }

    /// 解析拉卡拉回调 JSON 为 CallbackData
    private CallbackData parseCallback(String body) {
        try {
            cn.hutool.json.JSONObject json = cn.hutool.json.JSONUtil.parseObj(body);
            // 拉卡拉回调外层为 resp_data 包裹(与响应一致), 兼容直接业务数据
            cn.hutool.json.JSONObject data = json.getJSONObject("resp_data");
            if (data == null) {
                data = json;
            }
            CallbackData callbackData = new CallbackData();
            // out_trade_no = 下单时传入的平台 tradeNo
            callbackData.setTradeNo(data.getStr("out_trade_no"));
            // trade_no = 拉卡拉交易号
            callbackData.setOutTradeNo(data.getStr("trade_no"));
            // 交易状态映射
            String tradeState = data.getStr("trade_state");
            if (Objects.equals(tradeState, "SUCCESS")) {
                callbackData.setTradeStatus(CallbackStatusEnum.SUCCESS.getCode());
            } else {
                callbackData.setTradeStatus(tradeState);
                callbackData.setCallbackErrorMsg("拉卡拉回调状态非成功: " + tradeState);
            }
            // 完成时间(yyyyMMddHHmmss → OffsetDateTime)
            String tradeTime = data.getStr("trade_time");
            if (tradeTime != null && !tradeTime.isBlank()) {
                try {
                    callbackData.setFinishTime(OffsetDateTime.parse(tradeTime, LKL_TIME_FORMATTER));
                } catch (Exception e) {
                    log.warn("拉卡拉回调时间解析失败: tradeTime={}", tradeTime);
                }
            }
            return callbackData;
        } catch (Exception e) {
            log.error("拉卡拉回调 JSON 解析失败", e);
            return null;
        }
    }
}
