package cn.daxpay.open.platform.system.service.config.infra;

import cn.daxpay.open.platform.system.convert.config.infra.PlatformWebsiteConfigConvert;
import cn.daxpay.open.platform.system.entity.config.platform.infra.PlatformWebsiteConfig;
import cn.daxpay.open.platform.system.enums.PlatformConfigTypeEnum;
import cn.daxpay.open.platform.system.param.config.infra.PlatformWebsiteConfigParam;
import cn.daxpay.open.platform.system.result.config.infra.PlatformWebsiteConfigResult;
import cn.daxpay.open.platform.system.service.config.SystemPlatformConfigService;
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

    /// 获取站点配置
    public PlatformWebsiteConfigResult findWebsiteConfig() {
        return PlatformWebsiteConfigConvert.CONVERT.toResult(this.getWebsiteConfig());
    }

    /// 更新站点配置(整包覆盖, 允许清空字段)
    public void updateWebsiteConfig(PlatformWebsiteConfigParam param) {
        PlatformWebsiteConfig data = PlatformWebsiteConfigConvert.CONVERT.convert(param);
        if (data == null) {
            data = new PlatformWebsiteConfig();
        }
        systemConfigService.updateConfig(PlatformConfigTypeEnum.WEBSITE, data);
    }
}
