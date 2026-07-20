package cn.daxpay.open.payment.admin.check.checker;

import cn.daxpay.open.payment.check.checker.AdminConfigChecker;
import cn.daxpay.open.payment.check.enums.ConfigCheckCategoryEnum;
import cn.daxpay.open.payment.check.model.ConfigCheckItem;
import cn.daxpay.open.platform.system.entity.config.platform.infra.PlatformUrlConfig;
import cn.daxpay.open.platform.system.enums.PlatformConfigTypeEnum;
import cn.daxpay.open.platform.system.service.config.SystemPlatformConfigService;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/// # 平台访问地址检查器
///
/// 检测平台后端 API 地址与管理端地址是否已配置。
/// 后端地址为支付回调拼接的必需项, 缺失会导致通道回调地址无法生成。
@Component
@Order(3)
@RequiredArgsConstructor
public class PlatformUrlChecker implements AdminConfigChecker {

    private final SystemPlatformConfigService systemPlatformConfigService;

    @Override
    public ConfigCheckCategoryEnum getCategory() {
        return ConfigCheckCategoryEnum.PLATFORM_URL;
    }

    @Override
    public ConfigCheckItem check() {
        PlatformUrlConfig config = systemPlatformConfigService.getConfig(
                PlatformConfigTypeEnum.URL, PlatformUrlConfig.class);
        // 后端 API 地址与管理端地址是核心: 缺后端地址回调无法拼接, 缺管理端地址第三方登录回调失效
        boolean unconfigured = config == null
                || StrUtil.isBlank(config.getBackendBaseUrl())
                || StrUtil.isBlank(config.getAdminBaseUrl());
        if (unconfigured) {
            return ConfigCheckItem.of(
                    ConfigCheckCategoryEnum.PLATFORM_URL,
                    ConfigCheckCategoryEnum.PLATFORM_URL.getCode(),
                    "configCheck.platformUrl.title",
                    "configCheck.platformUrl.description",
                    "/system/config/platform"
            );
        }
        return null;
    }
}
