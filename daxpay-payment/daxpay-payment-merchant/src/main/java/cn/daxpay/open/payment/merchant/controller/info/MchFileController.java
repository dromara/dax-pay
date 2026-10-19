package cn.daxpay.open.payment.merchant.controller.info;

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

/// # 平台文件直传(商户 Web 端)
///
/// 透传 [PlatformFileService] 的预签名直传两段式接口, 供商户 Web 端(BUploadImage 链路)直传对象存储。
/// 原前端直连运营端 [/file/platform/upload/*]（完整码 system:file:manage, 挂 admin 端菜单）,
/// 商户端角色绑不上恒 403, 故在 merchant:file 域下单独开路; 运营端不受影响。
/// 文件访问地址解析走 /file/platform/access-urls（登录即可用）, 不在此重复建设。
@Validated
@PermCode(menuCode = PermCodes.Merchant.File.MENU)
@Tag(name = "平台文件直传(商户Web端)")
@RestController
@RequestMapping("/mch/file")
@RequiredArgsConstructor
public class MchFileController {

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
