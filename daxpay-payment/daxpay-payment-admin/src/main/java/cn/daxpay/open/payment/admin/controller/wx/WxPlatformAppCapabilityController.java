package cn.daxpay.open.payment.admin.controller.wx;

import cn.daxpay.open.payment.wx.param.WxPlatformAppCapabilityBatchParam;
import cn.daxpay.open.payment.wx.result.WxCapabilityOption;
import cn.daxpay.open.payment.wx.result.WxPlatformAppCapabilityResult;
import cn.daxpay.open.payment.wx.service.WxPlatformAppCapabilityService;
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
import org.springframework.web.bind.annotation.*;

import java.util.List;

/// # 平台微信应用默认能力绑定（运营端，按支付产品隔离）
///
@PermCode(menuCode = PermCodes.Payment.Wx.PlatformApp.MENU)
@Validated
@Tag(name = "平台微信应用默认能力绑定")
@RestController
@RequestMapping("/admin/wx/platform-app-capability")
@RequiredArgsConstructor
public class WxPlatformAppCapabilityController {

    private final WxPlatformAppCapabilityService wxPlatformAppCapabilityService;

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "按产品查询能力绑定列表")
    @GetMapping("/list-by-product")
    public Result<List<WxPlatformAppCapabilityResult>> listByProduct(
            @NotBlank(message = "{validation.field.product.notBlank}")
            @Parameter(description = "支付产品编码") @RequestParam String product) {
        return Res.ok(wxPlatformAppCapabilityService.listByProduct(product));
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "按产品全量保存能力绑定")
    @PostMapping("/save-batch")
    public Result<Void> saveBatch(@RequestBody @Validated WxPlatformAppCapabilityBatchParam param) {
        wxPlatformAppCapabilityService.saveBatch(param);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "按产品查询可绑定能力候选")
    @GetMapping("/list-supported-capabilities")
    public Result<List<WxCapabilityOption>> listSupportedCapabilities(
            @NotBlank(message = "{validation.field.product.notBlank}")
            @Parameter(description = "支付产品编码") @RequestParam String product) {
        return Res.ok(wxPlatformAppCapabilityService.listSupportedCapabilities(product));
    }
}
