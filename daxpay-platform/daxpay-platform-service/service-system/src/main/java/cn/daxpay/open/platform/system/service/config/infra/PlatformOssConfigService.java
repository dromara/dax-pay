package cn.daxpay.open.platform.system.service.config.infra;

import cn.daxpay.open.platform.capability.file.service.StorageClientService;
import cn.daxpay.open.platform.common.i18n.util.I18nUtil;
import cn.daxpay.open.platform.core.code.DaxPayErrorCode;
import cn.daxpay.open.platform.core.exception.BizException;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.system.convert.config.infra.PlatformOssConfigConvert;
import cn.daxpay.open.platform.system.entity.config.platform.infra.PlatformOssConfig;
import cn.daxpay.open.platform.system.enums.EncryptPlatformConfigTypeEnum;
import cn.daxpay.open.platform.system.param.config.infra.PlatformOssConfigParam;
import cn.daxpay.open.platform.system.result.config.infra.ConnectivityCheckResult;
import cn.daxpay.open.platform.system.result.config.infra.PlatformOssConfigResult;
import cn.daxpay.open.platform.system.service.config.SystemPlatformEncryptConfigService;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.http.Method;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.HeadBucketRequest;
import software.amazon.awssdk.services.s3.model.HeadObjectRequest;
import software.amazon.awssdk.services.s3.model.NoSuchBucketException;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.UUID;

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

    /// 探测对象所在目录(位于基础存储路径之下, 与真实文件同前缀)
    private static final String HEALTH_CHECK_DIR = ".healthcheck";

    /// 探测对象内容(仅用于验证读写权限, 不承载业务数据)
    private static final byte[] PROBE_CONTENT = "daxpay oss probe".getBytes(StandardCharsets.UTF_8);

    /// 公开访问地址可达性检查超时时间(毫秒)
    private static final int PUBLIC_URL_CHECK_TIMEOUT_MS = 10_000;

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
        if (Objects.nonNull(storageClientService)) {
            storageClientService.invalidate();
        }
    }

    /// 检查 OSS 连通性并验证读写闭环
    ///
    /// 请求体可带表单中的非敏感字段; accessKey/secretKey 为空时使用库中已存密钥.
    /// 使用临时 S3Client, 不影响全局缓存.
    ///
    /// ### 检查内容
    /// 仅探测存储桶存在性无法发现"密钥只有写权限/对象键路径错误"这类问题, 真正的失败往往出现在上传之后的确认环节,
    /// 因此这里对公开桶与私有桶各走一遍最小读写闭环:
    /// - 桶存在性 - 桶不存在直接给出桶名提示
    /// - 写入探测对象 - 验证写权限与对象键路径(与真实上传同一 basePath 前缀)
    /// - 读取探测对象 - 验证读权限(文件上传确认阶段依赖 HEAD, 只写权限的密钥会在此暴露)
    /// - 删除探测对象 - 清理, 删除失败不影响检查结论
    ///
    /// 另在配置了公开访问域名时, 用 HTTP 请求验证公开桶文件可被真实访问(上传成功但取不到图属于此类问题).
    public ConnectivityCheckResult checkOss(PlatformOssConfigParam param) {
        PlatformOssConfig saved = this.getOssConfig();
        PlatformOssConfig probe = mergeForCheck(saved, param);

        if (StrUtil.hasBlank(probe.getEndpoint(), probe.getPublicBucket(), probe.getPrivateBucket(),
                probe.getAccessKey(), probe.getSecretKey())) {
            return ConnectivityCheckResult.fail(I18nUtil.get("error.system.oss.incomplete"));
        }

        long start = System.currentTimeMillis();
        String probeKey = buildProbeKey(probe.getBasePath());
        try (S3Client client = buildTempClient(probe)) {
            // 两个桶都走完整闭环; 公开桶额外验证"上传后可被公开访问域名取到"
            String publicWarning = probeBucket(client, probe.getPublicBucket(), true, probeKey, probe);
            probeBucket(client, probe.getPrivateBucket(), false, probeKey, probe);

            long latency = System.currentTimeMillis() - start;
            log.info("OSS 读写检查通过: endpoint={}, probeKey={}, publicWarning={}",
                    probe.getEndpoint(), probeKey, publicWarning);
            return ConnectivityCheckResult.ok(
                    I18nUtil.get("error.system.oss.checkSuccess"),
                    latency,
                    null,
                    publicWarning);
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
        } catch (BizException e) {
            // 探测环节的具体失败(写不进/读不出), 携带已定位到环节的提示文案
            long latency = System.currentTimeMillis() - start;
            return ConnectivityCheckResult.fail(
                    I18nUtil.get(e.resolveMessageKey(), e.getArgs()),
                    latency,
                    null);
        } catch (Exception e) {
            long latency = System.currentTimeMillis() - start;
            log.warn("OSS 连通性检查网络异常: {}", e.getMessage());
            return ConnectivityCheckResult.fail(
                    I18nUtil.get("error.system.oss.networkError"),
                    latency,
                    null);
        }
    }

    /// 对单个存储桶执行最小读写闭环
    ///
    /// 失败时抛出携带具体环节的业务异常(是写不进去还是读不出来), 便于使用方一眼定位.
    ///
    /// @param client   临时 S3 客户端
    /// @param bucket   存储桶名称
    /// @param isPublic 是否为公开桶(仅用于提示文案区分)
    /// @param probeKey 本次检查使用的探测对象键
    /// @param probe    本次检查实际使用的配置
    /// @return 公开桶的公开地址可达性警告(非公开桶恒为 null)
    private String probeBucket(S3Client client, String bucket, boolean isPublic, String probeKey, PlatformOssConfig probe) {
        headBucket(client, bucket);

        boolean objectWritten = false;
        try {
            // 写入探测对象: 验证写权限, 同时验证对象键路径能被该存储服务正确接受
            client.putObject(PutObjectRequest.builder()
                            .bucket(bucket)
                            .key(probeKey)
                            .contentType("text/plain")
                            .contentLength((long) PROBE_CONTENT.length)
                            .build(),
                    RequestBody.fromBytes(PROBE_CONTENT));
            objectWritten = true;

            // 读取探测对象: 文件上传确认阶段依赖 HEAD, 只写权限的密钥会在此暴露
            assertReadable(client, bucket, probeKey, isPublic);
        } catch (NoSuchBucketException e) {
            throw e;
        } catch (S3Exception e) {
            log.warn("OSS 探测写入失败: bucket={}, key={}, status={}, msg={}",
                    bucket, probeKey, e.statusCode(), e.getMessage());
            throw new BizInfoException(DaxPayErrorCode.OPERATION_FAIL,
                    isPublic ? "error.system.oss.publicBucketWriteFailed" : "error.system.oss.privateBucketWriteFailed");
        } finally {
            if (objectWritten) {
                try {
                    client.deleteObject(DeleteObjectRequest.builder().bucket(bucket).key(probeKey).build());
                } catch (Exception e) {
                    // 清理失败不影响检查结论, 仅留痕(探测对象为几十字节的临时文件)
                    log.warn("OSS 探测对象清理失败: bucket={}, key={}, msg={}", bucket, probeKey, e.getMessage());
                }
            }
        }

        // 公开桶额外确认文件能否通过公开访问域名取到(上传成功但取不到图属于此类问题)
        return isPublic ? checkPublicAccessible(probe, probeKey) : null;
    }

    /// 校验刚写入的探测对象能否被读取
    ///
    /// 读失败单独归因(与写失败区分), 因为"能写不能读"正是上传确认阶段失败却又留下对象的典型症状.
    ///
    /// @param client   临时 S3 客户端
    /// @param bucket   存储桶名称
    /// @param probeKey 探测对象键
    /// @param isPublic 是否为公开桶(仅用于提示文案区分)
    /// @throws BizInfoException 读取失败时抛出
    private void assertReadable(S3Client client, String bucket, String probeKey, boolean isPublic) {
        try {
            client.headObject(HeadObjectRequest.builder().bucket(bucket).key(probeKey).build());
        } catch (S3Exception e) {
            log.warn("OSS 探测对象写入后读取失败: bucket={}, key={}, status={}", bucket, probeKey, e.statusCode());
            throw new BizInfoException(DaxPayErrorCode.OPERATION_FAIL,
                    isPublic ? "error.system.oss.publicBucketReadFailed" : "error.system.oss.privateBucketReadFailed");
        }
    }

    /// 验证公开桶文件能否通过公开访问域名真实访问
    ///
    /// 未配置公开访问域名时不做检查(此时公开桶文件本就依赖预签名 URL 访问).
    ///
    /// @param probe    本次检查实际使用的配置(表单值与库中值的合并结果)
    /// @param probeKey 探测对象键
    /// @return 警告信息(仅当上传链路本身正常但公开地址不可达时有值), 无问题时返回 null
    private String checkPublicAccessible(PlatformOssConfig probe, String probeKey) {
        if (StrUtil.isBlank(probe.getPublicBaseUrl())) {
            return null;
        }
        String url = StrUtil.removeSuffix(probe.getPublicBaseUrl(), "/")
                + "/" + probe.getPublicBucket() + "/" + probeKey;
        try {
            int status = HttpUtil.createRequest(Method.HEAD, url)
                    .timeout(PUBLIC_URL_CHECK_TIMEOUT_MS)
                    .execute()
                    .getStatus();
            if (status >= 200 && status < 300) {
                return null;
            }
            log.warn("OSS 公开访问地址不可达: url={}, status={}", url, status);
        } catch (Exception e) {
            // 服务端与浏览器网络环境不同, 此处不可达不代表浏览器不可达, 故仅作警告提示
            log.warn("OSS 公开访问地址请求异常: url={}, msg={}", url, e.getMessage());
        }
        return I18nUtil.get("error.system.oss.publicUrlUnreachable");
    }

    /// 构建探测对象键
    ///
    /// 与真实上传保持同一路径前缀(含基础存储路径), 以便发现"密钥只允许写入特定前缀"这类受限授权问题.
    ///
    /// @param basePath 基础存储路径(可为空)
    /// @return 探测对象键
    private String buildProbeKey(String basePath) {
        String uuid = UUID.randomUUID().toString().replace("-", "");
        String key = HEALTH_CHECK_DIR + "/" + uuid + ".txt";
        if (StrUtil.isBlank(basePath)) {
            return key;
        }
        String normalizedBasePath = StrUtil.removeSuffix(StrUtil.removePrefix(basePath.trim(), "/"), "/");
        return normalizedBasePath + "/" + key;
    }

    /// 合并探测用配置: 请求非空字段覆盖库配置; 密钥仅在请求显式传入时覆盖
    private PlatformOssConfig mergeForCheck(PlatformOssConfig saved, PlatformOssConfigParam param) {
        PlatformOssConfig probe = new PlatformOssConfig();
        if (Objects.nonNull(saved)) {
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
        if (Objects.isNull(param)) {
            return probe;
        }
        if (StrUtil.isNotBlank(param.getEndpoint())) {
            probe.setEndpoint(param.getEndpoint());
        }
        if (Objects.nonNull(param.getRegion())) {
            probe.setRegion(param.getRegion());
        }
        if (StrUtil.isNotBlank(param.getPublicBucket())) {
            probe.setPublicBucket(param.getPublicBucket());
        }
        if (StrUtil.isNotBlank(param.getPrivateBucket())) {
            probe.setPrivateBucket(param.getPrivateBucket());
        }
        if (Objects.nonNull(param.getPublicBaseUrl())) {
            probe.setPublicBaseUrl(param.getPublicBaseUrl());
        }
        if (Objects.nonNull(param.getPrivateBaseUrl())) {
            probe.setPrivateBaseUrl(param.getPrivateBaseUrl());
        }
        if (Objects.nonNull(param.getPathStyleAccess())) {
            probe.setPathStyleAccess(param.getPathStyleAccess());
        }
        if (Objects.nonNull(param.getBasePath())) {
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
