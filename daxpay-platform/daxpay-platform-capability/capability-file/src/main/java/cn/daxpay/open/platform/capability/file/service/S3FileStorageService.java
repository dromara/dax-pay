package cn.daxpay.open.platform.capability.file.service;

import cn.daxpay.open.platform.capability.file.entity.FileStorageConfig;
import cn.daxpay.open.platform.capability.file.provider.OssConfigProvider;
import cn.daxpay.open.platform.capability.file.result.S3UploadResult;
import cn.daxpay.open.platform.core.code.DaxPayErrorCode;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.HeadObjectRequest;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

/// # S3文件存储服务
///
/// 提供S3兼容存储的完整操作功能，包括文件上传、预签名URL生成、文件管理等。
/// 支持MinIO、阿里云OSS、腾讯云COS、AWS S3等S3兼容存储。
///
/// ### 主要功能
/// - 后端直传文件 - 服务端生成文件后直接上传到S3
/// - 生成上传预签名URL - 允许前端直接上传文件到S3
/// - 生成下载预签名URL - 允许前端直接从S3下载私有文件
/// - 检查文件是否存在
/// - 删除文件
/// - 构建公开/私有访问URL
///
/// @see StorageClientService S3客户端管理
/// @see OssConfigProvider OSS配置提供者
@Slf4j
@Service
@RequiredArgsConstructor
public class S3FileStorageService {

    private final StorageClientService storageClientService;
    private final OssConfigProvider ossConfigProvider;

    /// 上传预签名URL默认有效期（分钟）
    private static final int DEFAULT_UPLOAD_PRESIGN_EXPIRE_MINUTES = 10;

    /// 下载或查看预签名URL默认有效期（小时）
    private static final int DEFAULT_DOWNLOAD_PRESIGN_EXPIRE_HOURS = 72;

    /// 获取存储配置
    ///
    /// @return 存储配置，如果未配置则返回empty
    public Optional<FileStorageConfig> getStorageConfig() {
        return ossConfigProvider.getDefaultConfig();
    }

    /// 上传文件到私有桶
    ///
    /// 将文件字节数据直接上传到S3私有桶，并返回预签名下载URL。
    /// 适用于服务端生成文件并需要提供下载链接的场景。
    ///
    /// @param data        文件字节数据
    /// @param objectKey   对象Key（文件在S3中的路径）
    /// @param contentType 内容类型（MIME类型）
    /// @return 上传结果，包含文件名
    public S3UploadResult upload(byte[] data, String objectKey, String contentType) {
        FileStorageConfig storageConfig = getStorageConfig()
                .orElseThrow(() -> new IllegalStateException("存储配置不存在"));

        String bucket = storageConfig.getPrivateBucket();

        var s3Client = storageClientService.getS3Client();

        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucket)
                .key(objectKey)
                .contentType(contentType)
                .contentLength((long) data.length)
                .build();

        s3Client.putObject(request, RequestBody.fromBytes(data));

        log.info("上传文件成功: bucket={}, objectKey={}", bucket, objectKey);

        String filename = extractFilename(objectKey);

