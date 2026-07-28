package cn.daxpay.open.platform.system.service.mobile;

import cn.daxpay.open.platform.capability.alipay.auth.config.AlipayAuthTypeEnum;
import cn.daxpay.open.platform.common.json.util.JacksonUtil;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.daxpay.open.platform.system.convert.mobile.MobileAppConvert;
import cn.daxpay.open.platform.system.dao.mobile.MobileAppManager;
import cn.daxpay.open.platform.system.entity.mobile.MobileApp;
import cn.daxpay.open.platform.system.enums.MobileAppTypeEnum;
import cn.daxpay.open.platform.system.enums.MobilePlatformEnum;
import cn.daxpay.open.platform.system.mobile.config.AlipayMiniAppConfig;
import cn.daxpay.open.platform.system.mobile.config.DyMiniAppConfig;
import cn.daxpay.open.platform.system.mobile.config.WxMiniAppConfig;
import cn.daxpay.open.platform.system.mobile.config.convert.MobileAppConfigConvert;
import cn.daxpay.open.platform.system.mobile.config.param.AlipayMiniAppConfigParam;
import cn.daxpay.open.platform.system.mobile.config.param.DyMiniAppConfigParam;
import cn.daxpay.open.platform.system.mobile.config.param.WxMiniAppConfigParam;
import cn.daxpay.open.platform.system.param.mobile.MobileAppParam;
import cn.daxpay.open.platform.system.result.mobile.MobileAppResult;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/// # 移动端应用配置服务
///
/// 平台级配置, 按端类型(appType)+移动平台(platform)维度管理, 每组合最多一条(唯一键)。
/// - app_config: 强类型 Config 序列化后经 [DataEncryptTypeHandler] 加密入库
/// - API 层: wxMini / alipayMini / dyMini 嵌套对象; notify_config 仍为明文 jsonb 字符串
///
/// ## 端类型与平台白名单
/// - merchant: wx_h5 / wx_mini / alipay_mini / dy_mini
/// - admin: wx_mini / alipay_mini / dy_mini
/// - cashier: wx_mini / alipay_mini(抖音本期未开放)
@Slf4j
@Service
@RequiredArgsConstructor
public class MobileAppService {

    /// 各端类型允许配置的移动平台(本期开放范围)
    private static final Map<MobileAppTypeEnum, Set<MobilePlatformEnum>> ALLOWED_PLATFORMS = Map.of(
            MobileAppTypeEnum.MERCHANT, Set.of(
                    MobilePlatformEnum.WX_H5,
                    MobilePlatformEnum.WX_MINI,
                    MobilePlatformEnum.ALIPAY_MINI,
                    MobilePlatformEnum.DY_MINI),
            MobileAppTypeEnum.ADMIN, Set.of(
                    MobilePlatformEnum.WX_MINI,
                    MobilePlatformEnum.ALIPAY_MINI,
                    MobilePlatformEnum.DY_MINI),
            MobileAppTypeEnum.CASHIER, Set.of(
                    MobilePlatformEnum.WX_MINI,
                    MobilePlatformEnum.ALIPAY_MINI));

    private final MobileAppManager manager;

    /// 查询全部(前端按端类型分组展示卡片)
    public List<MobileAppResult> findAll() {
        return manager.findAll().stream()
                .map(this::toMaskedResult)
                .toList();
    }

    /// 按端类型查询所有平台配置(端详情页 Tab 列表)
    public List<MobileAppResult> findAllByAppType(String appType) {
        MobileAppTypeEnum.findByCode(appType);
        return manager.findAllByAppType(appType).stream()
                .map(this::toMaskedResult)
                .toList();
    }

    /// 查询单条(运营端, 脱敏)
    public MobileAppResult findById(Long id) {
        MobileApp entity = manager.findById(id)
                .orElseThrow(() -> new DataNotExistException("error.mobile_app.notExist"));
        return toMaskedResult(entity);
    }

    /// 按端类型+平台查询(运营端, 脱敏; 不存在返回 empty)
    public Optional<MobileAppResult> findByAppTypeAndPlatform(String appType, String platform) {
        MobileAppTypeEnum.findByCode(appType);
        MobilePlatformEnum.findByCode(platform);
        return manager.findByAppTypeAndPlatform(appType, platform)
                .map(this::toMaskedResult);
    }

