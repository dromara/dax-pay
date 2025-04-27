package cn.bootx.platform.starter.file.service;

import cn.bootx.platform.starter.file.configuration.OssProperties;
import cn.bootx.platform.starter.file.param.FileUploadRequestParams;
import cn.bootx.platform.starter.file.result.FileUploadParamsResult;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.dromara.x.file.storage.core.FileStorageService;
import org.dromara.x.file.storage.core.constant.Constant;
import org.dromara.x.file.storage.core.presigned.GeneratePresignedUrlResult;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Date;

/**
 * @author yinxucun
 */
@Service
@Slf4j
public class OssService {

    private final OssProperties ossProperties;

    private final FileStorageService fileStorageService;

    private static final Instant expirationTime = new Date().toInstant().atZone(ZoneId.of("Asia/Shanghai"))
            .plusMinutes(10).toInstant();

    public OssService(OssProperties ossProperties, FileStorageService fileStorageService) {
        this.ossProperties = ossProperties;
        this.fileStorageService = fileStorageService;
        log.info("初始化OSS配置:{}", ossProperties);
    }


    public FileUploadParamsResult getUploadParams(FileUploadRequestParams params) {
        return this.createSignedUrlForStringPut(params);
    }

    public void download(HttpServletResponse httpServletResponse, String attachName) {
        GeneratePresignedUrlResult downloadResult = fileStorageService
                .generatePresignedUrl()
                .setPath(ossProperties.getFilePath()) // 文件路径
                .setFilename(attachName) // 文件名，也可以换成缩略图的文件名
                .setMethod(Constant.GeneratePresignedUrl.Method.GET) // 签名方法
                .setExpiration(Date.from(expirationTime)) // 过期时间 10 分钟
                .putResponseHeaders(
                        Constant.Metadata.CONTENT_DISPOSITION, "attachment;filename=" + attachName)
                .generatePresignedUrl();
        try {
            httpServletResponse.sendRedirect(downloadResult.getUrl());
        } catch (IOException e) {
            log.error("下载文件失败", e);
            httpServletResponse.setStatus(HttpStatus.NOT_FOUND.value());
            throw new RuntimeException(e);
        }

    }

    private FileUploadParamsResult createSignedUrlForStringPut(FileUploadRequestParams params) {
        String fileExtension = FileUtil.extName(params.getFileName());
        String attachName = IdUtil.fastSimpleUUID() + (StringUtils.isNotBlank(fileExtension) ? "." + fileExtension : "");
        Date expirationTime = new Date(System.currentTimeMillis() + 600 * 1000);
        GeneratePresignedUrlResult uploadResult = fileStorageService
                .generatePresignedUrl()
                .setPath(ossProperties.getFilePath()) // 设置路径
                .setFilename(attachName) // 设置保存的文件名
                .setMethod(Constant.GeneratePresignedUrl.Method.PUT)    // 签名方法
                .setExpiration(expirationTime)   // 设置过期时间 10 分钟
                .putHeaders(Constant.Metadata.CONTENT_TYPE, params.getMediaType())
                .putHeaders(Constant.Metadata.CONTENT_LENGTH, String.valueOf(params.getFileSize()))
                .generatePresignedUrl();
        log.info("expirationTime:{},attachName:{}", DateUtil.format(expirationTime, "yyyy-MM-dd HH:mm:ss"), attachName);
        log.info("生成上传预签名 URL 结果：{}", uploadResult);
        FileUploadParamsResult result = new FileUploadParamsResult();
        result.setUrl(uploadResult.getUrl());
        result.setHeaders(uploadResult.getHeaders());
        return result;
    }
}
