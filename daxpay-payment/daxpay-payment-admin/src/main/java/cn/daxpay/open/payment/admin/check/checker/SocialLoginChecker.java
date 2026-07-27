package cn.daxpay.open.payment.admin.check.checker;

import cn.daxpay.open.payment.common.check.checker.AdminConfigChecker;
import cn.daxpay.open.payment.common.check.enums.ConfigCheckCategoryEnum;
import cn.daxpay.open.payment.common.check.model.ConfigCheckItem;
import cn.daxpay.open.platform.iam.dao.social.SocialLoginConfigManager;
import cn.daxpay.open.platform.iam.entity.social.SocialLoginConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/// # 社交登录配置检查器
///
/// 检测社交登录平台中, 已启用但未完成参数配置的数量。
/// 仅统计 `enabled = true && configured = false` 的记录(避免对未启用平台误报)。
@Component
@Order(5)
@RequiredArgsConstructor
public class SocialLoginChecker implements AdminConfigChecker {

    private final SocialLoginConfigManager socialLoginConfigManager;

    @Override
    public ConfigCheckCategoryEnum getCategory() {
        return ConfigCheckCategoryEnum.SOCIAL_LOGIN;
    }

    @Override
    public ConfigCheckItem check() {
        // 统计"已启用但未完成配置"的社交平台数量
        long unconfiguredCount = socialLoginConfigManager.lambdaQuery()
                .eq(SocialLoginConfig::getEnabled, true)
                .eq(SocialLoginConfig::isConfigured, false)
                .count();
        if (unconfiguredCount > 0) {
            return ConfigCheckItem.of(
                    ConfigCheckCategoryEnum.SOCIAL_LOGIN,
                    ConfigCheckCategoryEnum.SOCIAL_LOGIN.getCode(),
                    "configCheck.socialLogin.title",
                    "configCheck.socialLogin.description",
                    "/system/config/third-platform"
            ).setCount((int) unconfiguredCount);
        }
        return null;
    }
}
