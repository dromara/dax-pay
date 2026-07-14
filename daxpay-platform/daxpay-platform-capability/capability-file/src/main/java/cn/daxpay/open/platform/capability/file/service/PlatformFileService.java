package cn.daxpay.open.platform.capability.file.service;

import cn.daxpay.open.platform.capability.file.code.FileUploadStatusEnum;
import cn.daxpay.open.platform.capability.file.code.UploadAccessTypeEnum;
import cn.daxpay.open.platform.capability.file.dao.PlatformFileRecordManager;
import cn.daxpay.open.platform.capability.file.entity.FileStorageConfig;
import cn.daxpay.open.platform.capability.file.entity.PlatformFileRecord;
import cn.daxpay.open.platform.capability.file.param.FileUploadConfirmParam;
import cn.daxpay.open.platform.capability.file.param.FileUploadPresignParam;
import cn.daxpay.open.platform.capability.file.result.FileUploadPresignResult;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.code.DaxPayErrorCode;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

/// # 平台文件服务
///
/// 提供平台文件的完整生命周期管理，包括上传、访问、下载等功能。
/// 基于S3预签名URL实现前端直传，减少服务器压力。
///
/// ### 主要功能
/// - 预签名上传 - 前端直接上传文件到S3
/// - 上传确认 - 验证文件并更新记录状态
/// - 文件访问 - 通过文件名直接访问/下载文件
/// - 文件下载 - 通过文件ID下载文件内容（服务端使用）
///
/// ### 预签名上传流程
/// - 前端调用 getUploadPresignUrl 获取预签名URL和fileId
/// - 前端使用预签名URL直接上传文件到S3
/// - 前端调用 confirmUpload 确认上传完成
/// - 通过 /file/platform/access/{filename} 或 /file/platform/download/{filename} 访问文件
///
/// @see S3FileStorageService S3文件存储服务
/// @see PlatformFileRecordManager 文件记录管理
@Slf4j
@Service
@RequiredArgsConstructor
public class PlatformFileService {

    private final S3FileStorageService s3FileStorageService;
    private final PlatformFileRecordManager platformFileRecordManager;

    /// 获取上传预签名URL
    ///
    /// 生成S3预签名上传URL，同时创建待上传状态的文件记录。
    /// 根据accessType选择公开桶或私有桶：
    /// - public - 使用公开桶，文件可通过公开URL直接访问
    /// - private - 使用私有桶，文件需要预签名URL才能访问
    ///
    /// @param param 上传参数，包含文件名、大小、内容类型、访问类型等
    /// @return 预签名结果，包含fileId、objectKey、上传URL、过期时间
    @Transactional(rollbackFor = Exception.class)
    public FileUploadPresignResult getUploadPresignUrl(FileUploadPresignParam param) {
        var storageConfig = s3FileStorageService.getStorageConfig()
                // 文件: 存储配置不存在
                .orElseThrow(() -> new DataNotExistException("error.file.storageConfigNotExist"));

        String bucket = selectBucket(storageConfig, param.getAccessType());

        String fileExtension = FileUtil.extName(param.getFileName());
        String objectKey = generateObjectKey(fileExtension, storageConfig.getBasePath());
        String path = extractPath(objectKey);
        String filename = extractFilename(objectKey);

        String uploadUrl = s3FileStorageService.generateUploadPresignedUrl(
                bucket, objectKey, param.getContentType(), param.getFileSize()
        );

        OffsetDateTime expireTime = s3FileStorageService.calculateUploadExpireTime();

        var platformFileRecord = new PlatformFileRecord()
                .setPath(path)
                .setSize(param.getFileSize())
                .setFilename(filename)
                .setOriginalFilename(param.getFileName())
                .setExt(fileExtension)
                .setContentType(param.getContentType())
                .setAccessType(param.getAccessType())
                .setBizType(param.getBusinessType())
                .setStatus(FileUploadStatusEnum.PENDING.getCode());

        platformFileRecordManager.save(platformFileRecord);

        var result = new FileUploadPresignResult();
        result.setFileId(platformFileRecord.getId());
        result.setObjectKey(objectKey);
        result.setFilename(filename);
        result.setUploadUrl(uploadUrl);
        result.setExpireTime(expireTime);

        log.info("创建上传预签名: fileId={}, objectKey={}, bucket={}", platformFileRecord.getId(), objectKey, bucket);
        return result;
    }

