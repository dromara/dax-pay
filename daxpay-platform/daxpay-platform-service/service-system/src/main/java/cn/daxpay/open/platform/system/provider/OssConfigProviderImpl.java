package cn.daxpay.open.platform.system.provider;

import cn.daxpay.open.platform.capability.file.entity.FileStorageConfig;
import cn.daxpay.open.platform.capability.file.provider.OssConfigProvider;
import cn.daxpay.open.platform.system.entity.config.platform.PlatformOssConfig;
import cn.daxpay.open.platform.system.service.config.PlatformOssConfigService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

/// # 平台OSS配置提供者实现
///
@Slf4j
@Service
@RequiredArgsConstructor
public class OssConfigProviderImpl implements OssConfigProvider {

    private final PlatformOssConfigService platformOssConfigService;

    @Override
    public Optional<FileStorageConfig> getDefaultConfig() {
        PlatformOssConfig config = platformOssConfigService.getOssConfig();
        if (config == null || config.getEndpoint() == null) {
            log.warn("OSS配置不存在或未配置");
            return Optional.empty();
        }
        return Optional.of(convertToFileStorageConfig(config));
    }

    /// 将平台OSS配置转换为文件存储配置
    private FileStorageConfig convertToFileStorageConfig(PlatformOssConfig config) {
        FileStorageConfig storageConfig = new FileStorageConfig();
        storageConfig.setEndpoint(config.getEndpoint())
                .setRegion(config.getRegion())
                .setPublicBucket(config.getPublicBucket())
                .setPrivateBucket(config.getPrivateBucket())
                .setPublicBaseUrl(config.getPublicBaseUrl())
                .setPrivateBaseUrl(config.getPrivateBaseUrl())
                .setAccessKey(config.getAccessKey())
                .setSecretKey(config.getSecretKey())
                .setPathStyleAccess(config.getPathStyleAccess())
                .setUploadExpireMinutes(config.getUploadExpireMinutes())
                .setDownloadExpireHours(config.getDownloadExpireHours())
                .setBasePath(config.getBasePath());
        return storageConfig;
    }
}
