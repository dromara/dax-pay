package cn.daxpay.open.payment.admin.check.checker;

import cn.daxpay.open.payment.common.check.checker.AdminConfigChecker;
import cn.daxpay.open.payment.common.check.enums.ConfigCheckCategoryEnum;
import cn.daxpay.open.payment.common.check.model.ConfigCheckItem;
import cn.daxpay.open.platform.capability.social.justauth.SocialSourceEnum;
import cn.daxpay.open.platform.iam.dao.social.SocialLoginConfigManager;
import cn.daxpay.open.platform.iam.entity.social.SocialLoginConfig;
import cn.daxpay.open.platform.system.entity.config.platform.auth.PlatformWechatMpAuthConfig;
import cn.daxpay.open.platform.system.enums.EncryptPlatformConfigTypeEnum;
import cn.daxpay.open.platform.system.service.config.SystemPlatformEncryptConfigService;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;

/// # 平台微信公众号凭据检查器
///
/// 检测已启用微信公众号登录时的平台公众号凭据(appId/appSecret)是否已配置。
/// 公众号为平台级跳转型: 社交登录表只存启停占位, 凭据存平台级加密配置(WECHAT_MP_AUTH)。
/// 社交登录未启用微信公众号时不告警(降噪)。
@Component
@Order(9)
@RequiredArgsConstructor
public class PlatformWechatMpChecker implements AdminConfigChecker {

    private final SocialLoginConfigManager socialLoginConfigManager;
    private final SystemPlatformEncryptConfigService systemPlatformEncryptConfigService;

    @Override
    public ConfigCheckCategoryEnum getCategory() {
        return ConfigCheckCategoryEnum.PLATFORM_WECHAT_MP;
    }

    @Override
    public ConfigCheckItem check() {
        // 社交登录未启用微信公众号(无记录或 enabled=false)时不告警
        SocialLoginConfig socialConfig = socialLoginConfigManager
                .findBySource(SocialSourceEnum.WECHAT_MP.getCode())
                .orElse(null);
        if (Objects.isNull(socialConfig) || !Boolean.TRUE.equals(socialConfig.getEnabled())) {
            return null;
        }
        // 凭据只读获取自平台加密配置(无记录返回 null), appId/appSecret 任一为空 => 告警
        PlatformWechatMpAuthConfig config = systemPlatformEncryptConfigService.getConfig(
                EncryptPlatformConfigTypeEnum.WECHAT_MP_AUTH, PlatformWechatMpAuthConfig.class);
        boolean unconfigured = Objects.isNull(config)
                || StrUtil.isBlank(config.getAppId())
                || StrUtil.isBlank(config.getAppSecret());
        if (unconfigured) {
            return ConfigCheckItem.of(
                    ConfigCheckCategoryEnum.PLATFORM_WECHAT_MP,
                    ConfigCheckCategoryEnum.PLATFORM_WECHAT_MP.getCode(),
                    "configCheck.platformWechatMp.title",
                    "configCheck.platformWechatMp.description",
                    "/system/config/third-platform",
                    Map.of("tab", "wechatMp")
            );
        }
        return null;
    }
}
