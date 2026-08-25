package cn.daxpay.open.platform.system.service.config.security;

import cn.daxpay.open.platform.common.json.util.JacksonUtil;
import cn.daxpay.open.platform.system.convert.config.security.PlatformSecurityConfigConvert;
import cn.daxpay.open.platform.system.entity.config.platform.security.*;
import cn.daxpay.open.platform.system.enums.PayRiskOpenIdLevelEnum;
import cn.daxpay.open.platform.system.enums.PlatformConfigTypeEnum;
import cn.daxpay.open.platform.system.param.config.security.*;
import cn.daxpay.open.platform.system.result.config.security.*;
import cn.daxpay.open.platform.system.service.config.SystemPlatformConfigService;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;

/// # 平台安全配置服务
///
/// 统一管理密码策略、登录安全、会话管理、双因素认证、API安全等安全类配置。
/// API安全配置在支付切面高频读取，使用多级缓存（[system:api-security-config]），
/// 缓存 JSON 字符串避免 L2 Redis 反序列化为 LinkedHashMap 的类型问题。
@Slf4j
@Service
public class PlatformSecurityConfigService {

    /// API安全配置缓存名（L1 Caffeine + L2 Redis）
    public static final String API_SECURITY_CACHE_NAME = "system:api-security-config";

    /// 地理围栏策略默认值（平衡）
    private static final String GEO_FENCE_STRATEGY_BALANCED = "balanced";

    /// 地理围栏策略合法值集合（与 GeoFenceStrategyEnum 取值对齐, 此处不引 payment-core 避免反向依赖）
    private static final java.util.Set<String> GEO_FENCE_STRATEGY_VALID =
            java.util.Set.of("strict", "balanced", "loose");

    private final SystemPlatformConfigService systemConfigService;

    /// 自注入，保证读 API 安全配置走 Spring 缓存代理
    private final PlatformSecurityConfigService self;

    public PlatformSecurityConfigService(
            SystemPlatformConfigService systemConfigService,
            @Lazy PlatformSecurityConfigService self) {
        this.systemConfigService = systemConfigService;
        this.self = self;
    }

    /// 获取密码策略配置
    public PlatformPasswordPolicyConfig getPasswordPolicyConfig() {
        return systemConfigService.getOrCreateConfig(PlatformConfigTypeEnum.SECURITY_PASSWORD_POLICY,
                PlatformPasswordPolicyConfig.class,
                defaultPasswordPolicyConfig());
    }

    /// 密码策略默认值: 向后兼容档, 默认不启用强度校验(与配置缺失时的行为一致), 数值字段填充避免消费端拆箱空指针
    private PlatformPasswordPolicyConfig defaultPasswordPolicyConfig() {
        return new PlatformPasswordPolicyConfig()
                .setEnabled(false)
                .setMinLength(PlatformPasswordPolicyConfig.DEFAULT_MIN_LENGTH)
                .setMaxLength(PlatformPasswordPolicyConfig.DEFAULT_MAX_LENGTH)
                .setRequireUppercase(false)
                .setRequireLowercase(false)
                .setRequireDigit(false)
                .setRequireSpecialChar(false)
                .setSpecialChars(PlatformPasswordPolicyConfig.DEFAULT_SPECIAL_CHARS)
                .setRotationDays(0)
                .setHistoryCount(PlatformPasswordPolicyConfig.DEFAULT_HISTORY_COUNT);
    }

    /// 获取密码策略配置
    public PlatformPasswordPolicyConfigResult findPasswordPolicyConfig() {
        return PlatformSecurityConfigConvert.CONVERT.toPasswordPolicyResult(this.getPasswordPolicyConfig());
    }

    /// 更新密码策略配置
    public void updatePasswordPolicyConfig(PlatformPasswordPolicyConfigParam param) {
        PlatformPasswordPolicyConfig data = this.getPasswordPolicyConfig();
        PlatformSecurityConfigConvert.CONVERT.copy(param, data);
        systemConfigService.updateConfig(PlatformConfigTypeEnum.SECURITY_PASSWORD_POLICY, data);
    }

