package cn.daxpay.open.payment.admin.check.checker;

import cn.daxpay.open.payment.common.check.checker.AdminConfigChecker;
import cn.daxpay.open.payment.common.check.enums.ConfigCheckCategoryEnum;
import cn.daxpay.open.payment.common.check.model.ConfigCheckItem;
import cn.daxpay.open.platform.system.enums.EncryptPlatformConfigTypeEnum;
import cn.daxpay.open.platform.system.service.config.SystemPlatformEncryptConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/// # 对象存储配置检查器
///
/// 检测 OSS 配置是否存在。OSS 未配置时文件上传/Logo 存储等能力不可用。
@Component
@Order(4)
@RequiredArgsConstructor
public class PlatformOssChecker implements AdminConfigChecker {

    private final SystemPlatformEncryptConfigService systemPlatformEncryptConfigService;

    @Override
    public ConfigCheckCategoryEnum getCategory() {
        return ConfigCheckCategoryEnum.PLATFORM_OSS;
    }

    @Override
    public ConfigCheckItem check() {
        // OSS 配置记录不存在 => 告警
        boolean unconfigured = !systemPlatformEncryptConfigService.existsConfig(
                EncryptPlatformConfigTypeEnum.OSS);
        if (unconfigured) {
            return ConfigCheckItem.of(
                    ConfigCheckCategoryEnum.PLATFORM_OSS,
                    ConfigCheckCategoryEnum.PLATFORM_OSS.getCode(),
                    "configCheck.platformOss.title",
                    "configCheck.platformOss.description",
                    "/system/config/platform"
            );
        }
        return null;
    }
}
