package cn.daxpay.open.payment.admin.check.checker;

import cn.daxpay.open.payment.common.check.checker.AdminConfigChecker;
import cn.daxpay.open.payment.common.check.enums.ConfigCheckCategoryEnum;
import cn.daxpay.open.payment.common.check.model.ConfigCheckItem;
import cn.daxpay.open.platform.system.entity.config.platform.infra.PlatformMailConfig;
import cn.daxpay.open.platform.system.enums.EncryptPlatformConfigTypeEnum;
import cn.daxpay.open.platform.system.service.config.SystemPlatformEncryptConfigService;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;

/// # 平台邮件服务配置检查器
///
/// 检测已启用的邮件通道是否完成发件配置(服务器地址/账号/授权码)。
/// 配置不存在或通道未启用(enabled=false)不告警——主动关闭邮件通道是合法终态, 避免误报。
@Component
@Order(7)
@RequiredArgsConstructor
public class PlatformMailChecker implements AdminConfigChecker {

    private final SystemPlatformEncryptConfigService systemPlatformEncryptConfigService;

    @Override
    public ConfigCheckCategoryEnum getCategory() {
        return ConfigCheckCategoryEnum.PLATFORM_MAIL;
    }

    @Override
    public ConfigCheckItem check() {
        // 只读获取(无记录返回 null), 不触发懒创建写库
        PlatformMailConfig config = systemPlatformEncryptConfigService.getConfig(
                EncryptPlatformConfigTypeEnum.MAIL, PlatformMailConfig.class);
        // 通道未启用(含配置不存在)不告警
        if (Objects.isNull(config) || !config.getEnabled()) {
            return null;
        }
        // 已启用但发件三要素任一为空 => 告警
        boolean unconfigured = StrUtil.isBlank(config.getHost())
                || StrUtil.isBlank(config.getUsername())
                || StrUtil.isBlank(config.getPassword());
        if (unconfigured) {
            return ConfigCheckItem.of(
                    ConfigCheckCategoryEnum.PLATFORM_MAIL,
                    ConfigCheckCategoryEnum.PLATFORM_MAIL.getCode(),
                    "configCheck.platformMail.title",
                    "configCheck.platformMail.description",
                    "/system/config/platform",
                    Map.of("tab", "mail")
            );
        }
        return null;
    }
}