    /// 获取登录安全配置
    public PlatformLoginSecurityConfig getLoginSecurityConfig() {
        return systemConfigService.getOrCreateConfig(PlatformConfigTypeEnum.SECURITY_LOGIN,
                PlatformLoginSecurityConfig.class,
                defaultLoginSecurityConfig());
    }

    /// 登录安全配置默认值: 向后兼容档, 锁定默认不启用(与配置缺失时行为一致), 数值与消费端兜底值对齐
    private PlatformLoginSecurityConfig defaultLoginSecurityConfig() {
        return new PlatformLoginSecurityConfig()
                .setLockoutEnabled(false)
                .setMaxFailedAttempts(PlatformLoginSecurityConfig.DEFAULT_MAX_FAILED_ATTEMPTS)
                .setLockoutDurationMinutes(PlatformLoginSecurityConfig.DEFAULT_LOCKOUT_DURATION_MINUTES)
                .setFailureResetMinutes(PlatformLoginSecurityConfig.DEFAULT_FAILURE_RESET_MINUTES)
                .setCaptchaEnabled(true)
                .setCaptchaTriggerAttempts(PlatformLoginSecurityConfig.DEFAULT_CAPTCHA_TRIGGER_ATTEMPTS);
    }

    /// 获取登录安全配置
    public PlatformLoginSecurityConfigResult findLoginSecurityConfig() {
        return PlatformSecurityConfigConvert.CONVERT.toLoginSecurityResult(this.getLoginSecurityConfig());
    }

    /// 更新登录安全配置
    public void updateLoginSecurityConfig(PlatformLoginSecurityConfigParam param) {
        PlatformLoginSecurityConfig data = this.getLoginSecurityConfig();
        PlatformSecurityConfigConvert.CONVERT.copy(param, data);
        systemConfigService.updateConfig(PlatformConfigTypeEnum.SECURITY_LOGIN, data);
    }

    /// 获取会话管理配置
    public PlatformSessionManagementConfig getSessionManagementConfig() {
        return systemConfigService.getOrCreateConfig(PlatformConfigTypeEnum.SECURITY_SESSION,
                PlatformSessionManagementConfig.class,
                defaultSessionConfig());
    }

    /// 会话管理配置默认值: 新系统开箱即用
    private PlatformSessionManagementConfig defaultSessionConfig() {
        return new PlatformSessionManagementConfig()
                .setEnabled(true)
                .setMaxOnlineHours(72)
                .setActiveTimeoutHours(24)
                .setMaxConcurrentSessions(5)
                .setConcurrentStrategy("KICK_OLDEST")
                .setConcurrentScope("GLOBAL");
    }

    /// 获取会话管理配置
    public PlatformSessionManagementConfigResult findSessionManagementConfig() {
        return PlatformSecurityConfigConvert.CONVERT.toSessionManagementResult(this.getSessionManagementConfig());
    }

    /// 更新会话管理配置
    public void updateSessionManagementConfig(PlatformSessionManagementConfigParam param) {
        PlatformSessionManagementConfig data = this.getSessionManagementConfig();
        PlatformSecurityConfigConvert.CONVERT.copy(param, data);
        systemConfigService.updateConfig(PlatformConfigTypeEnum.SECURITY_SESSION, data);
    }

    /// 获取双因素认证配置
    public PlatformTwoFactorAuthConfig getTwoFactorAuthConfig() {
        return systemConfigService.getOrCreateConfig(PlatformConfigTypeEnum.SECURITY_TWO_FACTOR_AUTH,
                PlatformTwoFactorAuthConfig.class,
                defaultTwoFactorAuthConfig());
    }

    /// 双因素认证配置默认值: 向后兼容档, 默认不启用(与配置缺失时行为一致), issuer/备用码数量与消费端兜底值对齐
    private PlatformTwoFactorAuthConfig defaultTwoFactorAuthConfig() {
        return new PlatformTwoFactorAuthConfig()
                .setEnabled(false)
                .setIssuer(PlatformTwoFactorAuthConfig.DEFAULT_ISSUER)
                .setBackupCodesCount(PlatformTwoFactorAuthConfig.DEFAULT_BACKUP_CODES_COUNT);
    }

