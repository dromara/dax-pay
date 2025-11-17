package cn.bootx.platform.starter.file.controller;

import cn.bootx.platform.core.annotation.IgnoreAuth;
import cn.bootx.platform.core.annotation.OperateLog;
import cn.bootx.platform.core.annotation.RequestGroup;
import cn.bootx.platform.core.annotation.RequestPath;
import cn.bootx.platform.core.rest.Res;
import cn.bootx.platform.core.rest.param.PageParam;
import cn.bootx.platform.core.rest.result.PageResult;
import cn.bootx.platform.core.rest.result.Result;
import cn.bootx.platform.starter.file.param.FileUploadRequestParams;
import cn.bootx.platform.starter.file.param.UploadFileInfoParam;
import cn.bootx.platform.starter.file.param.UploadFileQuery;
import cn.bootx.platform.starter.file.result.FileUploadParamsResult;
import cn.bootx.platform.starter.file.result.UploadFileResult;
import cn.bootx.platform.starter.file.service.FileUploadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.dromara.core.trans.anno.TransMethodResult;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

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
        return Res.ok(uploadService.findByUrl(id));
    }

    @IgnoreAuth
    @Operation(summary = "根据URL获取单条详情")
    @GetMapping("/findByUrl")
    public Result<UploadFileResult> findById(@NotBlank(message = "文件URL不可为空") String url) {
        return Res.ok(uploadService.findByUrl(url));
    }

    @Operation(summary = "删除")
    @RequestPath("删除")
    @PostMapping("/delete")
    @OperateLog(title = "删除文件", businessType = OperateLog.BusinessType.DELETE, saveParam = true)
    public Result<Void> delete(@NotNull(message = "主键不可为空") Long id) {
        uploadService.delete(id);
        return Res.ok();
    }

    @IgnoreAuth(login = true)
    @Operation(summary = "下载文件")
    @GetMapping("/downloadByServer")
    public ResponseEntity<byte[]> downloadByServer(String attachName) {
        var bytes = uploadService.downloadAndCheck(attachName);
        // 设置header信息
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", URLEncoder.encode(attachName, StandardCharsets.UTF_8));
        return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
    }

    @IgnoreAuth(login = true)
    @Operation(summary = "获取前端直传参数")
    @PostMapping("/getUploadParams")
    public Result<FileUploadParamsResult> getUploadParams(@RequestBody @Valid FileUploadRequestParams params) {
        return Res.ok(uploadService.getUploadParams(params));
    }

    @IgnoreAuth(login = true)
    @Operation(summary = "前端直传文件信息保存")
    @PostMapping("/saveFileInfo")
    public Result<Void> saveFileInfo(@RequestBody @Valid UploadFileInfoParam param) {
        uploadService.saveFileInfo(param);
        return Res.ok();
    }

    @IgnoreAuth
    @Operation(summary = "前端直传文件预览/下载(不需要登录)")
    @GetMapping("/download/{attachName}")
    public void download(HttpServletResponse httpServletResponse, @Schema(description = "附件名") @PathVariable("attachName") String attachName) {
        uploadService.ossDownload(httpServletResponse, attachName);
    }
}
