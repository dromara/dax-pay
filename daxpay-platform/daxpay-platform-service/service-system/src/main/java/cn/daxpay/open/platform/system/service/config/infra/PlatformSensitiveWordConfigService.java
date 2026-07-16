package cn.daxpay.open.platform.system.service.config.infra;

import cn.daxpay.open.platform.capability.sensitiveword.policy.SensitiveWordPolicy;
import cn.daxpay.open.platform.system.convert.config.infra.PlatformSensitiveWordConfigConvert;
import cn.daxpay.open.platform.system.entity.config.platform.infra.PlatformSensitiveWordConfig;
import cn.daxpay.open.platform.system.enums.PlatformConfigTypeEnum;
import cn.daxpay.open.platform.system.param.config.infra.PlatformSensitiveWordConfigParam;
import cn.daxpay.open.platform.system.result.config.infra.PlatformSensitiveWordConfigResult;
import cn.daxpay.open.platform.system.service.config.SystemPlatformConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

/// # 平台敏感词策略服务
///
/// 同时实现 [SensitiveWordPolicy]，供 capability 运行时读取.
@Slf4j
@Primary
@Service
public class PlatformSensitiveWordConfigService implements SensitiveWordPolicy {

    public static final String CACHE_NAME = "system:sensitive-word-config";

    private final SystemPlatformConfigService systemConfigService;

    /// 自注入，保证 [SensitiveWordPolicy] 读配置走 Spring 缓存代理
    private final PlatformSensitiveWordConfigService self;

    public PlatformSensitiveWordConfigService(
            SystemPlatformConfigService systemConfigService,
            @Lazy PlatformSensitiveWordConfigService self) {
        this.systemConfigService = systemConfigService;
        this.self = self;
    }

    /// 获取配置实体（带默认）
    @Cacheable(value = CACHE_NAME, key = "'current'")
    public PlatformSensitiveWordConfig getConfig() {
        PlatformSensitiveWordConfig config = systemConfigService.getOrCreateConfig(
                PlatformConfigTypeEnum.SENSITIVE_WORD,
                PlatformSensitiveWordConfig.class,
                new PlatformSensitiveWordConfig());
        return config == null ? new PlatformSensitiveWordConfig() : config;
    }

    /// 查询结果
    public PlatformSensitiveWordConfigResult findConfig() {
        return PlatformSensitiveWordConfigConvert.CONVERT.toResult(self.getConfig());
    }

    /// 更新配置
    @CacheEvict(value = CACHE_NAME, allEntries = true)
    public void updateConfig(PlatformSensitiveWordConfigParam param) {
        PlatformSensitiveWordConfig data = PlatformSensitiveWordConfigConvert.CONVERT.convert(param);
        if (data == null) {
            data = new PlatformSensitiveWordConfig();
        }
        // 空值回落到默认
        if (data.getEnabled() == null) {
            data.setEnabled(true);
        }
        if (data.getRevealWord() == null) {
            data.setRevealWord(false);
        }
        if (data.getRecordHit() == null) {
            data.setRecordHit(true);
        }
        if (data.getContentPreviewMaxLen() == null || data.getContentPreviewMaxLen() < 1) {
            data.setContentPreviewMaxLen(200);
        }
        systemConfigService.updateConfig(PlatformConfigTypeEnum.SENSITIVE_WORD, data);
    }

    @Override
    public boolean isEnabled() {
        Boolean v = self.getConfig().getEnabled();
        return v == null || v;
    }

    @Override
    public boolean isRevealWord() {
        Boolean v = self.getConfig().getRevealWord();
        return Boolean.TRUE.equals(v);
    }

    @Override
    public boolean isRecordHit() {
        Boolean v = self.getConfig().getRecordHit();
        return v == null || v;
    }

    @Override
    public int contentPreviewMaxLen() {
        Integer v = self.getConfig().getContentPreviewMaxLen();
        return v == null || v < 1 ? 200 : v;
    }
}