    /// 获取双因素认证配置
    public PlatformTwoFactorAuthConfigResult findTwoFactorAuthConfig() {
        return PlatformSecurityConfigConvert.CONVERT.toTwoFactorAuthResult(this.getTwoFactorAuthConfig());
    }

    /// 更新双因素认证配置
    public void updateTwoFactorAuthConfig(PlatformTwoFactorAuthConfigParam param) {
        PlatformTwoFactorAuthConfig data = this.getTwoFactorAuthConfig();
        PlatformSecurityConfigConvert.CONVERT.copy(param, data);
        systemConfigService.updateConfig(PlatformConfigTypeEnum.SECURITY_TWO_FACTOR_AUTH, data);
    }

    /// 获取通行密钥(WebAuthn)配置
    public PlatformWebAuthnConfig getWebAuthnConfig() {
        return systemConfigService.getOrCreateConfig(PlatformConfigTypeEnum.SECURITY_WEBAUTHN,
                PlatformWebAuthnConfig.class,
                defaultWebAuthnConfig());
    }

    /// 通行密钥配置默认值: 默认不启用, rpId 待部署域名确定后配置
    private PlatformWebAuthnConfig defaultWebAuthnConfig() {
        return new PlatformWebAuthnConfig()
                .setEnabled(false)
                .setRpId("")
                .setRpName(PlatformWebAuthnConfig.DEFAULT_RP_NAME)
                .setOrigins(List.of());
    }

    /// 获取通行密钥配置(结果)
    public PlatformWebAuthnConfigResult findWebAuthnConfig() {
        return PlatformSecurityConfigConvert.CONVERT.toWebAuthnResult(this.getWebAuthnConfig());
    }

    /// 更新通行密钥配置
    public void updateWebAuthnConfig(PlatformWebAuthnConfigParam param) {
        PlatformWebAuthnConfig data = this.getWebAuthnConfig();
        PlatformSecurityConfigConvert.CONVERT.copy(param, data);
        systemConfigService.updateConfig(PlatformConfigTypeEnum.SECURITY_WEBAUTHN, data);
    }

    // ========== API安全配置（防重放，高频读取，走多级缓存） ==========

    /// 缓存配置 JSON（L2 安全类型: String；key 带 :json 避免命中旧版 POJO 缓存）
    @Cacheable(value = API_SECURITY_CACHE_NAME, key = "'current:json'")
    public String getApiSecurityConfigJson() {
        PlatformApiSecurityConfig config = systemConfigService.getOrCreateConfig(
                PlatformConfigTypeEnum.API_SECURITY,
                PlatformApiSecurityConfig.class,
                defaultApiSecurityConfig());
        if (config == null) {
            config = defaultApiSecurityConfig();
        }
        return JacksonUtil.toJson(config);
    }

    /// 获取API安全配置实体（从缓存JSON显式还原类型）
    public PlatformApiSecurityConfig getApiSecurityConfig() {
        // 走 self 代理以命中缓存
        String json = self.getApiSecurityConfigJson();
        if (StrUtil.isBlank(json)) {
            return defaultApiSecurityConfig();
        }
        PlatformApiSecurityConfig config = JacksonUtil.toBean(json, PlatformApiSecurityConfig.class);
        return config == null ? defaultApiSecurityConfig() : config;
    }

    /// API安全配置默认值: 默认不启用，避免影响存量商户
    private PlatformApiSecurityConfig defaultApiSecurityConfig() {
        return new PlatformApiSecurityConfig()
                .setNonceVerifyEnabled(false)
                .setReqTimeoutEnabled(false)
                .setReqTimeoutSeconds(300)
                .setNonceTtlSeconds(300);
    }