    /// 保存(按端类型+平台组合 upsert)
    @Transactional(rollbackFor = Exception.class)
    public MobileAppResult save(MobileAppParam param) {
        MobilePlatformEnum platform = validateSaveParam(param);
        String appConfigJson = buildAppConfigJson(param, platform,
                manager.findByAppTypeAndPlatform(param.getAppType(), param.getPlatform())
                        .map(MobileApp::getAppConfig)
                        .orElse(null));

        var existing = manager.findByAppTypeAndPlatform(param.getAppType(), param.getPlatform());
        if (existing.isPresent()) {
            var entity = existing.get();
            MobileAppConvert.CONVERT.copy(param, entity);
            entity.setAppConfig(appConfigJson);
            manager.updateById(entity);
            return toMaskedResult(entity);
        }

        var entity = MobileAppConvert.CONVERT.toEntity(param);
        entity.setAppConfig(appConfigJson);
        if (entity.getEnabled() == null) {
            entity.setEnabled(true);
        }
        if (entity.getBindingEnabled() == null) {
            entity.setBindingEnabled(false);
        }
        manager.save(entity);
        return toMaskedResult(entity);
    }

    /// 更新启用状态
    @Transactional(rollbackFor = Exception.class)
    public void updateEnabled(Long id, Boolean enabled) {
        var entity = manager.findById(id)
                .orElseThrow(() -> new DataNotExistException("error.mobile_app.notExist"));
        entity.setEnabled(enabled);
        manager.updateById(entity);
    }

    /// 保存前校验白名单 + platform 与嵌套对象匹配
    private MobilePlatformEnum validateSaveParam(MobileAppParam param) {
        MobileAppTypeEnum appType = MobileAppTypeEnum.findByCode(param.getAppType());
        MobilePlatformEnum platform = MobilePlatformEnum.findByCode(param.getPlatform());

        Set<MobilePlatformEnum> allowed = ALLOWED_PLATFORMS.get(appType);
        if (allowed == null || !allowed.contains(platform)) {
            // 移动应用: 不支持的平台
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "error.mobile_app.platformNotAllowed",
                    appType.getCode(), platform.getCode());
        }