        return new S3UploadResult()
                .setFilename(filename);
    }

    /// 上传文件到私有桶（使用默认内容类型）
    ///
    /// 使用 application/octet-stream 作为默认内容类型。
    ///
    /// @param data      文件字节数据
    /// @param objectKey 对象Key（文件在S3中的路径）
    /// @return 上传结果，包含文件名
    public S3UploadResult upload(byte[] data, String objectKey) {
        return upload(data, objectKey, "application/octet-stream");
    }

    /// 生成上传预签名URL
    ///
    /// 生成一个有时效性的上传URL，前端可以使用该URL直接上传文件到S3，
    /// 无需通过后端中转，减少服务器压力。
    ///
    /// @param bucket      存储桶名称
    /// @param objectKey   对象Key（文件在S3中的路径）
    /// @param contentType 内容类型（MIME类型）
    /// @param fileSize    文件大小（字节）
    /// @return 预签名上传URL
    public String generateUploadPresignedUrl(String bucket, String objectKey,
                                              String contentType, long fileSize) {
        var presigner = storageClientService.getS3Presigner();

        PutObjectRequest.Builder requestBuilder = PutObjectRequest.builder()
                .bucket(bucket)
                .key(objectKey);

        if (StrUtil.isNotBlank(contentType)) {
            requestBuilder.contentType(contentType);
        }
        if (fileSize > 0) {
            requestBuilder.contentLength(fileSize);
        }

        int expireMinutes = getUploadPresignExpireMinutes();

        PresignedPutObjectRequest presignedRequest = presigner.presignPutObject(builder -> builder
                .putObjectRequest(requestBuilder.build())
                .signatureDuration(Duration.ofMinutes(expireMinutes)));

        return presignedRequest.url().toString();
    }

    /// 生成预览预签名URL
    ///
    /// 生成一个有时效性的预览URL，用于在浏览器中直接显示文件内容。
    /// 设置 Content-Disposition 为 inline，浏览器会尝试直接显示文件。
    ///
    /// @param bucket    存储桶名称
    /// @param objectKey 对象Key（文件在S3中的路径）
    /// @return 预签名预览URL
    public String generateAccessPresignedUrl(String bucket, String objectKey) {
        var presigner = storageClientService.getS3Presigner();

        GetObjectRequest.Builder requestBuilder = GetObjectRequest.builder()
                .bucket(bucket)
                .key(objectKey)
                .responseContentDisposition("inline");

        int expireHours = getDownloadPresignExpireHours();

        PresignedGetObjectRequest presignedRequest = presigner.presignGetObject(builder -> builder
                .getObjectRequest(requestBuilder.build())
                .signatureDuration(Duration.ofHours(expireHours)));

        return presignedRequest.url().toString();
    }

    /// 生成下载预签名URL
    ///
    /// 生成一个有时效性的下载URL，用于下载私有桶中的文件。
    /// 设置 Content-Disposition 为 attachment，浏览器会下载文件并使用指定的文件名保存。
    ///
    /// @param bucket           存储桶名称
    /// @param objectKey        对象Key（文件在S3中的路径）
    /// @param downloadFilename 下载时显示的文件名（可选，为null时使用对象Key中的文件名）
    /// @return 预签名下载URL
    public String generateDownloadPresignedUrl(String bucket, String objectKey,
                                                String downloadFilename) {
        var presigner = storageClientService.getS3Presigner();

        GetObjectRequest.Builder requestBuilder = GetObjectRequest.builder()
                .bucket(bucket)
                .key(objectKey);

        if (StrUtil.isNotBlank(downloadFilename)) {
            requestBuilder.responseContentDisposition("attachment; filename=\"" + downloadFilename + "\"");
        } else {
            requestBuilder.responseContentDisposition("attachment");
        }

        int expireHours = getDownloadPresignExpireHours();

        PresignedGetObjectRequest presignedRequest = presigner.presignGetObject(builder -> builder
                .getObjectRequest(requestBuilder.build())
                .signatureDuration(Duration.ofHours(expireHours)));

        return presignedRequest.url().toString();
    }

    /// 检查文件是否存在
    ///
    /// 通过HEAD请求检查S3中是否存在指定对象。
    ///
    /// @param bucket    存储桶名称
    /// @param objectKey 对象Key
    /// @return true-存在，false-不存在
    public boolean exists(String bucket, String objectKey) {
        var s3Client = storageClientService.getS3Client();

        try {
            HeadObjectRequest request = HeadObjectRequest.builder()
                    .bucket(bucket)
                    .key(objectKey)
                    .build();

            s3Client.headObject(request);
            return true;
        } catch (NoSuchKeyException e) {
            // 对象确定不存在, 唯一合法的 false 语义
            return false;
        } catch (S3Exception | SdkClientException e) {
            // 凭证失效/网络故障/桶不存在等基础设施错误不能误判为"文件不存在":
            // 用户已通过预签名 URL 上传成功, 确认阶段误报"未上传"会诱导重试并产生孤儿记录
            log.warn("检查文件存在性失败(存储服务异常): bucket={}, objectKey={}", bucket, objectKey, e);
            // 文件: 存储服务异常, 无法确认文件存在性
            throw new BizInfoException(DaxPayErrorCode.OPERATION_FAIL, "error.file.storageServiceError");
        }
    }

    /// 删除文件
    ///
    /// 从S3中删除指定对象。删除操作是不可逆的。
    ///
    /// @param bucket    存储桶名称
    /// @param objectKey 对象Key
    public void delete(String bucket, String objectKey) {
        var s3Client = storageClientService.getS3Client();

        DeleteObjectRequest request = DeleteObjectRequest.builder()
                .bucket(bucket)
                .key(objectKey)
                .build();

        s3Client.deleteObject(request);
        log.info("删除文件: bucket={}, objectKey={}", bucket, objectKey);
    }

    /// 获取公开访问URL
    ///
    /// 构建公开桶中文件的直接访问URL。
    /// 仅适用于公开桶，私有桶的文件需要使用预签名URL。
    ///
    /// @param storageConfig 存储配置
    /// @param objectKey     对象Key
    /// @return 公开访问URL，如果未配置publicBaseUrl则返回null
    public String getPublicUrl(FileStorageConfig storageConfig, String objectKey) {
        if (StrUtil.isBlank(storageConfig.getPublicBaseUrl())) {
            return null;
        }
        String bucket = storageConfig.getPublicBucket();
        return StrUtil.removeSuffix(storageConfig.getPublicBaseUrl(), "/") + "/" + bucket + "/" + objectKey;
    }

    /// 获取私有桶访问URL
    ///
    /// 构建私有桶中文件的访问URL。
    /// 如果配置了privateBaseUrl，则拼接返回；否则返回null。
    /// 注意：此方法返回的是基础URL，实际访问可能需要预签名。
    ///
    /// @param storageConfig 存储配置
    /// @param objectKey     对象Key
    /// @return 私有桶访问URL，如果未配置privateBaseUrl则返回null
    public String getPrivateUrl(FileStorageConfig storageConfig, String objectKey) {
        if (StrUtil.isBlank(storageConfig.getPrivateBaseUrl())) {
            return null;
        }
        return StrUtil.removeSuffix(storageConfig.getPrivateBaseUrl(), "/") + "/" + objectKey;
    }

    /// 计算上传预签名URL过期时间
    ///
    /// @return 过期时间
    public OffsetDateTime calculateUploadExpireTime() {
        int expireMinutes = getUploadPresignExpireMinutes();
        return OffsetDateTime.now(ZoneOffset.UTC).plusMinutes(expireMinutes);
    }

    /// 计算下载或查看预签名URL过期时间
    ///
    /// @return 过期时间
    public OffsetDateTime calculateDownloadExpireTime() {
        int expireHours = getDownloadPresignExpireHours();
        return OffsetDateTime.now(ZoneOffset.UTC).plusHours(expireHours);
    }

    /// 获取上传预签名URL有效期（分钟）
    ///
    /// 优先从配置中读取，如果配置为空则使用默认值。
    ///
    /// @return 有效期（分钟）
    private int getUploadPresignExpireMinutes() {
        return getStorageConfig()
                .map(FileStorageConfig::getUploadExpireMinutes)
                .filter(minutes -> minutes != null && minutes > 0)
                .orElse(DEFAULT_UPLOAD_PRESIGN_EXPIRE_MINUTES);
    }

    /// 获取下载或查看预签名URL有效期（小时）
    ///
    /// 优先从配置中读取，如果配置为空则使用默认值。
    ///
    /// @return 有效期（小时）
    private int getDownloadPresignExpireHours() {
        return getStorageConfig()
                .map(FileStorageConfig::getDownloadExpireHours)
                .filter(hours -> hours != null && hours > 0)
                .orElse(DEFAULT_DOWNLOAD_PRESIGN_EXPIRE_HOURS);
    }

    /// 从对象Key中提取文件名部分
    ///
    /// 例如：platform/2026/04/10/abc123.jpg -> abc123.jpg
    ///
    /// @param objectKey 对象Key
    /// @return 文件名
    private String extractFilename(String objectKey) {
        int lastSlashIndex = objectKey.lastIndexOf('/');
        if (lastSlashIndex >= 0 && lastSlashIndex < objectKey.length() - 1) {
            return objectKey.substring(lastSlashIndex + 1);
        }
        return objectKey;
    }
}