    /// 确认上传
    ///
    /// 验证文件是否已成功上传到S3，并更新文件状态为已上传。
    /// 只有状态为PENDING的文件才能确认。
    ///
    /// @param param 确认参数，包含fileId和objectKey
    /// @throws IllegalStateException 文件状态不正确或文件尚未上传
    /// @throws IllegalArgumentException 对象Key不匹配
    @Transactional(rollbackFor = Exception.class)
    public void confirmUpload(FileUploadConfirmParam param) {
        PlatformFileRecord platformFileRecord = platformFileRecordManager.findByIdNotDeleted(param.getFileId())
                // 文件: 文件记录不存在
                .orElseThrow(() -> new DataNotExistException("error.file.recordNotExist"));

        if (!FileUploadStatusEnum.PENDING.getCode().equals(platformFileRecord.getStatus())) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "error.file.statusInvalidForConfirm");
        }

        String objectKey = buildObjectKey(platformFileRecord.getPath(), platformFileRecord.getFilename());
        if (!objectKey.equals(param.getObjectKey())) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "error.file.objectKeyMismatch");
        }

        var storageConfig = s3FileStorageService.getStorageConfig()
                // 文件: 存储配置不存在
                .orElseThrow(() -> new DataNotExistException("error.file.storageConfigNotExist"));

        String bucket = selectBucket(storageConfig, platformFileRecord.getAccessType());

        boolean exists = s3FileStorageService.exists(bucket, param.getObjectKey());
        if (!exists) {
            throw new BizInfoException(DaxPayErrorCode.OPERATION_FAIL, "error.file.notUploadedToStorage");
        }

        if (param.getFileSize() != null && param.getFileSize() > 0) {
            platformFileRecord.setSize(param.getFileSize());
        }
        if (StrUtil.isNotBlank(param.getContentType())) {
            platformFileRecord.setContentType(param.getContentType());
        }

        platformFileRecord.setStatus(FileUploadStatusEnum.UPLOADED.getCode());

        platformFileRecordManager.updateById(platformFileRecord);
        log.info("确认上传: fileId={}, objectKey={}", platformFileRecord.getId(), param.getObjectKey());
    }

    /// 根据文件名获取文件访问URL（用于预览）
    ///
    /// 根据文件的访问类型返回对应的访问地址：
    /// - 公开文件 - 直接返回公开访问URL
    /// - 私有文件 - 生成有时效的预签名URL（inline模式，浏览器直接显示）
    ///
    /// @param filename 文件名（UUID.后缀）
    /// @return 文件访问URL
    /// @throws DataNotExistException 文件记录不存在
    /// @throws IllegalStateException 文件未上传完成
    public String getAccessUrlByFilename(String filename) {
        PlatformFileRecord platformFileRecord = platformFileRecordManager.findByFilename(filename)
                // 文件: 文件记录不存在
                .orElseThrow(() -> new DataNotExistException("error.file.recordNotExist"));

        if (!FileUploadStatusEnum.UPLOADED.getCode().equals(platformFileRecord.getStatus())) {
            throw new BizInfoException(DaxPayErrorCode.OPERATION_FAIL, "error.file.notUploadedForAccess");
        }

        FileStorageConfig storageConfig = s3FileStorageService.getStorageConfig()
                // 文件: 存储配置不存在
                .orElseThrow(() -> new DataNotExistException("error.file.storageConfigNotExist"));

        String bucket = selectBucket(storageConfig, platformFileRecord.getAccessType());
        String objectKey = buildObjectKey(platformFileRecord.getPath(), platformFileRecord.getFilename());

        if (UploadAccessTypeEnum.PUBLIC.getCode().equals(platformFileRecord.getAccessType())) {
            return s3FileStorageService.getPublicUrl(storageConfig, objectKey);
        }

        return s3FileStorageService.generateAccessPresignedUrl(bucket, objectKey);
    }

    /// 根据文件名获取文件下载URL（用于下载）
    ///
    /// 生成带Content-Disposition头的预签名URL，浏览器会使用原始文件名保存。
    /// 无论是公开文件还是私有文件，下载时都走预签名URL以获取正确的文件名。
    ///
    /// @param filename 文件名（UUID.后缀）
    /// @return 文件下载URL
    /// @throws DataNotExistException 文件记录不存在
    /// @throws IllegalStateException 文件未上传完成
    public String getDownloadUrlByFilename(String filename) {
        PlatformFileRecord platformFileRecord = platformFileRecordManager.findByFilename(filename)
                // 文件: 文件记录不存在
                .orElseThrow(() -> new DataNotExistException("error.file.recordNotExist"));

        if (!FileUploadStatusEnum.UPLOADED.getCode().equals(platformFileRecord.getStatus())) {
            throw new BizInfoException(DaxPayErrorCode.OPERATION_FAIL, "error.file.notUploadedForDownload");
        }

        FileStorageConfig storageConfig = s3FileStorageService.getStorageConfig()
                // 文件: 存储配置不存在
                .orElseThrow(() -> new DataNotExistException("error.file.storageConfigNotExist"));

        String bucket = selectBucket(storageConfig, platformFileRecord.getAccessType());
        String objectKey = buildObjectKey(platformFileRecord.getPath(), platformFileRecord.getFilename());

        return s3FileStorageService.generateDownloadPresignedUrl(
                bucket,
                objectKey,
                platformFileRecord.getOriginalFilename()
        );
    }

    /// 下载文件并校验
    ///
    /// 通过文件ID下载文件内容，会校验文件状态是否为已上传。
    /// 适用于需要读取文件内容进行处理的场景。
    ///
    /// @param fileId 文件ID
    /// @return 文件字节数组
    /// @throws DataNotExistException 文件记录不存在
    /// @throws IllegalStateException 文件未上传完成
    public byte[] downloadAndCheck(Long fileId) {
        PlatformFileRecord platformFileRecord = platformFileRecordManager.findByIdNotDeleted(fileId)
                // 文件: 文件记录不存在
                .orElseThrow(() -> new DataNotExistException("error.file.recordNotExist"));

        if (!FileUploadStatusEnum.UPLOADED.getCode().equals(platformFileRecord.getStatus())) {
            throw new BizInfoException(DaxPayErrorCode.OPERATION_FAIL, "error.file.notUploadedForDownload");
        }

        FileStorageConfig storageConfig = s3FileStorageService.getStorageConfig()
                // 文件: 存储配置不存在
                .orElseThrow(() -> new DataNotExistException("error.file.storageConfigNotExist"));

        String bucket = selectBucket(storageConfig, platformFileRecord.getAccessType());
        String objectKey = buildObjectKey(platformFileRecord.getPath(), platformFileRecord.getFilename());

        String presignedUrl = s3FileStorageService.generateDownloadPresignedUrl(bucket, objectKey, null);
        return HttpUtil.downloadBytes(presignedUrl);
    }

    /// 服务端直传文件（用于系统生成的文件）
    ///
    /// 适用于服务端生成文件并需要存储的场景，如对账单、报表等。
    /// 自动创建文件记录并上传到私有桶，返回文件名用于访问。
    ///
    /// @param data        文件字节数据
    /// @param fileName    原始文件名
    /// @param contentType 内容类型
    /// @param bizType     业务类型（如 reconcile、report 等）
    /// @return 文件名（用于访问/下载）
    @Transactional(rollbackFor = Exception.class)
    public String uploadServerFile(byte[] data, String fileName, String contentType, String bizType) {
        var storageConfig = s3FileStorageService.getStorageConfig()
                // 文件: 存储配置不存在
                .orElseThrow(() -> new DataNotExistException("error.file.storageConfigNotExist"));

        String fileExtension = FileUtil.extName(fileName);
        String objectKey = generateObjectKey(fileExtension, storageConfig.getBasePath());
        String path = extractPath(objectKey);
        String filename = extractFilename(objectKey);

        s3FileStorageService.upload(data, objectKey, contentType);

        var platformFileRecord = new PlatformFileRecord()
                .setPath(path)
                .setSize((long) data.length)
                .setFilename(filename)
                .setOriginalFilename(fileName)
                .setExt(fileExtension)
                .setContentType(contentType)
                .setAccessType(UploadAccessTypeEnum.PRIVATE.getCode())
                .setBizType(bizType)
                .setStatus(FileUploadStatusEnum.UPLOADED.getCode());

        platformFileRecordManager.save(platformFileRecord);

        log.info("服务端直传文件: fileId={}, filename={}, bizType={}", platformFileRecord.getId(), filename, bizType);
        return filename;
    }

    /// 生成对象Key
    ///
    /// 格式: {basePath}/{yyyy/MM/dd}/{uuid}.{extension}
    /// 例如: platform/2026/04/10/abc123.jpg
    ///
    /// @param fileExtension 文件扩展名
    /// @param basePath      基础存储路径（可选）
    /// @return 对象Key
    private String generateObjectKey(String fileExtension, String basePath) {
        String datePath = OffsetDateTime.now(ZoneOffset.UTC).format(java.time.format.DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        String uuid = IdUtil.fastSimpleUUID();
        String key = datePath + "/" + uuid;
        if (StrUtil.isNotBlank(fileExtension)) {
            key = key + "." + fileExtension;
        }
        if (StrUtil.isNotBlank(basePath)) {
            String normalizedBasePath = StrUtil.removeSuffix(StrUtil.removePrefix(basePath, "/"), "/");
            key = normalizedBasePath + "/" + key;
        }
        return key;
    }

    /// 从对象Key中提取路径部分
    ///
    /// 例如：2026/04/10/abc123.jpg -> /2026/04/10
    ///
    /// @param objectKey 对象Key
    /// @return 路径（以/开头）
    private String extractPath(String objectKey) {
        int lastSlashIndex = objectKey.lastIndexOf('/');
        if (lastSlashIndex > 0) {
            return "/" + objectKey.substring(0, lastSlashIndex);
        }
        return "/";
    }

    /// 从对象Key中提取文件名部分
    ///
    /// 例如：2026/04/10/abc123.jpg -> abc123.jpg
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

    /// 根据路径和文件名构建对象Key
    ///
    /// 例如：path=/2026/04/10, filename=abc123.jpg -> 2026/04/10/abc123.jpg
    ///
    /// @param path     路径（以/开头）
    /// @param filename 文件名
    /// @return 对象Key
    private String buildObjectKey(String path, String filename) {
        if (StrUtil.isBlank(path)) {
            return filename;
        }
        String normalizedPath = path.startsWith("/") ? path.substring(1) : path;
        return normalizedPath + "/" + filename;
    }

    /// 根据访问类型选择存储桶
    ///
    /// @param storageConfig 存储配置
    /// @param accessType    访问类型 (public/private)
    /// @return 存储桶名称
    private String selectBucket(FileStorageConfig storageConfig, String accessType) {
        if (UploadAccessTypeEnum.PUBLIC.getCode().equals(accessType)) {
            return storageConfig.getPublicBucket();
        }
        return storageConfig.getPrivateBucket();
    }
}

