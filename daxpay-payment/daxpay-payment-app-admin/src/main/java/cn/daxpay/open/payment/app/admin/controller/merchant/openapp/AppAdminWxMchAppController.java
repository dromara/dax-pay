package cn.daxpay.open.payment.app.admin.controller.merchant.openapp;

import cn.daxpay.open.payment.wx.result.merchant.WxMchAppResult;
import cn.daxpay.open.payment.wx.service.merchant.WxMchAppService;
import cn.daxpay.open.platform.core.annotation.PermCode;
import cn.daxpay.open.platform.core.code.PermCodes;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/// 小程序管理端-商户微信开放应用(只读)
///
/// 镜像自 admin 版 `WxMchAppController`(路径 /admin/wx/mch-app), 仅保留小程序端
/// 支付应用(微信)只读列表用到的端点; 新增/编辑/删除等写操作请在 PC 端完成。
@PermCode(menuCode = PermCodes.Payment.Wx.MchApp.MENU)
@Validated
@Tag(name = "小程序管理端-商户微信应用")
@RestController
@RequestMapping("/app-admin/wx/mch-app")
@RequiredArgsConstructor
public class AppAdminWxMchAppController {

    private final WxMchAppService wxMchAppService;

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "按商户号查询微信应用列表")
    @GetMapping("/list-by-mch-no")
    public Result<List<WxMchAppResult>> listByMchNo(
            @NotBlank(message = "{validation.field.mchNo.notBlank}") String mchNo) {
        return Res.ok(wxMchAppService.listByMchNo(mchNo));
    }
}
