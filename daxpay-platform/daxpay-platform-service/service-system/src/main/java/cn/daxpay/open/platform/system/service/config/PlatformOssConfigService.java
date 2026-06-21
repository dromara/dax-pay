package cn.daxpay.open.platform.system.service.config;

import cn.daxpay.open.platform.system.convert.PlatformOssConfigConvert;
import cn.daxpay.open.platform.system.entity.config.platform.PlatformOssConfig;
import cn.daxpay.open.platform.system.enums.EncryptPlatformConfigTypeEnum;
import cn.daxpay.open.platform.system.param.config.PlatformOssConfigParam;
import cn.daxpay.open.platform.system.result.config.platform.PlatformOssConfigResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 平台OSS配置服务
///
/// 管理对象存储配置，数据通过加密配置服务进行加密存储
@Slf4j
@Service
@RequiredArgsConstructor
public class PlatformOssConfigService {

    private final SystemPlatformEncryptConfigService encryptConfigService;

    /// 获取OSS配置
    public PlatformOssConfig getOssConfig() {
        return encryptConfigService.getOrCreateConfig(EncryptPlatformConfigTypeEnum.OSS,
                PlatformOssConfig.class,
                new PlatformOssConfig());
    }

    /// 获取OSS配置
    public PlatformOssConfigResult findOssConfig() {
        return PlatformOssConfigConvert.CONVERT.toOssResult(this.getOssConfig());
    }

    /// 更新OSS配置
    public void updateOssConfig(PlatformOssConfigParam param) {
        PlatformOssConfig data = this.getOssConfig();
        PlatformOssConfigConvert.CONVERT.copy(param, data);
        encryptConfigService.updateConfig(EncryptPlatformConfigTypeEnum.OSS, data);
    }
}