        // 统计传入的嵌套配置数量, 必须恰好 1 个且与 platform 对应
        int nestedCount = 0;
        if (param.getWxMini() != null) {
            nestedCount++;
        }
        if (param.getAlipayMini() != null) {
            nestedCount++;
        }
        if (param.getDyMini() != null) {
            nestedCount++;
        }
        if (nestedCount != 1) {
            // 移动应用: 配置解析异常
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "error.mobile_app.configRequired", platform.getCode());
        }

        boolean match = switch (platform) {
            case WX_MINI -> param.getWxMini() != null;
            case ALIPAY_MINI -> param.getAlipayMini() != null;
            case DY_MINI -> param.getDyMini() != null;
            default -> false;
        };
        if (!match) {
            // 移动应用: 配置格式不匹配
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "error.mobile_app.configPlatformMismatch", platform.getCode());
        }
        return platform;
    }

    /// 按平台合并并序列化 app_config JSON
    private String buildAppConfigJson(MobileAppParam param, MobilePlatformEnum platform, String oldJson) {
        return switch (platform) {
            case WX_MINI -> {
                WxMiniAppConfig merged = mergeWx(oldJson, param.getWxMini());
                validateWxForCreate(merged, oldJson);
                yield JacksonUtil.toJson(merged);
            }
            case ALIPAY_MINI -> {
                AlipayMiniAppConfig merged = mergeAlipay(oldJson, param.getAlipayMini());
                validateAlipayForCreate(merged, oldJson);
                yield JacksonUtil.toJson(merged);
            }
            case DY_MINI -> {
                DyMiniAppConfig merged = mergeDy(oldJson, param.getDyMini());
                validateDyForCreate(merged, oldJson);
                yield JacksonUtil.toJson(merged);
            }
            default -> throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "error.mobile_app.configPlatformMismatch", platform.getCode());
        };
    }

    private WxMiniAppConfig mergeWx(String oldJson, WxMiniAppConfigParam param) {
        WxMiniAppConfig config = parseConfigOrNew(oldJson, WxMiniAppConfig.class);
        // appId 必填, 总是覆盖
        config.setAppId(param.getAppId());
        if (StrUtil.isNotBlank(param.getAppSecret())) {
            config.setAppSecret(param.getAppSecret());
        }
        // originalId 允许传空串清空
        if (param.getOriginalId() != null) {
            config.setOriginalId(param.getOriginalId());
        }
        return config;
    }

    private AlipayMiniAppConfig mergeAlipay(String oldJson, AlipayMiniAppConfigParam param) {
        AlipayMiniAppConfig config = parseConfigOrNew(oldJson, AlipayMiniAppConfig.class);
        config.setAppId(param.getAppId());
        config.setAuthType(param.getAuthType());
        if (StrUtil.isNotBlank(param.getPrivateKey())) {
            config.setPrivateKey(param.getPrivateKey());
        }
        if (StrUtil.isNotBlank(param.getAlipayPublicKey())) {
            config.setAlipayPublicKey(param.getAlipayPublicKey());
        }
        if (StrUtil.isNotBlank(param.getAppCert())) {
            config.setAppCert(param.getAppCert());
        }
        if (StrUtil.isNotBlank(param.getAlipayCert())) {
            config.setAlipayCert(param.getAlipayCert());
        }
        if (StrUtil.isNotBlank(param.getAlipayRootCert())) {
            config.setAlipayRootCert(param.getAlipayRootCert());
        }
        return config;
    }

    private DyMiniAppConfig mergeDy(String oldJson, DyMiniAppConfigParam param) {
        DyMiniAppConfig config = parseConfigOrNew(oldJson, DyMiniAppConfig.class);
        config.setAppId(param.getAppId());
        if (StrUtil.isNotBlank(param.getAppSecret())) {
            config.setAppSecret(param.getAppSecret());
        }
        return config;
    }

    /// 新建时强制敏感材料齐全
    private void validateWxForCreate(WxMiniAppConfig config, String oldJson) {
        if (StrUtil.isNotBlank(oldJson)) {
            return;
        }
        if (StrUtil.isBlank(config.getAppSecret())) {
            // 移动应用: AppSecret不能为空
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "validation.field.appSecret.notBlank");
        }
    }

    private void validateDyForCreate(DyMiniAppConfig config, String oldJson) {
        if (StrUtil.isNotBlank(oldJson)) {
            return;
        }
        if (StrUtil.isBlank(config.getAppSecret())) {
            // 移动应用: AppSecret不能为空
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "validation.field.appSecret.notBlank");
        }
    }

    private void validateAlipayForCreate(AlipayMiniAppConfig config, String oldJson) {
        if (StrUtil.isNotBlank(oldJson)) {
            return;
        }
        if (StrUtil.isBlank(config.getPrivateKey())) {
            // 移动应用: 私钥不能为空
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "validation.field.privateKey.notBlank");
        }
        AlipayAuthTypeEnum authType = AlipayAuthTypeEnum.fromCode(config.getAuthType());
        if (authType.isCert()) {
            if (StrUtil.isBlank(config.getAppCert())
                    || StrUtil.isBlank(config.getAlipayCert())
                    || StrUtil.isBlank(config.getAlipayRootCert())) {
                // 移动应用: 支付宝根证书不能为空
                throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                        "error.mobile_app.alipayCertRequired");
            }
        } else if (StrUtil.isBlank(config.getAlipayPublicKey())) {
            // 移动应用: 支付宝公钥不能为空
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "validation.field.alipayPublicKey.notBlank");
        }
    }

    /// 实体 → 脱敏结果(填充对应嵌套配置)
    private MobileAppResult toMaskedResult(MobileApp entity) {
        MobileAppResult result = MobileAppConvert.CONVERT.toResult(entity);
        String json = entity.getAppConfig();
        if (StrUtil.isBlank(json) || StrUtil.isBlank(entity.getPlatform())) {
            return result;
        }
        MobilePlatformEnum platform;
        try {
            platform = MobilePlatformEnum.findByCode(entity.getPlatform());
        } catch (Exception e) {
            return result;
        }
        try {
            switch (platform) {
                case WX_MINI -> result.setWxMini(
                        MobileAppConfigConvert.CONVERT.toResult(parseConfig(json, WxMiniAppConfig.class)));
                case ALIPAY_MINI -> result.setAlipayMini(
                        MobileAppConfigConvert.CONVERT.toResult(parseConfig(json, AlipayMiniAppConfig.class)));
                case DY_MINI -> result.setDyMini(
                        MobileAppConfigConvert.CONVERT.toResult(parseConfig(json, DyMiniAppConfig.class)));
                default -> {
                }
            }
        } catch (Exception e) {
            log.warn("parse appConfig for result failed, platform={}: {}", entity.getPlatform(), e.getMessage());
        }
        return result;
    }

    private <T> T parseConfig(String json, Class<T> type) {
        if (StrUtil.isBlank(json)) {
            // 移动应用: 移动应用配置不存在
            throw new DataNotExistException("error.mobile_app.notExist");
        }
        T config = JacksonUtil.toBean(json, type);
        if (config == null) {
            // 移动应用: 移动应用配置不存在
            throw new DataNotExistException("error.mobile_app.notExist");
        }
        return config;
    }

    private <T> T parseConfigOrNew(String json, Class<T> type) {
        if (StrUtil.isBlank(json)) {
            try {
                return type.getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                throw new IllegalStateException("create config failed: " + type.getName(), e);
            }
        }
        T config = JacksonUtil.toBean(json, type);
        if (config != null) {
            return config;
        }
        try {
            return type.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new IllegalStateException("create config failed: " + type.getName(), e);
        }
    }
}
