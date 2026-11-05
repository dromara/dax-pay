package cn.daxpay.open.payment.admin.check.checker;

import cn.daxpay.open.payment.common.check.checker.AdminConfigChecker;
import cn.daxpay.open.payment.common.check.enums.ConfigCheckCategoryEnum;
import cn.daxpay.open.payment.common.check.model.ConfigCheckItem;
import cn.daxpay.open.platform.capability.alipay.auth.config.AlipayAuthConfig;
import cn.daxpay.open.platform.capability.alipay.auth.service.AlipayAuthCapability;
import cn.daxpay.open.platform.system.entity.config.platform.auth.PlatformAlipayAuthConfig;
import cn.daxpay.open.platform.system.enums.EncryptPlatformConfigTypeEnum;
import cn.daxpay.open.platform.system.service.config.SystemPlatformEncryptConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;

/// # 平台支付宝应用凭据检查器
///
/// 检测平台支付宝开放平台应用凭据是否配置完整(appId/私钥必填, 公钥或证书按鉴权方式二选一)。
/// 支付宝是主打通道, 配置缺失即告警(无条件告警, 不做降噪)。
/// 完整性判定复用 [AlipayAuthCapability#isConfigured], 与真实授权链路校验口径一致。
@Component
@Order(8)
@RequiredArgsConstructor
public class PlatformAlipayAuthChecker implements AdminConfigChecker {

    private final SystemPlatformEncryptConfigService systemPlatformEncryptConfigService;
    private final AlipayAuthCapability alipayAuthCapability;

    @Override
    public ConfigCheckCategoryEnum getCategory() {
        return ConfigCheckCategoryEnum.PLATFORM_ALIPAY_AUTH;
    }

    @Override
    public ConfigCheckItem check() {
        // 只读获取(无记录返回 null), 不触发懒创建写库
        PlatformAlipayAuthConfig data = systemPlatformEncryptConfigService.getConfig(
                EncryptPlatformConfigTypeEnum.ALIPAY_AUTH, PlatformAlipayAuthConfig.class);
        boolean unconfigured = Objects.isNull(data)
                || !alipayAuthCapability.isConfigured(this.toCapabilityConfig(data));
        if (unconfigured) {
            return ConfigCheckItem.of(
                    ConfigCheckCategoryEnum.PLATFORM_ALIPAY_AUTH,
                    ConfigCheckCategoryEnum.PLATFORM_ALIPAY_AUTH.getCode(),
                    "configCheck.platformAlipayAuth.title",
                    "configCheck.platformAlipayAuth.description",
                    "/system/config/third-platform",
                    Map.of("tab", "alipay")
            );
        }
        return null;
    }

    /// 转换为 capability 层配置 POJO(字段一一对应拷贝, 只读不落库)
    private AlipayAuthConfig toCapabilityConfig(PlatformAlipayAuthConfig data) {
        return new AlipayAuthConfig()
                .setAppId(data.getAppId())
                .setAuthType(data.getAuthType())
                .setPrivateKey(data.getPrivateKey())
                .setAlipayPublicKey(data.getAlipayPublicKey())
                .setAppCert(data.getAppCert())
                .setAlipayCert(data.getAlipayCert())
                .setAlipayRootCert(data.getAlipayRootCert());
    }
}
