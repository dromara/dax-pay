package cn.daxpay.open.platform.system.service.config.infra;

import cn.daxpay.open.platform.capability.file.service.StorageClientService;
import cn.daxpay.open.platform.common.i18n.util.I18nUtil;
import cn.daxpay.open.platform.system.convert.config.infra.PlatformOssConfigConvert;
import cn.daxpay.open.platform.system.entity.config.platform.infra.PlatformOssConfig;
import cn.daxpay.open.platform.system.enums.EncryptPlatformConfigTypeEnum;
import cn.daxpay.open.platform.system.param.config.infra.PlatformOssConfigParam;
import cn.daxpay.open.platform.system.result.config.infra.BucketCheckDetail;
import cn.daxpay.open.platform.system.result.config.infra.CheckStep;
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
import software.amazon.awssdk.core.checksums.RequestChecksumCalculation;
import software.amazon.awssdk.core.checksums.ResponseChecksumValidation;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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

    /// 检查步骤标识: 连接与桶存在
    private static final String STEP_CONNECT = "connect";

    /// 检查步骤标识: 写入探测对象
    private static final String STEP_WRITE = "write";

    /// 检查步骤标识: 读取探测对象
    private static final String STEP_READ = "read";

    /// 检查步骤标识: 公开访问可达性(仅公开桶)
    private static final String STEP_PUBLIC_ACCESS = "publicAccess";

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
    /// 因此这里对公开桶与私有桶**各自独立**走一遍最小读写闭环, 一桶失败不中断另一桶的检查, 一次返回两桶的全部分步明细:
    /// - 连接与桶存在 - 桶不存在/认证失败/网络不通在此暴露
    /// - 写入探测对象 - 验证写权限与对象键路径(与真实上传同一 basePath 前缀)
    /// - 读取探测对象 - 验证读权限(文件上传确认阶段依赖 HEAD, 只写权限的密钥会在此暴露)
    /// - 公开访问(仅公开桶) - 配置公开访问域名时验证文件可被真实访问(上传成功但取不到图属于此类问题)
    ///
    /// 探测对象在读取环节结束后删除, 删除失败不影响检查结论.
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
            // 两桶各自独立探测, 互不中断, 汇总返回整体情况
            BucketCheckDetail publicDetail = probeBucket(client, probe.getPublicBucket(), true, probeKey, probe);
            BucketCheckDetail privateDetail = probeBucket(client, probe.getPrivateBucket(), false, probeKey, probe);

            long latency = System.currentTimeMillis() - start;
            boolean allOk = publicDetail.isSuccess() && privateDetail.isSuccess();
            String warning = resolveTopWarning(publicDetail);
            if (allOk) {
                log.info("OSS 读写检查通过: endpoint={}, probeKey={}, warning={}", probe.getEndpoint(), probeKey, warning);
                return ConnectivityCheckResult.ok(
                        I18nUtil.get("error.system.oss.checkSuccess"),
                        latency,
                        null,
                        warning)
                        .setPublicBucketDetail(publicDetail)
                        .setPrivateBucketDetail(privateDetail);
            }
            log.warn("OSS 读写检查未全部通过: endpoint={}, probeKey={}, publicOk={}, privateOk={}, warning={}",
                    probe.getEndpoint(), probeKey, publicDetail.isSuccess(), privateDetail.isSuccess(), warning);
            return ConnectivityCheckResult.fail(
                    I18nUtil.get("error.system.oss.checkHasFailures"),
                    latency,
                    null)
                    .setPublicBucketDetail(publicDetail)
                    .setPrivateBucketDetail(privateDetail)
                    .setWarning(warning);
        } catch (Exception e) {
            long latency = System.currentTimeMillis() - start;
            log.warn("OSS 连通性检查异常: {}", e.getMessage());
            return ConnectivityCheckResult.fail(
                    I18nUtil.get("error.system.oss.networkError"),
                    latency,
                    null);
        }
    }

    /// 对单个存储桶执行最小读写闭环, 逐步记录检查结果
    ///
    /// 失败不抛出, 而是记录到对应步骤后跳过后续步骤, 便于一次看到两桶的整体情况.
    ///
    /// @param client   临时 S3 客户端
    /// @param bucket   存储桶名称
    /// @param isPublic 是否为公开桶(决定失败文案与是否检查公开访问)
    /// @param probeKey 本次检查使用的探测对象键
    /// @param probe    本次检查实际使用的配置
    private BucketCheckDetail probeBucket(S3Client client, String bucket, boolean isPublic, String probeKey, PlatformOssConfig probe) {
        BucketCheckDetail detail = new BucketCheckDetail()
                .setBucketName(bucket)
                .setSteps(new ArrayList<>());
        List<CheckStep> steps = detail.getSteps();

        // 步骤一: 连接与桶存在(首个 S3 请求, 网络不通/认证失败/桶不存在都在此暴露)
        try {
            headBucket(client, bucket);
            steps.add(CheckStep.of(STEP_CONNECT, CheckStep.STATUS_SUCCESS));
        } catch (NoSuchBucketException e) {
            log.warn("OSS 桶不存在: bucket={}, msg={}", bucket, e.getMessage());
            steps.add(CheckStep.of(STEP_CONNECT, CheckStep.STATUS_FAILED,
                    I18nUtil.get("error.system.oss.bucketNotFound")));
            return skipRemaining(detail, isPublic, STEP_WRITE, STEP_READ);
        } catch (S3Exception e) {
            log.warn("OSS 连接检查失败: bucket={}, status={}, msg={}", bucket, e.statusCode(), e.getMessage());
            String message = resolveConnectFailureMessage(e);
            steps.add(CheckStep.of(STEP_CONNECT, CheckStep.STATUS_FAILED, message));
            return skipRemaining(detail, isPublic, STEP_WRITE, STEP_READ);
        } catch (Exception e) {
            log.warn("OSS 连接异常: bucket={}, msg={}", bucket, e.getMessage());
            steps.add(CheckStep.of(STEP_CONNECT, CheckStep.STATUS_FAILED,
                    I18nUtil.get("error.system.oss.networkError")));
            return skipRemaining(detail, isPublic, STEP_WRITE, STEP_READ);
        }

        // 步骤二 + 三: 写入与读取(读取失败单独归因, "能写不能读"正是上传确认阶段失败却又留下对象的典型症状)
        boolean objectWritten = false;
        try {
            // 写入探测对象: 验证写权限, 同时验证对象键路径能被该存储服务正确接受
            try {
                client.putObject(PutObjectRequest.builder()
                                .bucket(bucket)
                                .key(probeKey)
                                .contentType("text/plain")
                                .contentLength((long) PROBE_CONTENT.length)
                                .build(),
                        RequestBody.fromBytes(PROBE_CONTENT));
                objectWritten = true;
                steps.add(CheckStep.of(STEP_WRITE, CheckStep.STATUS_SUCCESS));
            } catch (Exception e) {
                log.warn("OSS 探测写入失败: bucket={}, key={}, msg={}", bucket, probeKey, e.getMessage());
                steps.add(CheckStep.of(STEP_WRITE, CheckStep.STATUS_FAILED,
                        I18nUtil.get(isPublic ? "error.system.oss.publicBucketWriteFailed" : "error.system.oss.privateBucketWriteFailed",
                                resolveS3ErrorCode(e))));
                return skipRemaining(detail, isPublic, STEP_READ);
            }

            // 读取探测对象: 文件上传确认阶段依赖 HEAD, 只写权限的密钥会在此暴露
            try {
                client.headObject(HeadObjectRequest.builder().bucket(bucket).key(probeKey).build());
                steps.add(CheckStep.of(STEP_READ, CheckStep.STATUS_SUCCESS));
            } catch (Exception e) {
                log.warn("OSS 探测对象写入后读取失败: bucket={}, key={}, msg={}", bucket, probeKey, e.getMessage());
                steps.add(CheckStep.of(STEP_READ, CheckStep.STATUS_FAILED,
                        I18nUtil.get(isPublic ? "error.system.oss.publicBucketReadFailed" : "error.system.oss.privateBucketReadFailed",
                                resolveS3ErrorCode(e))));
                return skipRemaining(detail, isPublic);
            }

            // 步骤四(仅公开桶): 确认文件能否通过公开访问域名取到, 须在探测对象删除前执行;
            // 不可达仅作警告, 不影响闭环结论(服务端与浏览器网络环境不同)
            if (isPublic) {
                String warning = checkPublicAccessible(probe, probeKey);
                if (Objects.isNull(warning)) {
                    steps.add(CheckStep.of(STEP_PUBLIC_ACCESS, CheckStep.STATUS_SUCCESS));
                } else {
                    steps.add(CheckStep.of(STEP_PUBLIC_ACCESS, CheckStep.STATUS_WARNING, warning));
                }
            }
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

        return detail.setSuccess(true);
    }

    /// 连接步骤失败时的提示文案: 401/403 归因认证, 其余携带状态码
    private String resolveConnectFailureMessage(S3Exception e) {
        if (e.statusCode() == 403 || e.statusCode() == 401) {
            return I18nUtil.get("error.system.oss.authFailed");
        }
        return I18nUtil.get("error.system.oss.checkFailed", String.valueOf(e.statusCode()));
    }

    /// 提取 S3 错误的服务端错误码(如 AccessDenied / SignatureDoesNotMatch), 非服务端错误时退回异常类名
    ///
    /// 阿里云 OSS 等 S3 兼容存储的拒绝原因藏在错误码里, 与"密钥是否有权限"并不等价, 需原样透出供排查.
    private String resolveS3ErrorCode(Exception e) {
        if (e instanceof S3Exception s3 && Objects.nonNull(s3.awsErrorDetails())
                && StrUtil.isNotBlank(s3.awsErrorDetails().errorCode())) {
            return s3.awsErrorDetails().errorCode();
        }
        return e.getClass().getSimpleName();
    }

    /// 将未执行到的后续步骤标记为跳过, 并以失败结束该桶的检查
    ///
    /// @param detail       桶检查明细
    /// @param isPublic     是否为公开桶(公开桶多一个公开访问步骤)
    /// @param remainSteps  未执行的后续步骤标识
    private BucketCheckDetail skipRemaining(BucketCheckDetail detail, boolean isPublic, String... remainSteps) {
        for (String step : remainSteps) {
            detail.getSteps().add(CheckStep.of(step, CheckStep.STATUS_SKIPPED));
        }
        if (isPublic) {
            // 未显式列出的公开访问步骤同样未执行
            boolean publicAccessListed = Arrays.asList(remainSteps).contains(STEP_PUBLIC_ACCESS);
            if (!publicAccessListed) {
                detail.getSteps().add(CheckStep.of(STEP_PUBLIC_ACCESS, CheckStep.STATUS_SKIPPED));
            }
        }
        detail.setMessage(detail.getSteps().stream()
                .filter(s -> Objects.equals(s.getStatus(), CheckStep.STATUS_FAILED))
                .map(CheckStep::getMessage)
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null));
        return detail;
    }

    /// 从公开桶明细提取顶层警告(公开访问地址不可达, 服务端与浏览器网络环境不同, 不影响闭环结论)
    private String resolveTopWarning(BucketCheckDetail publicDetail) {
        return publicDetail.getSteps().stream()
                .filter(s -> Objects.equals(s.getStatus(), CheckStep.STATUS_WARNING))
                .map(CheckStep::getMessage)
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
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
                + "/" + probeKey;
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
                .region(Region.of(region))
                // 与全局客户端一致: 禁用 SDK 默认 CRC32 校验(aws-chunked/trailer), 阿里云 OSS 等
                // S3 兼容存储不支持该机制, 会在探测写入时报 501 NotImplemented
                .requestChecksumCalculation(RequestChecksumCalculation.WHEN_REQUIRED)
                .responseChecksumValidation(ResponseChecksumValidation.WHEN_REQUIRED);
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
