package cn.daxpay.open.payment.admin.check.checker;

import cn.daxpay.open.payment.common.check.checker.AdminConfigChecker;
import cn.daxpay.open.payment.common.check.enums.ConfigCheckCategoryEnum;
import cn.daxpay.open.payment.common.check.model.ConfigCheckItem;
import cn.daxpay.open.platform.system.entity.config.platform.infra.PlatformUrlConfig;
import cn.daxpay.open.platform.system.enums.PlatformConfigTypeEnum;
import cn.daxpay.open.platform.system.service.config.SystemPlatformConfigService;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import java.util.Map;
import java.util.Objects;

/// # 平台访问地址检查器
///
/// 检测平台后端 API 地址、管理端地址与支付网关地址是否已配置。
/// 后端地址为支付回调拼接的必需项, 缺失会导致通道回调地址无法生成;
/// 支付网关地址缺失则收银台/支付页面跳转不可用。
/// 商户端地址不纳入检测(未启用商户端社交登录的部署会误报)。
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
        // 后端 API 地址与管理端地址是核心: 缺后端地址回调无法拼接, 缺管理端地址第三方登录回调失效;
        // 支付网关地址缺失则收银台/支付页面跳转不可用
        boolean unconfigured = Objects.isNull(config)
                || StrUtil.isBlank(config.getBackendBaseUrl())
                || StrUtil.isBlank(config.getAdminBaseUrl())
                || StrUtil.isBlank(config.getPaymentGatewayBaseUrl());
        if (unconfigured) {
            return ConfigCheckItem.of(
                    ConfigCheckCategoryEnum.PLATFORM_URL,
                    ConfigCheckCategoryEnum.PLATFORM_URL.getCode(),
                    "configCheck.platformUrl.title",
                    "configCheck.platformUrl.description",
                    "/system/config/platform",
                    Map.of("tab", "url")
            );
        }
        return null;
    }
}
