package cn.daxpay.open.payment.app.admin.controller.merchant.openapp;

import cn.daxpay.open.payment.douyin.param.platform.DyPlatformAppCapabilityBatchParam;
import cn.daxpay.open.payment.douyin.result.DyCapabilityOption;
import cn.daxpay.open.payment.douyin.result.platform.DyPlatformAppCapabilityResult;
import cn.daxpay.open.payment.douyin.service.platform.DyPlatformAppCapabilityService;
import cn.daxpay.open.platform.core.annotation.PermCode;
import cn.daxpay.open.platform.core.code.PermCodes;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/// # 小程序管理端-平台抖音应用默认能力绑定
///
/// 镜像自 admin 版 `DyPlatformAppCapabilityController`(路径 /admin/douyin/platform-app-capability),
/// 端点语义与权限码完全一致, 供小程序端服务商产品「默认支付应用」配置使用;
/// 与微信侧 [AppAdminWxPlatformAppCapabilityController] 同构。
@PermCode(menuCode = PermCodes.Payment.Douyin.PlatformApp.MENU)
@Validated
@Tag(name = "小程序管理端-平台抖音应用默认能力绑定")
@RestController
@RequestMapping("/app-admin/douyin/platform-app-capability")
@RequiredArgsConstructor
public class AppAdminDyPlatformAppCapabilityController {

    private final DyPlatformAppCapabilityService dyPlatformAppCapabilityService;

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "按产品查询能力绑定列表")
    @GetMapping("/list-by-product")
    public Result<List<DyPlatformAppCapabilityResult>> listByProduct(
            @NotBlank(message = "{validation.field.product.notBlank}")
            @Parameter(description = "支付产品编码") String product) {
        return Res.ok(dyPlatformAppCapabilityService.listByProduct(product));
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "按产品全量保存能力绑定")
    @PostMapping("/save-batch")
    public Result<Void> saveBatch(@RequestBody @Validated DyPlatformAppCapabilityBatchParam param) {
        dyPlatformAppCapabilityService.saveBatch(param);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "按产品查询可绑定能力候选")
    @GetMapping("/list-supported-capabilities")
    public Result<List<DyCapabilityOption>> listSupportedCapabilities(
            @NotBlank(message = "{validation.field.product.notBlank}")
            @Parameter(description = "支付产品编码") String product) {
        return Res.ok(dyPlatformAppCapabilityService.listSupportedCapabilities(product));
    }
}