    /// 获取API安全配置(结果)
    public PlatformApiSecurityConfigResult findApiSecurityConfig() {
        return PlatformSecurityConfigConvert.CONVERT.toApiSecurityResult(self.getApiSecurityConfig());
    }

    /// 更新API安全配置
    @CacheEvict(value = API_SECURITY_CACHE_NAME, allEntries = true)
    public void updateApiSecurityConfig(PlatformApiSecurityConfigParam param) {
        PlatformApiSecurityConfig data = systemConfigService.getOrCreateConfig(
                PlatformConfigTypeEnum.API_SECURITY,
                PlatformApiSecurityConfig.class,
                defaultApiSecurityConfig());
        PlatformSecurityConfigConvert.CONVERT.copy(param, data);
        systemConfigService.updateConfig(PlatformConfigTypeEnum.API_SECURITY, data);
    }

    // ========== IAM域防重放配置（登录/注册/改密等敏感操作，走多级缓存） ==========

    /// IAM域防重放配置缓存名（L1 Caffeine + L2 Redis）
    public static final String IAM_REPLAY_PROTECT_CACHE_NAME = "system:iam-replay-protect-config";

    /// 缓存配置 JSON
    @Cacheable(value = IAM_REPLAY_PROTECT_CACHE_NAME, key = "'current:json'")
    public String getIamReplayProtectConfigJson() {
        PlatformIamReplayProtectConfig config = systemConfigService.getOrCreateConfig(
                PlatformConfigTypeEnum.IAM_REPLAY_PROTECT,
                PlatformIamReplayProtectConfig.class,
                defaultIamReplayProtectConfig());
        if (config == null) {
            config = defaultIamReplayProtectConfig();
        }
        return JacksonUtil.toJson(config);
    }

    /// 获取IAM域防重放配置实体（从缓存JSON显式还原类型）
    public PlatformIamReplayProtectConfig getIamReplayProtectConfig() {
        String json = self.getIamReplayProtectConfigJson();
        if (StrUtil.isBlank(json)) {
            return defaultIamReplayProtectConfig();
        }
        PlatformIamReplayProtectConfig config = JacksonUtil.toBean(json, PlatformIamReplayProtectConfig.class);
        return config == null ? defaultIamReplayProtectConfig() : config;
    }

    /// IAM域防重放配置默认值: 默认启用（登录接口已在使用），保持向后兼容
    private PlatformIamReplayProtectConfig defaultIamReplayProtectConfig() {
        return new PlatformIamReplayProtectConfig()
                .setEnabled(true)
                .setNonceTimeoutSeconds(300)
                .setTimestampToleranceSeconds(300);
    }

    /// 获取IAM域防重放配置(结果)
    public PlatformIamReplayProtectConfigResult findIamReplayProtectConfig() {
        return PlatformSecurityConfigConvert.CONVERT.toIamReplayProtectResult(self.getIamReplayProtectConfig());
    }

    /// 更新IAM域防重放配置
    @CacheEvict(value = IAM_REPLAY_PROTECT_CACHE_NAME, allEntries = true)
    public void updateIamReplayProtectConfig(PlatformIamReplayProtectConfigParam param) {
        PlatformIamReplayProtectConfig data = systemConfigService.getOrCreateConfig(
                PlatformConfigTypeEnum.IAM_REPLAY_PROTECT,
                PlatformIamReplayProtectConfig.class,
                defaultIamReplayProtectConfig());
        PlatformSecurityConfigConvert.CONVERT.copy(param, data);
        systemConfigService.updateConfig(PlatformConfigTypeEnum.IAM_REPLAY_PROTECT, data);
    }

    // ========== 支付安全配置（风控开关，支付主链路高频读取，走多级缓存） ==========

    /// 支付安全配置缓存名（L1 Caffeine + L2 Redis）
    public static final String PAY_SECURITY_CACHE_NAME = "system:pay-security-config";

