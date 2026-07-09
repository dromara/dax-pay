package cn.daxpay.open.platform.system.service.config;

import cn.daxpay.open.platform.capability.alipay.auth.config.AlipayAuthConfig;
import cn.daxpay.open.platform.system.convert.PlatformAlipayAuthConfigConvert;
import cn.daxpay.open.platform.system.entity.config.platform.PlatformAlipayAuthConfig;
import cn.daxpay.open.platform.system.enums.EncryptPlatformConfigTypeEnum;
import cn.daxpay.open.platform.system.param.config.PlatformAlipayAuthConfigParam;
import cn.daxpay.open.platform.system.result.config.platform.PlatformAlipayAuthConfigResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 平台支付宝开放平台认证配置服务
///
/// 管理支付宝 OAuth 凭据(appId/私钥/证书), 通过 [SystemPlatformEncryptConfigService]
/// 以 AES-256-GCM 加密 JSON 存储。供 iam 模块(授权登录)与 payment 模块(通道认证)共用。
///
@Slf4j
@Service
@RequiredArgsConstructor
public class PlatformAlipayAuthConfigService {

    private final SystemPlatformEncryptConfigService encryptConfigService;

    /// 获取支付宝认证配置(原始, 不脱敏)
    public PlatformAlipayAuthConfig getAlipayAuthConfig() {
        return encryptConfigService.getOrCreateConfig(EncryptPlatformConfigTypeEnum.ALIPAY_AUTH,
                PlatformAlipayAuthConfig.class,
                new PlatformAlipayAuthConfig());
    }

    /// 获取支付宝认证配置(脱敏, 返回前端)
    public PlatformAlipayAuthConfigResult findAlipayAuthConfig() {
        return PlatformAlipayAuthConfigConvert.CONVERT.toResult(this.getAlipayAuthConfig());
    }

    /// 更新支付宝认证配置
    /// 平台级配置不再支持沙箱, 保存时强制 sandbox=false
    public void updateAlipayAuthConfig(PlatformAlipayAuthConfigParam param) {
        PlatformAlipayAuthConfig data = this.getAlipayAuthConfig();
        PlatformAlipayAuthConfigConvert.CONVERT.copy(param, data);
        // 平台级支付宝认证固定生产环境
        data.setSandbox(false);
        encryptConfigService.updateConfig(EncryptPlatformConfigTypeEnum.ALIPAY_AUTH, data);
    }

    /// 转换为 capability 层的 [AlipayAuthConfig](供 capability-alipay 调用使用)
    public AlipayAuthConfig toCapabilityConfig() {
        PlatformAlipayAuthConfig data = this.getAlipayAuthConfig();
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
