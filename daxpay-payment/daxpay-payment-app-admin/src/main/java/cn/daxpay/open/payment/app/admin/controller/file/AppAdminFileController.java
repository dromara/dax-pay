package cn.daxpay.open.payment.app.admin.controller.file;

import cn.daxpay.open.platform.capability.file.param.FileUploadConfirmParam;
import cn.daxpay.open.platform.capability.file.param.FileUploadPresignParam;
import cn.daxpay.open.platform.capability.file.result.FileUploadPresignResult;
import cn.daxpay.open.platform.capability.file.service.PlatformFileService;
import cn.daxpay.open.platform.core.annotation.PermCode;
import cn.daxpay.open.platform.core.code.PermCodes;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/// # 平台文件直传(运营移动端)
///
/// 透传 [PlatformFileService] 的预签名直传两段式接口, 供运营 APP/小程序直传对象存储:
/// 先取 PUT 预签名URL, 前端直传完成后回调确认, 与 Web 运营端 [/file/platform/upload/*] 同一链路。
/// 文件访问/下载复用 [/file/platform/access|download/{filename}] 匿名302端点, 不重复建设。
@Validated
@PermCode(menuCode = PermCodes.System.File.MENU)
@Tag(name = "平台文件直传(运营移动端)")
@RestController
@RequestMapping("/app-admin/file")
@RequiredArgsConstructor
public class AppAdminFileController {

    private final PlatformFileService platformFileService;

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "获取上传预签名URL")
    @PostMapping("/upload/presign")
    public Result<FileUploadPresignResult> uploadPresign(@RequestBody FileUploadPresignParam param) {
        return Res.ok(platformFileService.getUploadPresignUrl(param));
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "确认上传")
    @PostMapping("/upload/confirm")
    public Result<Void> uploadConfirm(@RequestBody @Valid FileUploadConfirmParam param) {
        platformFileService.confirmUpload(param);
        return Res.ok();
    }
}
