package cn.bootx.platform.starter.file.controller;

import cn.bootx.platform.common.core.annotation.IgnoreAuth;
import cn.bootx.platform.common.core.rest.PageResult;
import cn.bootx.platform.common.core.rest.Res;
import cn.bootx.platform.common.core.rest.ResResult;
import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.starter.file.UploadFileParam;
import cn.bootx.platform.starter.file.dto.UploadFileDto;
import cn.bootx.platform.starter.file.service.FileUploadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 文件上传
 *
 * @author xxm
 * @since 2022/1/12
 */
@IgnoreAuth
@Tag(name = "文件上传")
@RestController
@RequestMapping("/file")
@RequiredArgsConstructor
public class FIleUpLoadController {

    private final FileUploadService uploadService;

    @IgnoreAuth(ignore = false)
    @Operation(summary = "分页")
    @GetMapping("/page")
    public ResResult<PageResult<UploadFileDto>> page(PageParam pageParam, UploadFileParam param) {
        return Res.ok(uploadService.page(pageParam,param));
    }

    @IgnoreAuth(ignore = false)
    @Operation(summary = "获取单条详情")
    @GetMapping("/findById")
    public ResResult<UploadFileDto> findById(Long id) {
        return Res.ok(uploadService.findById(id));
    }

    @Operation(summary = "删除")
    @DeleteMapping("/delete")
    public ResResult<Void> delete(Long id) {
        uploadService.delete(id);
        return Res.ok();
    }

    @IgnoreAuth(ignore = false, login = true)
    @Operation(summary = "上传")
    @PostMapping("/upload")
    public ResResult<UploadFileDto> local(MultipartFile file, String fileName) throws IOException {
        return Res.ok(uploadService.upload(file, fileName));
    }

    @Operation(summary = "获取文件预览地址(流量会经过后端)")
    @GetMapping("getFilePreviewUrl")
    public ResResult<String> getFilePreviewUrl(Long id) {
        return Res.ok(uploadService.getFilePreviewUrl(id));
    }

    @Operation(summary = "获取文件预览地址前缀")
    @GetMapping("getFilePreviewUrlPrefix")
    public ResResult<String> getFilePreviewUrlPrefix() {
        return Res.ok(uploadService.getFilePreviewUrlPrefix());
    }

    @Operation(summary = "获取文件下载地址(流量会经过后端)")
    @GetMapping("getFileDownloadUrl")
    public ResResult<String> getFileDownloadUrl(Long id) {
        return Res.ok(uploadService.getFileDownloadUrl(id));
    }

    @Operation(summary = "预览文件(流量会经过后端)")
    @GetMapping("/preview/{id}")
    public void preview(@PathVariable Long id, HttpServletResponse response) {
        uploadService.preview(id, response);
    }

    @Operation(summary = "下载文件(流量会经过后端)")
    @GetMapping("/download/{id}")
    public ResponseEntity<byte[]> download(@PathVariable Long id) {
        return uploadService.download(id);
    }

}
