package cn.daxpay.open.payment.core.assist;

import cn.daxpay.open.payment.common.service.MerchantContextLoader;
import cn.daxpay.open.payment.core.strategy.PaymentStrategyFactory;
import cn.daxpay.open.payment.core.strategy.auth.AbsChannelAuthStrategy;
import cn.daxpay.open.payment.unipay.param.assist.AuthCodeParam;
import cn.daxpay.open.payment.unipay.param.assist.GenerateAuthUrlParam;
import cn.daxpay.open.payment.unipay.result.assist.AuthResult;
import cn.daxpay.open.payment.unipay.result.assist.AuthUrlResult;
import cn.daxpay.open.platform.capability.alipay.auth.config.AlipayAuthConfig;
import cn.daxpay.open.platform.capability.alipay.auth.result.AlipayAuthResult;
import cn.daxpay.open.platform.capability.alipay.auth.service.AlipayAuthCapability;
import cn.daxpay.open.platform.common.json.util.JacksonUtil;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.code.DaxPayErrorCode;
import cn.daxpay.open.platform.core.enums.unipay.ChannelAuthStatusEnum;
import cn.daxpay.open.platform.core.enums.unipay.ChannelAuthTypeEnum;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.system.service.config.PlatformAlipayAuthConfigService;
import cn.daxpay.open.platform.system.service.config.PlatformUrlConfigService;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/// # 通道认证服务, 用户获取OpenId或UserId等标识
///
/// - **支付宝**: 平台级配置 + H5 中间页 JSAPI 取码(调试/支付共用), 不依赖商户上下文
/// - **其他通道**: 按支付产品路由认证策略; H5 授权重定向场景生成 authToken 保存会话
@Slf4j
@Service
@RequiredArgsConstructor
public class ChannelAuthService {
    private final RedisTemplate<String, Object> redisTemplate;

    /// 付款码/道通/调试场景的认证结果查询缓存前缀
    public static final String CHANNEL_AUTH_KEY_PREFIX = "payment:channel-auth:";

    /// H5授权重定向场景的认证会话上下文缓存前缀(与 queryCode 机制解耦)
    public static final String AUTH_SESSION_KEY_PREFIX = "payment:auth-session:";

    /// 支付宝 H5 中间页路径: /auth/alipay/{aliAppId}/{queryCode}
    private static final String ALIPAY_AUTH_PATH = "/auth/alipay/{}/{}";

    /// 会话码/查询码过期时间(分钟)
    private static final long AUTH_TIMEOUT_MINUTES = 5;

    private final MerchantContextLoader merchantContextLoader;
    private final PlatformAlipayAuthConfigService platformAlipayAuthConfigService;
    private final PlatformUrlConfigService platformUrlConfigService;
    private final AlipayAuthCapability alipayAuthCapability;

    /// 获取授权链接
    ///
    /// 支付宝走平台级中间页; 其他通道生成 authToken 并委托产品策略。
    public AuthUrlResult generateAuthUrl(GenerateAuthUrlParam param) {
        // 支付宝: 平台级配置, 不依赖商户/应用
        if (isAlipayAuth(param.getAuthType())) {
            return generateAlipayAuthUrl();
        }
        merchantContextLoader.initMchByApp(param.getAppId());
        var strategy = PaymentStrategyFactory.createByProduct(param.getProduct(), AbsChannelAuthStrategy.class);
        // 生成认证会话码并保存上下文, 授权回调后凭此恢复
        String authToken = IdUtil.fastSimpleUUID();
        AuthSession session = new AuthSession()
                .setProduct(param.getProduct())
                .setChannelMchNo(param.getChannelMchNo())
                .setCapability(param.getCapability())
                .setOpAppId(param.getOpAppId())
                .setReturnPath(param.getReturnPath());
        redisTemplate.opsForValue().set(AUTH_SESSION_KEY_PREFIX + authToken, session, AUTH_TIMEOUT_MINUTES, TimeUnit.MINUTES);
        AuthUrlResult authUrlResult = strategy.generateAuthUrl(param, authToken);
        // 返回查询 Code 则写入 Redis 等待结果
        if (StrUtil.isNotBlank(authUrlResult.getQueryCode())) {
            saveWaitingResult(authUrlResult.getQueryCode());
        }
        return authUrlResult;
    }

    /// 生成支付宝授权中间页链接(平台级, 无商户上下文)
    ///
    /// 二维码指向 H5: `{paymentGatewayBaseUrl}/auth/alipay/{aliAppId}/{queryCode}`,
    /// H5 内通过 JSAPI `ap.getAuthCode` 取码后回调 `auth`。
    public AuthUrlResult generateAlipayAuthUrl() {
        AlipayAuthConfig config = platformAlipayAuthConfigService.toCapabilityConfig();
        if (!alipayAuthCapability.isConfigured(config)) {
            // 支付宝: 平台级支付宝配置不完整, 请先在「三方平台管理」中配置
            throw new BizInfoException(CommonErrorCode.SYSTEM_ERROR, "error.social.alipayNotConfigured");
        }
        String gatewayBase = platformUrlConfigService.getUrlConfig().getPaymentGatewayBaseUrl();
        if (StrUtil.isBlank(gatewayBase)) {
            // 支付网关前端地址未配置
            throw new BizInfoException(DaxPayErrorCode.CONFIG_ERROR, "error.common.gatewayUrlNotConfigured");
        }
        String queryCode = RandomUtil.randomString(10);
        String authPath = StrUtil.format(ALIPAY_AUTH_PATH, config.getAppId(), queryCode);
        String authUrl = StrUtil.removeSuffix(gatewayBase, "/") + authPath;
        saveWaitingResult(queryCode);
        return new AuthUrlResult().setAuthUrl(authUrl).setQueryCode(queryCode);
    }

