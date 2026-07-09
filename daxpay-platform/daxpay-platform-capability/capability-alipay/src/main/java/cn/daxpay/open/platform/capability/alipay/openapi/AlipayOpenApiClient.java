package cn.daxpay.open.platform.capability.alipay.openapi;

import cn.daxpay.open.platform.capability.alipay.auth.config.AlipayAuthConfig;
import cn.daxpay.open.platform.capability.alipay.auth.config.AlipayAuthTypeEnum;
import cn.daxpay.open.platform.capability.alipay.auth.result.AlipayAuthResult;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/// # 支付宝开放平台 OpenAPI 客户端
///
/// 仅实现 `alipay.system.oauth.token`(auth_code 换 userId/openId/accessToken),
/// 不依赖官方 SDK。支持公钥模式与证书模式。
///
@Slf4j
@UtilityClass
public class AlipayOpenApiClient {

    private static final String GATEWAY_PRODUCTION = "https://openapi.alipay.com/gateway.do";

    private static final String GATEWAY_SANDBOX = "https://openapi-sandbox.dl.alipaydev.com/gateway.do";

    private static final String METHOD_OAUTH_TOKEN = "alipay.system.oauth.token";

    private static final String RESPONSE_NODE = "alipay_system_oauth_token_response";

    private static final String ERROR_NODE = "error_response";

    private static final DateTimeFormatter TIMESTAMP_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private static final int HTTP_TIMEOUT_MS = 15_000;

    /// 调用 `alipay.system.oauth.token` 换取用户标识
    ///
    /// @param config   支付宝凭据
    /// @param authCode 授权码
    /// @param sandbox  是否沙箱网关
    public AlipayAuthResult oauthToken(AlipayAuthConfig config, String authCode, boolean sandbox) {
        Map<String, String> params = buildCommonParams(config);
        params.put("grant_type", "authorization_code");
        params.put("code", authCode);
        // 证书模式附加 SN
        if (AlipayAuthTypeEnum.fromCode(config.getAuthType()).isCert()) {
            params.put("app_cert_sn", AlipayCertUtil.getAppCertSn(config.getAppCert()));
            params.put("alipay_root_cert_sn", AlipayCertUtil.getRootCertSn(config.getAlipayRootCert()));
        }
        String signContent = AlipayOpenApiSigner.getSignContent(params);
        params.put("sign", AlipayOpenApiSigner.rsa2Sign(signContent, config.getPrivateKey()));

        String gateway = sandbox ? GATEWAY_SANDBOX : GATEWAY_PRODUCTION;
        String body;
        try (HttpResponse response = HttpRequest.post(gateway)
                .form(new HashMap<>(params))
                .timeout(HTTP_TIMEOUT_MS)
                .execute()) {
            body = response.body();
        } catch (Exception e) {
            log.error("支付宝 OpenAPI 请求失败, method={}", METHOD_OAUTH_TOKEN, e);
            throw new IllegalStateException("alipay openapi request failed: " + e.getMessage(), e);
        }
        if (body == null || body.isBlank()) {
            throw new IllegalStateException("alipay openapi empty response");
        }
        // 验签
        verifyResponse(body, config);
        // 解析业务节点
        JSONObject root = JSONUtil.parseObj(body);
        JSONObject biz = root.getJSONObject(RESPONSE_NODE);
        if (biz == null) {
            biz = root.getJSONObject(ERROR_NODE);
        }
        if (biz == null) {
            throw new IllegalStateException("alipay openapi missing response node");
        }
        String code = biz.getStr("code");
        if (code != null && !code.isBlank() && !"10000".equals(code)) {
            String subMsg = biz.getStr("sub_msg");
            String msg = subMsg != null && !subMsg.isBlank() ? subMsg : biz.getStr("msg");
            throw new IllegalStateException(msg != null ? msg : code);
        }
        return new AlipayAuthResult()
                .setUserId(biz.getStr("user_id"))
                .setOpenId(biz.getStr("open_id"))
                .setAccessToken(biz.getStr("access_token"));
    }

    /// 组装公共请求参数
    private Map<String, String> buildCommonParams(AlipayAuthConfig config) {
        Map<String, String> params = new HashMap<>();
        params.put("app_id", config.getAppId());
        params.put("method", METHOD_OAUTH_TOKEN);
        params.put("format", "JSON");
        params.put("charset", "UTF-8");
        params.put("sign_type", "RSA2");
        params.put("timestamp", LocalDateTime.now().format(TIMESTAMP_FMT));
        params.put("version", "1.0");
        return params;
    }