    /// 缓存配置 JSON（L2 安全类型: String；key 带 :json 避免命中旧版 POJO 缓存）
    @Cacheable(value = PAY_SECURITY_CACHE_NAME, key = "'current:json'")
    public String getPaySecurityConfigJson() {
        PlatformPaySecurityConfig config = systemConfigService.getOrCreateConfig(
                PlatformConfigTypeEnum.PAY_SECURITY,
                PlatformPaySecurityConfig.class,
                defaultPaySecurityConfig());
        if (config == null) {
            config = defaultPaySecurityConfig();
        }
        return JacksonUtil.toJson(config);
    }

    /// 获取支付安全配置实体（从缓存 JSON 显式还原类型）
    public PlatformPaySecurityConfig getPaySecurityConfig() {
        // 走 self 代理以命中缓存
        String json = self.getPaySecurityConfigJson();
        if (StrUtil.isBlank(json)) {
            return defaultPaySecurityConfig();
        }
        PlatformPaySecurityConfig config = JacksonUtil.toBean(json, PlatformPaySecurityConfig.class);
        if (config == null) {
            return defaultPaySecurityConfig();
        }
        // 兼容旧版: 原省级/市级开关已合并为地区拦截, 旧字段任一开启则迁移为地区拦截开启
        // 注意: regionBlacklistEnabled 字段初始值为 FALSE(Jackson 反序列化缺字段时保留初始值而非 null),
        // 不可用 == null 判断, 否则迁移永不执行, 历史开启的地区拦截会静默失效(fail-open 安全降级)。
        // 旧字段 provinceBlacklistEnabled/cityBlacklistEnabled 在新版保存时已被清 null,
        // 用户若在新版显式关闭 region 后保存, legacyOn 必为 false, 不会误开启。
        boolean legacyOn = Boolean.TRUE.equals(config.getProvinceBlacklistEnabled())
                || Boolean.TRUE.equals(config.getCityBlacklistEnabled());
        if (legacyOn) {
            config.setRegionBlacklistEnabled(true);
        }
        return config;
    }

    /// 支付安全配置默认值: 默认开启风控、命中阻断、事后补录、增强拦截
    private PlatformPaySecurityConfig defaultPaySecurityConfig() {
        return new PlatformPaySecurityConfig()
                .setRiskEnabled(true)
                .setRiskBlockBeforePay(true)
                .setRiskCheckAfterPay(true)
                .setRiskOpenIdLevel(PayRiskOpenIdLevelEnum.ENHANCED.getCode())
                .setBlockOverseasIp(false)
                .setIpv6MatchEnabled(false)
                .setGeoFenceStrategy(GEO_FENCE_STRATEGY_BALANCED);
    }

    /// 获取支付安全配置(结果)
    public PlatformPaySecurityConfigResult findPaySecurityConfig() {
        return PlatformSecurityConfigConvert.CONVERT.toPaySecurityResult(self.getPaySecurityConfig());
    }

    /// 更新支付安全配置
    @CacheEvict(value = PAY_SECURITY_CACHE_NAME, allEntries = true)
    public void updatePaySecurityConfig(PlatformPaySecurityConfigParam param) {
        // 地理围栏策略兜底: 非法值回退 balanced（与 GeoFenceStrategyEnum 取值对齐, 此处不引 payment-core 避免反向依赖）
        if (!GEO_FENCE_STRATEGY_VALID.contains(param.getGeoFenceStrategy())) {
            param.setGeoFenceStrategy(GEO_FENCE_STRATEGY_BALANCED);
        }
        PlatformPaySecurityConfig data = systemConfigService.getOrCreateConfig(
                PlatformConfigTypeEnum.PAY_SECURITY,
                PlatformPaySecurityConfig.class,
                defaultPaySecurityConfig());
        PlatformSecurityConfigConvert.CONVERT.copy(param, data);
        // 清理旧版省级/市级开关字段（已合并进 regionBlacklistEnabled, jsonb 不再保留）
        data.setProvinceBlacklistEnabled(null);
        data.setCityBlacklistEnabled(null);
        systemConfigService.updateConfig(PlatformConfigTypeEnum.PAY_SECURITY, data);
    }
}
