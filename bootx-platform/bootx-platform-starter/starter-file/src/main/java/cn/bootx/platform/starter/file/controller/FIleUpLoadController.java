package cn.bootx.platform.starter.file.controller;

import cn.bootx.platform.core.annotation.IgnoreAuth;
import cn.bootx.platform.core.rest.Res;
import cn.bootx.platform.core.rest.param.PageParam;
import cn.bootx.platform.core.rest.result.PageResult;
import cn.bootx.platform.core.rest.result.Result;
import cn.bootx.platform.starter.file.param.UploadFileParam;
import cn.bootx.platform.starter.file.result.UploadFileResult;
import cn.bootx.platform.starter.file.service.FileUploadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件上传
 *
 * @author xxm
 * @since 2022/1/12
 */
@Tag(name = "文件上传")
@RestController
@RequestMapping("/file")
@RequiredArgsConstructor
public class FIleUpLoadController {

    private final FileUploadService uploadService;

    @IgnoreAuth
    @Operation(summary = "分页")
    @GetMapping("/page")
    public Result<PageResult<UploadFileResult>> page(PageParam pageParam, UploadFileParam param) {
        return Res.ok(uploadService.page(pageParam,param));
    }

    @Operation(summary = "获取单条详情")
    @GetMapping("/findById")
    public Result<UploadFileResult> findById(Long id) {
        return Res.ok(uploadService.findById(id));
    }

    @Operation(summary = "删除")
    @PostMapping("/delete")
    public Result<Void> delete(Long id) {
        uploadService.delete(id);
        return Res.ok();
    }

    @IgnoreAuth(login = true)
    @Operation(summary = "上传")
    @PostMapping("/upload")
    public Result<UploadFileResult> local(MultipartFile file, String fileName) {
        return Res.ok(uploadService.upload(file, fileName));
    }

    @IgnoreAuth
    @Operation(summary = "获取文件预览地址(流量会经过后端)")
    @GetMapping("/getFilePreviewUrl")
    public Result<String> getFilePreviewUrl(Long id) {
        return Res.ok(uploadService.getFilePreviewUrl(id));
    }

    @IgnoreAuth
    @Operation(summary = "获取文件预览地址前缀")
    @GetMapping("/getFilePreviewUrlPrefix")
    public Result<String> getFilePreviewUrlPrefix() {
        return Res.ok(uploadService.getFilePreviewUrlPrefix());
    }

    @IgnoreAuth
    @Operation(summary = "获取文件下载地址(流量会经过后端)")
    @GetMapping("/getFileDownloadUrl")
    public Result<String> getFileDownloadUrl(Long id) {
        return Res.ok(uploadService.getFileDownloadUrl(id));
    }

    @IgnoreAuth
    @Operation(summary = "预览文件(流量会经过后端)")
    @GetMapping("/preview/{id}")
    public void preview(@PathVariable Long id, HttpServletResponse response) {
        uploadService.preview(id, response);
    }

    @IgnoreAuth
    @Operation(summary = "下载文件(流量会经过后端)")
    @GetMapping("/download/{id}")
    public ResponseEntity<byte[]> download(@PathVariable Long id) {
        return uploadService.download(id);
    }

}