    /// 通过AuthCode获取认证结果
    ///
    /// 支付宝走平台配置换 userId; 其他通道优先用 authToken 恢复会话再调产品策略。
    public AuthResult auth(AuthCodeParam param) {
        // 支付宝: 平台级换票, 不依赖商户/产品策略
        if (isAlipayAuth(param.getAuthType())) {
            AuthResult authResult = doAlipayAuth(param.getAuthCode());
            authResult.setStatus(ChannelAuthStatusEnum.SUCCESS.getCode());
            if (StrUtil.isNotBlank(param.getQueryCode())) {
                redisTemplate.opsForValue().set(
                        CHANNEL_AUTH_KEY_PREFIX + param.getQueryCode(),
                        authResult,
                        AUTH_TIMEOUT_MINUTES,
                        TimeUnit.MINUTES);
            }
            return authResult;
        }
        merchantContextLoader.initMchByApp(param.getAppId());
        // 尝试从会话码恢复上下文
        AuthSession session = loadSession(param.getAuthToken());
        // product 优先从会话恢复, 其次取参数(小程序直连场景)
        String product = (session != null && StrUtil.isNotBlank(session.getProduct()))
                ? session.getProduct() : param.getProduct();
        var strategy = PaymentStrategyFactory.createByProduct(product, AbsChannelAuthStrategy.class);
        AuthResult authResult = strategy.doAuth(param, session);
        authResult.setStatus(ChannelAuthStatusEnum.SUCCESS.getCode());
        // 会话恢复场景: 回填来源回跳路径, 供前端跳回业务页面
        if (session != null) {
            authResult.setReturnPath(session.getReturnPath());
        }
        // 兼容原 queryCode 机制(付款码/道通/调试场景)
        if (StrUtil.isNotBlank(param.getQueryCode())) {
            redisTemplate.opsForValue().set(
                    CHANNEL_AUTH_KEY_PREFIX + param.getQueryCode(),
                    authResult,
                    AUTH_TIMEOUT_MINUTES,
                    TimeUnit.MINUTES);
        }
        return authResult;
    }

    /// 支付宝 authCode 换 userId/openId(平台级配置)
    private AuthResult doAlipayAuth(String authCode) {
        AlipayAuthConfig config = platformAlipayAuthConfigService.toCapabilityConfig();
        if (!alipayAuthCapability.isConfigured(config)) {
            throw new BizInfoException(CommonErrorCode.SYSTEM_ERROR, "error.social.alipayNotConfigured");
        }
        AlipayAuthResult alipayResult = alipayAuthCapability.getUserId(config, authCode);
        // 统一映射: 支付链路按 openId 取值, 同时回填 userId
        String userId = StrUtil.blankToDefault(alipayResult.getUserId(), alipayResult.getOpenId());
        if (StrUtil.isBlank(userId)) {
            // 支付宝: 获取用户标识失败
            throw new BizInfoException(CommonErrorCode.SYSTEM_ERROR, "error.alipay.authFailed", "userId is blank");
        }
        return new AuthResult()
                .setOpenId(userId)
                .setUserId(userId)
                .setAccessToken(alipayResult.getAccessToken());
    }

    /// 通过查询码获取认证结果(付款码/道通/调试场景)
    public AuthResult queryAuthResult(String queryCode) {
        // 从 redis 中获取，读取后显式转换为目标类型
        var authResult = redisTemplate.opsForValue().get(CHANNEL_AUTH_KEY_PREFIX + queryCode);
        if (Objects.isNull(authResult)) {
            return new AuthResult().setStatus(ChannelAuthStatusEnum.NOT_EXIST.getCode());
        }
        return JacksonUtil.convert(authResult, AuthResult.class);
    }

    /// 写入 WAITING 状态的查询结果
    private void saveWaitingResult(String queryCode) {
        AuthResult authResult = new AuthResult().setStatus(ChannelAuthStatusEnum.WAITING.getCode());
        redisTemplate.opsForValue().set(
                CHANNEL_AUTH_KEY_PREFIX + queryCode,
                authResult,
                AUTH_TIMEOUT_MINUTES,
                TimeUnit.MINUTES);
    }

    /// 是否支付宝认证类型
    private boolean isAlipayAuth(String authType) {
        return Objects.equals(authType, ChannelAuthTypeEnum.ALIPAY.getCode());
    }

    /// 根据 authToken 加载会话上下文, 不存在或过期返回 null
    private AuthSession loadSession(String authToken) {
        if (StrUtil.isBlank(authToken)) {
            return null;
        }
        var cached = redisTemplate.opsForValue().get(AUTH_SESSION_KEY_PREFIX + authToken);
        if (Objects.isNull(cached)) {
            return null;
        }
        return JacksonUtil.convert(cached, AuthSession.class);
    }
}
