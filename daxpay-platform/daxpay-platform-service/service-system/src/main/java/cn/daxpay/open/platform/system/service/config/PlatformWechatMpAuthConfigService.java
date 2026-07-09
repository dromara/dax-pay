package cn.daxpay.open.platform.system.service.config;

import cn.daxpay.open.platform.system.convert.PlatformWechatMpAuthConfigConvert;
import cn.daxpay.open.platform.system.entity.config.platform.PlatformWechatMpAuthConfig;
import cn.daxpay.open.platform.system.enums.EncryptPlatformConfigTypeEnum;
import cn.daxpay.open.platform.system.param.config.PlatformWechatMpAuthConfigParam;
import cn.daxpay.open.platform.system.result.config.platform.PlatformWechatMpAuthConfigResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 平台微信公众号 H5 认证配置服务
///
/// 管理微信公众号网页授权凭据(appId/appSecret), 通过 [SystemPlatformEncryptConfigService]
/// 以 AES-256-GCM 加密 JSON 存储。供 iam 模块(授权登录)使用。
///
/// 注: 微信公众号能力在 capability-wechat 模块的 `WechatMpAuthService` 中已实现(参数化签名,
/// 直接传 appId/appSecret), 故本服务不需要转换为 capability 层 POJO, 调用方直接取 appId/appSecret 即可。
///
@Slf4j
@Service
@RequiredArgsConstructor
public class PlatformWechatMpAuthConfigService {

    private final SystemPlatformEncryptConfigService encryptConfigService;

    /// 获取微信公众号认证配置(原始, 不脱敏)
    public PlatformWechatMpAuthConfig getWechatMpAuthConfig() {
        return encryptConfigService.getOrCreateConfig(EncryptPlatformConfigTypeEnum.WECHAT_MP_AUTH,
                PlatformWechatMpAuthConfig.class,
                new PlatformWechatMpAuthConfig());
    }

    /// 获取微信公众号认证配置(脱敏, 返回前端)
    public PlatformWechatMpAuthConfigResult findWechatMpAuthConfig() {
        return PlatformWechatMpAuthConfigConvert.CONVERT.toResult(this.getWechatMpAuthConfig());
    }

    /// 更新微信公众号认证配置
    public void updateWechatMpAuthConfig(PlatformWechatMpAuthConfigParam param) {
        PlatformWechatMpAuthConfig data = this.getWechatMpAuthConfig();
        PlatformWechatMpAuthConfigConvert.CONVERT.copy(param, data);
        encryptConfigService.updateConfig(EncryptPlatformConfigTypeEnum.WECHAT_MP_AUTH, data);
    }
}
