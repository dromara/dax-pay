package cn.daxpay.open.platform.system.service.config.auth;

import cn.daxpay.open.platform.system.convert.config.auth.PlatformDouyinH5AuthConfigConvert;
import cn.daxpay.open.platform.system.entity.config.platform.auth.PlatformDouyinH5AuthConfig;
import cn.daxpay.open.platform.system.enums.EncryptPlatformConfigTypeEnum;
import cn.daxpay.open.platform.system.param.config.auth.PlatformDouyinH5AuthConfigParam;
import cn.daxpay.open.platform.system.result.config.auth.PlatformDouyinH5AuthConfigResult;
import cn.daxpay.open.platform.system.service.config.SystemPlatformEncryptConfigService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 平台抖音开放平台 H5 应用认证配置服务
///
/// 管理抖音开放平台 H5 应用凭据(clientKey/clientSecret), 通过 [SystemPlatformEncryptConfigService]
/// 以 AES-256-GCM 加密 JSON 存储。
///
/// 本配置独立于「三方平台登录配置」中的抖音 OAuth 登录凭据。
///
@Slf4j
@Service
@RequiredArgsConstructor
public class PlatformDouyinH5AuthConfigService {

    private final SystemPlatformEncryptConfigService encryptConfigService;

    /// 获取抖音 H5 应用认证配置(原始, 不脱敏)
    public PlatformDouyinH5AuthConfig getDouyinH5AuthConfig() {
        return encryptConfigService.getOrCreateConfig(EncryptPlatformConfigTypeEnum.DOUYIN_H5_AUTH,
                PlatformDouyinH5AuthConfig.class,
                new PlatformDouyinH5AuthConfig());
    }

    /// 获取抖音 H5 应用认证配置(脱敏, 返回前端)
    public PlatformDouyinH5AuthConfigResult findDouyinH5AuthConfig() {
        return PlatformDouyinH5AuthConfigConvert.CONVERT.toResult(this.getDouyinH5AuthConfig());
    }

    /// 更新抖音 H5 应用认证配置
    public void updateDouyinH5AuthConfig(PlatformDouyinH5AuthConfigParam param) {
        PlatformDouyinH5AuthConfig data = this.getDouyinH5AuthConfig();
        PlatformDouyinH5AuthConfigConvert.CONVERT.copy(param, data);
        encryptConfigService.updateConfig(EncryptPlatformConfigTypeEnum.DOUYIN_H5_AUTH, data);
    }
}
