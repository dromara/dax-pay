package cn.bootx.platform.starter.file.controller;

import cn.bootx.platform.starter.file.param.FileUploadRequestParams;
import cn.bootx.platform.starter.file.result.FileUploadParamsResult;
import cn.bootx.platform.starter.file.service.OssService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@Tag(name = "对象存储")
@RequestMapping("/oss")
@AllArgsConstructor
public class OssController {


    private final OssService ossService;

    @Operation(summary = "获取上传参数")
    @PostMapping("/getUploadParams")
    public FileUploadParamsResult getUploadParams(@RequestBody @Valid FileUploadRequestParams params) {
        return ossService.getUploadParams(params);
    }

    @Operation(summary = "文件预览/文件下载")
    @GetMapping("/download/{attachName}")
    public void download(HttpServletResponse httpServletResponse, @Schema(description = "附件名") @PathVariable("attachName") String attachName) {
        ossService.download(httpServletResponse, attachName);
    }
}
