package cn.daxpay.open.platform.capability.douyin.auth.service;

import cn.daxpay.open.platform.capability.douyin.auth.result.DouyinJsapiConfigResult;
import cn.daxpay.open.platform.common.redis.lock.LockExecutor;
import cn.daxpay.open.platform.common.redis.lock.TryLockResult;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/// # 抖音开放平台 Token / Ticket 管理服务
///
/// 用于 H5 JSAPI 调起前置 `sdk.config` 验签:
/// - **client_access_token**: 由 client_key + client_secret 换取, 缓存 ~7200s
/// - **jsapi_ticket**:       由 client_access_token 换取, 缓存 ~7200s
///
/// 参考范式: [cn.daxpay.open.platform.capability.wechat.token.service.WechatTokenService]
///
/// 参考文档:
/// - client_token: https://developer.open-douyin.com/docs/resource/zh-CN/dop/develop/openapi/account-permission/client-token
/// - get_ticket:   https://developer.open-douyin.com/docs/resource/zh-CN/dop/develop/openapi/tools-ability/jsb-management/get-jsticket
@Slf4j
@Service
@RequiredArgsConstructor
public class DouyinOpenTokenService {

    private final RedisTemplate<String, String> redisTemplate;
    private final LockExecutor lockExecutor;

    /// client_token 换取地址
    private static final String CLIENT_TOKEN_URL = "https://open.douyin.com/oauth/client_token/";
    /// jsapi_ticket 换取地址(注意是 getticket 一个词, 无下划线; HTTP 方法为 GET, 不要 body)
    private static final String JSAPI_TICKET_URL = "https://open.douyin.com/js/getticket/";

    /// Redis Key 前缀
    private static final String CLIENT_TOKEN_KEY = "douyin:open:client_token:";
    private static final String TICKET_KEY = "douyin:open:jsapi_ticket:";
    private static final String CLIENT_TOKEN_LOCK_KEY = "douyin:open:client_token:lock:";
    private static final String TICKET_LOCK_KEY = "douyin:open:jsapi_ticket:lock:";

    /// 缓存过期时间(抖音 token/ticket 有效期 7200s, 提前 5 分钟刷新)
    private static final Duration CACHE_EXPIRE = Duration.ofSeconds(7200 - 300);

    /// 获取 client_access_token(自动刷新, 支持多副本部署)
    public String getClientAccessToken(String clientKey, String clientSecret) {
        String cacheKey = CLIENT_TOKEN_KEY + clientKey;
        String token = redisTemplate.opsForValue().get(cacheKey);
        if (StrUtil.isNotBlank(token)) {
            return token;
        }
        return refreshClientAccessToken(clientKey, clientSecret);
    }

    /// 强制刷新 client_access_token(分布式锁 + 双重检查)
    public String refreshClientAccessToken(String clientKey, String clientSecret) {
        String lockKey = CLIENT_TOKEN_LOCK_KEY + clientKey;
        String cacheKey = CLIENT_TOKEN_KEY + clientKey;
        TryLockResult<String> result = lockExecutor.tryExecute(lockKey,
                30_000L, 5_000L, () -> {
                    // 双重检查
                    String cached = redisTemplate.opsForValue().get(cacheKey);
                    if (StrUtil.isNotBlank(cached)) {
                        return cached;
                    }
                    log.info("刷新抖音 client_access_token, clientKey: {}", clientKey);
                    Map<String, Object> body = new HashMap<>();
                    body.put("client_key", clientKey);
                    body.put("client_secret", clientSecret);
                    body.put("grant_type", "client_credential");
                    String token = postJsonAndExtract(CLIENT_TOKEN_URL, body, "access_token");
                    redisTemplate.opsForValue().set(cacheKey, token, CACHE_EXPIRE);
                    log.info("刷新抖音 client_access_token 成功, clientKey: {}", clientKey);
                    return token;
                });
        if (!result.acquired()) {
            log.warn("获取抖音 client_access_token 刷新锁失败, clientKey: {}", clientKey);
            String cached = redisTemplate.opsForValue().get(cacheKey);
            if (StrUtil.isNotBlank(cached)) {
                return cached;
            }
            // 抖音: 获取client_access_token刷新锁失败
            throw new BizInfoException(CommonErrorCode.SYSTEM_ERROR,
                    "error.channel.douyin.tokenLockFailed");
        }
        return result.value();
    }

    /// 获取 jsapi_ticket(自动刷新, 依赖 client_access_token)
    public String getJsapiTicket(String clientKey, String clientSecret) {
        String cacheKey = TICKET_KEY + clientKey;
        String ticket = redisTemplate.opsForValue().get(cacheKey);
        if (StrUtil.isNotBlank(ticket)) {
            return ticket;
        }
        return refreshJsapiTicket(clientKey, clientSecret);
    }

