package cn.bootx.platform.starter.file.controller;

import cn.bootx.platform.core.annotation.IgnoreAuth;
import cn.bootx.platform.core.annotation.OperateLog;
import cn.bootx.platform.core.annotation.RequestGroup;
import cn.bootx.platform.core.annotation.RequestPath;
import cn.bootx.platform.core.rest.Res;
import cn.bootx.platform.core.rest.param.PageParam;
import cn.bootx.platform.core.rest.result.PageResult;
import cn.bootx.platform.core.rest.result.Result;
import cn.bootx.platform.starter.file.param.UploadFileQuery;
import cn.bootx.platform.starter.file.result.UploadFileResult;
import cn.bootx.platform.starter.file.service.FileUploadService;
import com.fhs.core.trans.anno.TransMethodResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件上传
 *
 * @author xxm
 * @since 2022/1/12
 */
@Validated
@Tag(name = "文件上传")
@RestController
@RequestGroup(groupCode = "FIleUpLoad", groupName = "文件上传管理", moduleCode = "starter")
@RequestMapping("/file")
@RequiredArgsConstructor
public class FIleUpLoadController {

    private final FileUploadService uploadService;

    @TransMethodResult
    @RequestPath("分页")
    @Operation(summary = "分页")
    @GetMapping("/page")
    public Result<PageResult<UploadFileResult>> page(PageParam pageParam, UploadFileQuery param) {
        return Res.ok(uploadService.page(pageParam,param));
    }

    @TransMethodResult
    @RequestPath("获取单条详情")
    @Operation(summary = "获取单条详情")
    @GetMapping("/findById")
    public Result<UploadFileResult> findById(@NotNull(message = "主键不可为空") Long id) {
        return Res.ok(uploadService.findById(id));
    }

    @IgnoreAuth
    @Operation(summary = "根据URL获取单条详情")
    @GetMapping("/findByUrl")
    public Result<UploadFileResult> findById(@NotBlank(message = "文件URL不可为空") String url) {
        return Res.ok(uploadService.findById(url));
    }

    @Operation(summary = "删除")
    @RequestPath("删除")
    @PostMapping("/delete")
    @OperateLog(title = "删除文件", businessType = OperateLog.BusinessType.DELETE, saveParam = true)
    public Result<Void> delete(@NotNull(message = "主键不可为空") Long id) {
        uploadService.delete(id);
        return Res.ok();
    }

    @IgnoreAuth
    @Operation(summary = "上传")
    @PostMapping("/upload")
    public Result<UploadFileResult> local(@RequestPart MultipartFile file, String fileName) {
        return Res.ok(uploadService.upload(file, fileName));
    }

    @IgnoreAuth
    @Operation(summary = "获取文件预览地址前缀(流量会经过后端)")
    @GetMapping("/forward/getFilePreviewUrlPrefix")
    public Result<String> getFilePreviewUrlPrefix() {
        return Res.ok(uploadService.getServerFilePreviewUrlPrefix());
    }

    @IgnoreAuth
    @Operation(summary = "预览文件(流量会经过后端)")
    @GetMapping("/forward/preview/{id}")
    public void preview(@PathVariable Long id, HttpServletResponse response) {
        uploadService.preview(id, response);
    }

    @IgnoreAuth
    @Operation(summary = "下载文件(流量会经过后端)")
    @GetMapping("/forward/download/{id}")
    public ResponseEntity<byte[]> download(@PathVariable Long id) {
        return uploadService.download(id);
    }
}
