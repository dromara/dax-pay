package cn.daxpay.open.platform.system.service.config.infra;

import cn.daxpay.open.platform.capability.file.service.StorageClientService;
import cn.daxpay.open.platform.common.i18n.util.I18nUtil;
import cn.daxpay.open.platform.system.convert.config.infra.PlatformOssConfigConvert;
import cn.daxpay.open.platform.system.entity.config.platform.infra.PlatformOssConfig;
import cn.daxpay.open.platform.system.enums.EncryptPlatformConfigTypeEnum;
import cn.daxpay.open.platform.system.param.config.infra.PlatformOssConfigParam;
import cn.daxpay.open.platform.system.result.config.infra.ConnectivityCheckResult;
import cn.daxpay.open.platform.system.result.config.infra.PlatformOssConfigResult;
import cn.daxpay.open.platform.system.service.config.SystemPlatformEncryptConfigService;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.HeadBucketRequest;
import software.amazon.awssdk.services.s3.model.NoSuchBucketException;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.net.URI;

/// # 平台OSS配置服务
///
/// 管理对象存储配置，数据通过加密配置服务进行加密存储
@Slf4j
@Service
@RequiredArgsConstructor
public class PlatformOssConfigService {

    private final SystemPlatformEncryptConfigService encryptConfigService;
    /// ObjectProvider 延迟获取, 避免与 StorageClientService → OssConfigProvider → 本类 循环依赖
    private final ObjectProvider<StorageClientService> storageClientServiceProvider;

    /// S3 兼容存储默认区域
    private static final String DEFAULT_REGION = "us-east-1";

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
        // 配置变更后丢弃缓存的 S3 客户端, 下次访问按新配置重建
        StorageClientService storageClientService = storageClientServiceProvider.getIfAvailable();
        if (storageClientService != null) {
            storageClientService.invalidate();
        }
    }

    /// 检查 OSS 连通性
    ///
    /// 请求体可带表单中的非敏感字段; accessKey/secretKey 为空时使用库中已存密钥.
    /// 使用临时 S3Client, 不影响全局缓存.
    public ConnectivityCheckResult checkOss(PlatformOssConfigParam param) {
        PlatformOssConfig saved = this.getOssConfig();
        PlatformOssConfig probe = mergeForCheck(saved, param);

        if (StrUtil.hasBlank(probe.getEndpoint(), probe.getPublicBucket(), probe.getPrivateBucket(),
                probe.getAccessKey(), probe.getSecretKey())) {
            return ConnectivityCheckResult.fail(I18nUtil.get("error.system.oss.incomplete"));
        }

        long start = System.currentTimeMillis();
        try (S3Client client = buildTempClient(probe)) {
            headBucket(client, probe.getPublicBucket());
            headBucket(client, probe.getPrivateBucket());
            long latency = System.currentTimeMillis() - start;
            return ConnectivityCheckResult.ok(
                    I18nUtil.get("error.system.oss.checkSuccess"),
                    latency,
                    null);
        } catch (NoSuchBucketException e) {
            long latency = System.currentTimeMillis() - start;
            log.warn("OSS 桶不存在: {}", e.getMessage());
            return ConnectivityCheckResult.fail(
                    I18nUtil.get("error.system.oss.bucketNotFound"),
                    latency,
                    e.statusCode());
        } catch (S3Exception e) {
            long latency = System.currentTimeMillis() - start;
            log.warn("OSS 连通性检查失败: status={}, msg={}", e.statusCode(), e.getMessage());
            if (e.statusCode() == 403 || e.statusCode() == 401) {
                return ConnectivityCheckResult.fail(
                        I18nUtil.get("error.system.oss.authFailed"),
                        latency,
                        e.statusCode());
            }
            return ConnectivityCheckResult.fail(
                    I18nUtil.get("error.system.oss.checkFailed", String.valueOf(e.statusCode())),
                    latency,
                    e.statusCode());
        } catch (Exception e) {
            long latency = System.currentTimeMillis() - start;
            log.warn("OSS 连通性检查网络异常: {}", e.getMessage());
            return ConnectivityCheckResult.fail(
                    I18nUtil.get("error.system.oss.networkError"),
                    latency,
                    null);
        }
    }

    /// 合并探测用配置: 请求非空字段覆盖库配置; 密钥仅在请求显式传入时覆盖
    private PlatformOssConfig mergeForCheck(PlatformOssConfig saved, PlatformOssConfigParam param) {
        PlatformOssConfig probe = new PlatformOssConfig();
        if (saved != null) {
            probe.setEndpoint(saved.getEndpoint())
                    .setRegion(saved.getRegion())
                    .setPublicBucket(saved.getPublicBucket())
                    .setPrivateBucket(saved.getPrivateBucket())
                    .setPublicBaseUrl(saved.getPublicBaseUrl())
                    .setPrivateBaseUrl(saved.getPrivateBaseUrl())
                    .setAccessKey(saved.getAccessKey())
                    .setSecretKey(saved.getSecretKey())
                    .setPathStyleAccess(saved.getPathStyleAccess())
                    .setUploadExpireMinutes(saved.getUploadExpireMinutes())
                    .setDownloadExpireHours(saved.getDownloadExpireHours())
                    .setBasePath(saved.getBasePath());
        }
        if (param == null) {
            return probe;
        }
        if (StrUtil.isNotBlank(param.getEndpoint())) {
            probe.setEndpoint(param.getEndpoint());
        }
        if (param.getRegion() != null) {
            probe.setRegion(param.getRegion());
        }
        if (StrUtil.isNotBlank(param.getPublicBucket())) {
            probe.setPublicBucket(param.getPublicBucket());
        }
        if (StrUtil.isNotBlank(param.getPrivateBucket())) {
            probe.setPrivateBucket(param.getPrivateBucket());
        }
        if (param.getPublicBaseUrl() != null) {
            probe.setPublicBaseUrl(param.getPublicBaseUrl());
        }
        if (param.getPrivateBaseUrl() != null) {
            probe.setPrivateBaseUrl(param.getPrivateBaseUrl());
        }
        if (param.getPathStyleAccess() != null) {
            probe.setPathStyleAccess(param.getPathStyleAccess());
        }
        if (param.getBasePath() != null) {
            probe.setBasePath(param.getBasePath());
        }
        // 密钥: 仅当请求携带非空明文时覆盖(前端未改密钥时不传)
        if (StrUtil.isNotBlank(param.getAccessKey())) {
            probe.setAccessKey(param.getAccessKey());
        }
        if (StrUtil.isNotBlank(param.getSecretKey())) {
            probe.setSecretKey(param.getSecretKey());
        }
        return probe;
    }

    private S3Client buildTempClient(PlatformOssConfig config) {
        AwsBasicCredentials credentials = AwsBasicCredentials.create(
                config.getAccessKey(),
                config.getSecretKey()
        );
        String region = StrUtil.blankToDefault(config.getRegion(), DEFAULT_REGION);
        var builder = S3Client.builder()
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .region(Region.of(region));
        if (StrUtil.isNotBlank(config.getEndpoint())) {
            builder.endpointOverride(URI.create(config.getEndpoint()));
        }
        builder.forcePathStyle(Boolean.TRUE.equals(config.getPathStyleAccess()));
        return builder.build();
    }

    private void headBucket(S3Client client, String bucket) {
        client.headBucket(HeadBucketRequest.builder().bucket(bucket).build());
    }
}
