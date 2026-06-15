package org.dromara.daxpay.platform.capability.file.controller;

import org.dromara.daxpay.platform.capability.file.param.FileUploadConfirmParam;
import org.dromara.daxpay.platform.capability.file.param.FileUploadPresignParam;
import org.dromara.daxpay.platform.capability.file.result.FileUploadPresignResult;
import org.dromara.daxpay.platform.capability.file.service.PlatformFileService;
import org.dromara.daxpay.platform.core.annotation.IgnoreAuth;
import org.dromara.daxpay.platform.core.exception.BizException;
import org.dromara.daxpay.platform.core.rest.Res;
import org.dromara.daxpay.platform.core.rest.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/// # 平台文件控制器
///
/// 提供平台文件的上传、访问、下载等功能。
/// 基于S3预签名URL实现前端直传，减少服务器压力。
@Slf4j
@Validated
@Tag(name = "平台文件管理")
@RestController
@RequestMapping("/file/platform")
@RequiredArgsConstructor
public class PlatformFileController {

    private final PlatformFileService platformFileService;

    @Operation(summary = "获取上传预签名URL")
    @PostMapping("/upload/presign")
    public Result<FileUploadPresignResult> getUploadPresignUrl(@RequestBody FileUploadPresignParam param) {
        return Res.ok(platformFileService.getUploadPresignUrl(param));
    }

    @Operation(summary = "确认上传")
    @PostMapping("/upload/confirm")
    public Result<Void> confirmUpload(
            @NotNull(message = "{validation.field.fileId.notNull}") @RequestParam Long fileId,
            @NotNull(message = "{validation.field.objectKey.notBlank}") @RequestParam String objectKey,
            @RequestParam(required = false) String etag) {
        FileUploadConfirmParam param = new FileUploadConfirmParam();
        param.setFileId(fileId);
        param.setObjectKey(objectKey);
        param.setEtag(etag);
        platformFileService.confirmUpload(param);
        return Res.ok();
    }

    /// 根据文件名访问文件（预览）
    /// 直接重定向到文件访问URL
    @IgnoreAuth
    @Operation(summary = "根据文件名访问文件（预览）")
    @GetMapping("/access/{filename}")
    public void accessByFilename(HttpServletResponse response, @PathVariable String filename) {
        try {
            String accessUrl = platformFileService.getAccessUrlByFilename(filename);
            response.sendRedirect(accessUrl);
        } catch (IOException e) {
            log.error("访问文件失败", e);
            response.setStatus(HttpStatus.NOT_FOUND.value());
            // 通用: 文件操作失败
            throw new BizException("error.common.fileOperationFailed", e.getMessage());
        }
    }

    /// 根据文件名下载文件
    /// 直接重定向到下载URL（带Content-Disposition头）
    @IgnoreAuth
    @Operation(summary = "根据文件名下载文件")
    @GetMapping("/download/{filename}")
    public void downloadByFilename(HttpServletResponse response, @PathVariable String filename) {
        try {
            String downloadUrl = platformFileService.getDownloadUrlByFilename(filename);
            response.sendRedirect(downloadUrl);
        } catch (IOException e) {
            log.error("下载文件失败", e);
            response.setStatus(HttpStatus.NOT_FOUND.value());
            // 通用: 文件操作失败
            throw new BizException("error.common.fileOperationFailed", e.getMessage());
        }
    }
}