    /// 强制刷新 jsapi_ticket(分布式锁 + 双重检查)
    public String refreshJsapiTicket(String clientKey, String clientSecret) {
        String lockKey = TICKET_LOCK_KEY + clientKey;
        String cacheKey = TICKET_KEY + clientKey;
        TryLockResult<String> result = lockExecutor.tryExecute(lockKey,
                30_000L, 5_000L, () -> {
                    String cached = redisTemplate.opsForValue().get(cacheKey);
                    if (StrUtil.isNotBlank(cached)) {
                        return cached;
                    }
                    String accessToken = getClientAccessToken(clientKey, clientSecret);
                    log.info("刷新抖音 jsapi_ticket, clientKey: {}", clientKey);
                    // getticket 是 GET + access-token header, 无 body
                    // 错误用 POST/带 body 会报 28001007 参数不合法
                    String ticket = getWithAuth(JSAPI_TICKET_URL, accessToken, "ticket");
                    redisTemplate.opsForValue().set(cacheKey, ticket, CACHE_EXPIRE);
                    log.info("刷新抖音 jsapi_ticket 成功, clientKey: {}", clientKey);
                    return ticket;
                });
        if (!result.acquired()) {
            log.warn("获取抖音 jsapi_ticket 刷新锁失败, clientKey: {}", clientKey);
            String cached = redisTemplate.opsForValue().get(cacheKey);
            if (StrUtil.isNotBlank(cached)) {
                return cached;
            }
            // 抖音: 获取jsapi_ticket刷新锁失败
            throw new BizInfoException(CommonErrorCode.SYSTEM_ERROR,
                    "error.channel.douyin.ticketLockFailed");
        }
        return result.value();
    }

    /// 生成给前端的 sdk.config 验签包(MD5 signature)
    ///
    /// 签名拼接(字典序): `jsapi_ticket={}&nonce_str={}&timestamp={}&url={}`
    /// 详情参考: https://developer.open-douyin.com/docs/resource/zh-CN/dop/develop/sdk/web-app/js/signature
    public DouyinJsapiConfigResult buildJsapiConfig(String clientKey, String clientSecret, String url) {
        if (StrUtil.hasBlank(clientKey, clientSecret, url)) {
            // 抖音: JS-SDK配置参数不能为空
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "error.channel.douyin.jsapiConfigParamBlank");
        }
        String ticket = getJsapiTicket(clientKey, clientSecret);
        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
        String nonceStr = RandomUtil.randomString(32);

        // 字典序拼接(jsapi_ticket < nonce_str < timestamp < url)
        String signStr = String.format("jsapi_ticket=%s&nonce_str=%s&timestamp=%s&url=%s",
                ticket, nonceStr, timestamp, url);
        String signature = cn.hutool.crypto.SecureUtil.md5(signStr);

        return new DouyinJsapiConfigResult()
                .setClientKey(clientKey)
                .setTimestamp(timestamp)
                .setNonceStr(nonceStr)
                .setSignature(signature);
    }

    /// POST JSON 调抖音接口, 提取 data.{field}
    private String postJsonAndExtract(String url, Map<String, Object> body, String field) {
        return postJson(url, body, field);
    }

    /// GET 调抖音接口(仅带 access-token header, 无 body), 提取 data.{field}
    ///
    /// 适用于 jsapi_ticket 等纯授权接口, 参考文档:
    /// https://developer.open-douyin.com/docs/resource/zh-CN/dop/develop/openapi/tools-ability/jsb-management/get-jsticket
    private String getWithAuth(String url, String accessToken, String field) {
        HttpRequest request = HttpUtil.createGet(url)
                .header("Content-Type", "application/json")
                .header("access-token", accessToken);
        return doRequestAndExtract(request, url, field);
    }

    /// 统一 POST JSON 调用, 解析 `data.{field}`, 失败抛业务异常
    private String postJson(String url, Map<String, Object> body, String field) {
        HttpRequest request = HttpUtil.createPost(url)
                .header("Content-Type", "application/json");
        request.body(JSONUtil.toJsonStr(body));
        return doRequestAndExtract(request, url, field);
    }

    /// 执行请求并解析 `data.{field}`, 失败抛业务异常
    private String doRequestAndExtract(HttpRequest request, String url, String field) {
        String respBody;
        try (HttpResponse response = request.execute()) {
            respBody = response.body();
        }
        log.info("抖音开放平台响应: url={}, body={}", url, respBody);
        JSONObject obj = JSONUtil.parseObj(respBody);
        JSONObject data = obj.getJSONObject("data");
        if (data == null) {
            // 抖音: 开放平台接口调用失败
            throw new BizInfoException(CommonErrorCode.SYSTEM_ERROR,
                    "error.channel.douyin.openApiFailed", obj.getStr("message", respBody));
        }
        // 抖音错误码: data.error_code 非 0 视为失败
        int errorCode = data.getInt("error_code", -1);
        if (errorCode != 0) {
            // 抖音: 开放平台接口返回错误
            throw new BizInfoException(CommonErrorCode.SYSTEM_ERROR,
                    "error.channel.douyin.openApiFailed",
                    data.getStr("description", "error_code=" + errorCode));
        }
        String value = data.getStr(field);
        if (StrUtil.isBlank(value)) {
            // 抖音: 开放平台接口返回字段为空
            throw new BizInfoException(CommonErrorCode.SYSTEM_ERROR,
                    "error.channel.douyin.openApiFailed", field + " is blank");
        }
        return value;
    }
}
