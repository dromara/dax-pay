package cn.daxpay.open.payment.admin.check.checker;

import cn.daxpay.open.payment.check.checker.AdminConfigChecker;
import cn.daxpay.open.payment.check.enums.ConfigCheckCategoryEnum;
import cn.daxpay.open.payment.check.model.ConfigCheckItem;
import cn.daxpay.open.platform.system.entity.config.platform.infra.PlatformWebsiteConfig;
import cn.daxpay.open.platform.system.enums.PlatformConfigTypeEnum;
import cn.daxpay.open.platform.system.service.config.SystemPlatformConfigService;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/// # 平台站点信息检查器
///
/// 检测站点关键展示字段(系统名称/Logo/ICP 备案)是否已配置。
/// 任一关键字段为空视为未配置。
@Component
@Order(2)
@RequiredArgsConstructor
public class PlatformWebsiteChecker implements AdminConfigChecker {

    private final SystemPlatformConfigService systemPlatformConfigService;

    @Override
    public ConfigCheckCategoryEnum getCategory() {
        return ConfigCheckCategoryEnum.PLATFORM_WEBSITE;
    }

    @Override
    public ConfigCheckItem check() {
        PlatformWebsiteConfig config = systemPlatformConfigService.getConfig(
                PlatformConfigTypeEnum.WEBSITE, PlatformWebsiteConfig.class);
        // 关键字段任一为空 => 告警(系统名称与 Logo 是站点基础展示, ICP 为合规要求)
        boolean unconfigured = config == null
                || StrUtil.isBlank(config.getSystemName())
                || StrUtil.isBlank(config.getLogo())
                || StrUtil.isBlank(config.getIcpInfo());
        if (unconfigured) {
            return ConfigCheckItem.of(
                    ConfigCheckCategoryEnum.PLATFORM_WEBSITE,
                    ConfigCheckCategoryEnum.PLATFORM_WEBSITE.getCode(),
                    "configCheck.platformWebsite.title",
                    "configCheck.platformWebsite.description",
                    "/system/config/platform"
            );
        }
        return null;
    }
}