    /// 响应验签: 截取 response 节点原文 + RSA2
    private void verifyResponse(String body, AlipayAuthConfig config) {
        String sign = extractJsonStringField(body, "sign");
        if (sign == null || sign.isBlank()) {
            // 部分错误响应可能无 sign, 交给业务解析抛错
            return;
        }
        String source = extractSignSource(body);
        if (source == null || source.isBlank()) {
            throw new IllegalStateException("alipay response sign source empty");
        }
        String publicKey = resolveVerifyPublicKey(config);
        if (!AlipayOpenApiSigner.rsa2Verify(source, sign, publicKey)) {
            throw new IllegalStateException("alipay response sign verify failed");
        }
    }

    /// 公钥模式用配置公钥; 证书模式从支付宝公钥证书提取
    private String resolveVerifyPublicKey(AlipayAuthConfig config) {
        if (AlipayAuthTypeEnum.fromCode(config.getAuthType()).isCert()) {
            return AlipayCertUtil.getAlipayPublicKeyFromCert(config.getAlipayCert());
        }
        return config.getAlipayPublicKey();
    }

    /// 截取 `alipay_system_oauth_token_response` 或 `error_response` 节点的 JSON 原文
    private String extractSignSource(String body) {
        int index = body.indexOf(RESPONSE_NODE);
        String node = RESPONSE_NODE;
        if (index < 0) {
            index = body.indexOf(ERROR_NODE);
            node = ERROR_NODE;
        }
        if (index < 0) {
            return null;
        }
        // 节点名后的 `":` 再往后找 `{` 或 `"`
        int startSearch = index + node.length() + 2;
        return extractSignContent(body, startSearch);
    }

    /// 对齐官方 `AlipaySignature.extractSignContent`: 从 begin 起找到 `{...}` 或 `"..."` 片段
    private String extractSignContent(String responseString, int begin) {
        int beginIndex = begin;
        while (beginIndex < responseString.length()
                && responseString.charAt(beginIndex) != '{'
                && responseString.charAt(beginIndex) != '"') {
            beginIndex++;
        }
        if (beginIndex >= responseString.length()) {
            return null;
        }
        int endIndex;
        if (responseString.charAt(beginIndex) == '{') {
            endIndex = extractJsonObjectEndPosition(responseString, beginIndex);
        } else {
            endIndex = extractJsonStringEndPosition(responseString, beginIndex);
        }
        return responseString.substring(beginIndex, endIndex);
    }

    private int extractJsonObjectEndPosition(String responseString, int beginPosition) {
        LinkedList<Character> braces = new LinkedList<>();
        boolean inQuotes = false;
        int consecutiveEscapeCount = 0;
        for (int index = beginPosition; index < responseString.length(); index++) {
            char currentChar = responseString.charAt(index);
            if (currentChar == '"' && consecutiveEscapeCount % 2 == 0) {
                inQuotes = !inQuotes;
            } else if (currentChar == '{' && !inQuotes) {
                braces.push('{');
            } else if (currentChar == '}' && !inQuotes) {
                braces.pop();
                if (braces.isEmpty()) {
                    return index + 1;
                }
            }
            if (currentChar == '\\') {
                consecutiveEscapeCount++;
            } else {
                consecutiveEscapeCount = 0;
            }
        }
        return responseString.length();
    }

    private int extractJsonStringEndPosition(String responseString, int beginPosition) {
        for (int index = beginPosition; index < responseString.length(); index++) {
            if (responseString.charAt(index) == '"' && index != beginPosition) {
                return index + 1;
            }
        }
        return responseString.length();
    }

    /// 粗提取顶层 JSON 字符串字段(用于 sign)
    private String extractJsonStringField(String body, String field) {
        String pattern = "\"" + field + "\"";
        int idx = body.lastIndexOf(pattern);
        if (idx < 0) {
            return null;
        }
        int colon = body.indexOf(':', idx + pattern.length());
        if (colon < 0) {
            return null;
        }
        int start = colon + 1;
        while (start < body.length() && Character.isWhitespace(body.charAt(start))) {
            start++;
        }
        if (start >= body.length() || body.charAt(start) != '"') {
            return null;
        }
        start++;
        StringBuilder sb = new StringBuilder();
        boolean escape = false;
        for (int i = start; i < body.length(); i++) {
            char c = body.charAt(i);
            if (escape) {
                sb.append(c);
                escape = false;
                continue;
            }
            if (c == '\\') {
                escape = true;
                continue;
            }
            if (c == '"') {
                return sb.toString();
            }
            sb.append(c);
        }
        return null;
    }
}
