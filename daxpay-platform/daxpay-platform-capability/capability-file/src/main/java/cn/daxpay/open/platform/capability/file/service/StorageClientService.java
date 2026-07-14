package cn.daxpay.open.platform.capability.file.service;

import cn.daxpay.open.platform.capability.file.entity.FileStorageConfig;
import cn.daxpay.open.platform.capability.file.provider.OssConfigProvider;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

import java.net.URI;
import java.util.Optional;

/// # S3客户端管理服务
///
/// 负责创建和缓存S3客户端及预签名器。采用懒加载和双重检查锁定模式，
/// 确保客户端只创建一次，提高性能。
///
/// ### 支持的存储服务
/// - MinIO - 开源对象存储
/// - 阿里云OSS
/// - 腾讯云COS
/// - AWS S3
/// - 其他S3兼容存储
///
/// ### 使用方式
/// ```java
/// // 获取S3客户端进行文件操作
/// S3Client s3Client = storageClientService.getS3Client();
/// s3Client.putObject(...);
///
/// // 获取预签名器生成预签名URL
/// S3Presigner presigner = storageClientService.getS3Presigner();
/// presigner.presignPutObject(...);
/// ```
///
/// @see OssConfigProvider OSS配置提供者
/// @see S3FileStorageService S3文件存储服务
@Slf4j
@Service
@RequiredArgsConstructor
public class StorageClientService {

    private final OssConfigProvider ossConfigProvider;

    /// S3客户端（懒加载）
    private S3Client s3Client;

    /// S3预签名器（懒加载）
    private volatile S3Presigner s3Presigner;

    /// S3兼容存储默认区域
    ///
    /// AWS SDK要求必须设置region，但对于MinIO等S3兼容存储，此值不影响实际功能。
    private static final String DEFAULT_REGION = "us-east-1";

    /// 获取S3客户端
    ///
    /// 采用双重检查锁定模式实现懒加载，确保线程安全。
    /// 客户端创建后会缓存，后续调用直接返回缓存的实例。
    ///
    /// @return S3客户端实例
    public S3Client getS3Client() {
        if (s3Client == null) {
            synchronized (this) {
                if (s3Client == null) {
                    s3Client = createS3Client();
                }
            }
        }
        return s3Client;
    }

    /// 获取S3预签名器
    ///
    /// 用于生成预签名URL。采用双重检查锁定模式实现懒加载。
    ///
    /// @return S3预签名器实例
    public S3Presigner getS3Presigner() {
        if (s3Presigner == null) {
            synchronized (this) {
                if (s3Presigner == null) {
                    s3Presigner = createS3Presigner();
                }
            }
        }
        return s3Presigner;
    }

    /// 丢弃缓存的 S3 客户端与预签名器
    ///
    /// OSS 配置更新后调用, 下次访问按新配置重建客户端.
    public void invalidate() {
        synchronized (this) {
            if (s3Client != null) {
                try {
                    s3Client.close();
                } catch (Exception e) {
                    log.warn("关闭 S3Client 失败: {}", e.getMessage());
                }
                s3Client = null;
            }
            if (s3Presigner != null) {
                try {
                    s3Presigner.close();
                } catch (Exception e) {
                    log.warn("关闭 S3Presigner 失败: {}", e.getMessage());
                }
                s3Presigner = null;
            }
            log.info("已使 S3 客户端缓存失效");
        }
    }

    /// 创建S3客户端
    ///
    /// 根据配置创建S3客户端，支持自定义endpoint（用于MinIO等兼容存储）。
    /// 启用path-style访问（forcePathStyle），适用于MinIO等不支持virtual-host风格的存储。
    ///
    /// @return S3客户端
    private S3Client createS3Client() {
        FileStorageConfig config = ossConfigProvider.getDefaultConfig()
                .orElseThrow(() -> new IllegalStateException("OSS配置不存在"));

        AwsBasicCredentials credentials = AwsBasicCredentials.create(
                config.getAccessKey(),
                config.getSecretKey()
        );

        String region = StrUtil.blankToDefault(config.getRegion(), DEFAULT_REGION);

        software.amazon.awssdk.services.s3.S3ClientBuilder builder = S3Client.builder()
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .region(Region.of(region));

        if (StrUtil.isNotBlank(config.getEndpoint())) {
            builder.endpointOverride(URI.create(config.getEndpoint()));
        }

        builder.forcePathStyle(config.isPathStyleAccess());

        log.info("创建S3客户端: endpoint={}, publicBucket={}, privateBucket={}, pathStyleAccess={}",
                config.getEndpoint(), config.getPublicBucket(), config.getPrivateBucket(), config.isPathStyleAccess());
        return builder.build();
    }

    /// 创建S3预签名器
    ///
    /// 预签名器用于生成有时效性的预签名URL，允许前端直接访问S3进行上传下载。
    ///
    /// @return S3预签名器
    private S3Presigner createS3Presigner() {
        FileStorageConfig config = ossConfigProvider.getDefaultConfig()
                .orElseThrow(() -> new IllegalStateException("OSS配置不存在"));

        var credentials = AwsBasicCredentials.create(
                config.getAccessKey(),
                config.getSecretKey()
        );

        String region = StrUtil.blankToDefault(config.getRegion(), DEFAULT_REGION);

        var builder = S3Presigner.builder()
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .region(Region.of(region));

        if (StrUtil.isNotBlank(config.getEndpoint())) {
            builder.endpointOverride(URI.create(config.getEndpoint()));
        }

        // 与 S3Client 保持一致: MinIO 等需 path-style, 否则预签名 URL 主机形态不正确
        builder.serviceConfiguration(S3Configuration.builder()
                .pathStyleAccessEnabled(config.isPathStyleAccess())
                .build());

        log.info("创建S3预签名器: pathStyleAccess={}", config.isPathStyleAccess());
        return builder.build();
    }

}

