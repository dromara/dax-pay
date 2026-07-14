package cn.daxpay.open.platform.system.service.config.infra;

import cn.daxpay.open.platform.common.json.util.JacksonUtil;
import cn.daxpay.open.platform.system.convert.config.infra.PlatformWebsiteConfigConvert;
import cn.daxpay.open.platform.system.entity.config.platform.SystemPlatformConfig;
import cn.daxpay.open.platform.system.entity.config.platform.infra.PlatformWebsiteConfig;
import cn.daxpay.open.platform.system.enums.PlatformConfigTypeEnum;
import cn.daxpay.open.platform.system.param.config.infra.PlatformWebsiteConfigParam;
import cn.daxpay.open.platform.system.result.config.infra.PlatformWebsiteConfigResult;
import cn.daxpay.open.platform.system.service.config.SystemPlatformConfigService;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 平台站点配置服务
///
/// 管理系统名称、Logo、备案与版权等展示配置, 存于 `system_platform_config`.
@Slf4j
@Service
@RequiredArgsConstructor
public class PlatformWebsiteConfigService {

    private final SystemPlatformConfigService systemConfigService;

    /// 获取站点配置
    public PlatformWebsiteConfig getWebsiteConfig() {
        return systemConfigService.getOrCreateConfig(PlatformConfigTypeEnum.WEBSITE,
                PlatformWebsiteConfig.class,
                new PlatformWebsiteConfig());
    }

    /// 获取站点配置(含 contentHash, 供客户端缓存比对)
    public PlatformWebsiteConfigResult findWebsiteConfig() {
        // 确保配置行存在
        PlatformWebsiteConfig config = this.getWebsiteConfig();
        PlatformWebsiteConfigResult result = PlatformWebsiteConfigConvert.CONVERT.toResult(config);
        if (result == null) {
            result = new PlatformWebsiteConfigResult();
        }
        result.setContentHash(this.computeContentHash());
        return result;
    }

    /// 更新站点配置(整包覆盖, 允许清空字段)
    public void updateWebsiteConfig(PlatformWebsiteConfigParam param) {
        PlatformWebsiteConfig data = PlatformWebsiteConfigConvert.CONVERT.convert(param);
        if (data == null) {
            data = new PlatformWebsiteConfig();
        }
        systemConfigService.updateConfig(PlatformConfigTypeEnum.WEBSITE, data);
    }

    /// 对入库 configData 原文做 MD5, 写什么 hash 什么; 空配置用空对象 JSON
    private String computeContentHash() {
        SystemPlatformConfig entity = systemConfigService.getConfigEntity(PlatformConfigTypeEnum.WEBSITE);
        String raw = entity != null ? entity.getConfigData() : null;
        if (StrUtil.isBlank(raw)) {
            raw = JacksonUtil.toJson(new PlatformWebsiteConfig());
        }
        return DigestUtil.md5Hex(raw);
    }
}
