package cn.daxpay.open.payment.app.admin.controller.merchant.config;

import cn.daxpay.open.payment.merchant.param.wxverify.WxDomainVerifyParam;
import cn.daxpay.open.payment.merchant.param.wxverify.WxDomainVerifyQuery;
import cn.daxpay.open.payment.merchant.param.wxverify.WxDomainVerifyUploadParam;
import cn.daxpay.open.payment.merchant.result.wxverify.WxDomainVerifyResult;
import cn.daxpay.open.payment.merchant.service.wxverify.WxDomainVerifyService;
import cn.daxpay.open.platform.core.annotation.PermCode;
import cn.daxpay.open.platform.core.code.PermCodes;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import cn.daxpay.open.platform.core.rest.result.Result;
import cn.daxpay.open.platform.core.validation.ValidationGroup;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/// # 平台域名校验文件(运营移动端)
///
/// 管理平台自身的公众号/小程序域名验证，以及收银台等小程序「扫普通链接二维码」规则校验文件
/// （支持 MP_verify_*.txt、随机名 .txt 与 32位hex .html），挂载在支付配置菜单下。
/// 对照运营端 Web [PlatformWxDomainVerifyAdminController]，路径 `/admin/platform/wx-verify`，同源逻辑、同权限码。
/// 与商户级 [AppAdminWxDomainVerifyController] 的差异: 上传走 uploadPlatform（mchNo="0"），列表查询全部（平台 + 所有商户）。
@PermCode(menuCode = PermCodes.Payment.Config.WxDomainVerify.MENU)
@Validated
@Tag(name = "平台域名校验文件(运营移动端)")
@RestController
@RequestMapping("/app-admin/platform/wx-verify")
@RequiredArgsConstructor
public class AppAdminPlatformWxDomainVerifyController {

    private final WxDomainVerifyService wxDomainVerifyService;

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "上传验证文件")
    @PostMapping("/upload")
    public Result<WxDomainVerifyResult> upload(@RequestBody @Validated WxDomainVerifyUploadParam param) {
        return Res.ok(wxDomainVerifyService.uploadPlatform(param));
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "修改验证文件元数据")
    @PostMapping("/update")
    public Result<Void> update(@RequestBody @Validated(ValidationGroup.edit.class) WxDomainVerifyParam param) {
        wxDomainVerifyService.update(param);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "分页查询")
    @GetMapping("/page")
    public Result<PageResult<WxDomainVerifyResult>> page(PageParam pageParam, WxDomainVerifyQuery query) {
        // 平台管理端查询全部（平台 + 所有商户），由前端按需筛选归属/商户号
        return Res.ok(wxDomainVerifyService.page(pageParam, query));
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "详情")
    @GetMapping("/get")
    public Result<WxDomainVerifyResult> findById(@NotNull(message = "{validation.field.id.notNull}") Long id) {
        return Res.ok(wxDomainVerifyService.findById(id));
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "删除")
    @PostMapping("/delete")
    public Result<Void> delete(@NotNull(message = "{validation.field.id.notNull}") Long id) {
        wxDomainVerifyService.delete(id);
        return Res.ok();
    }
}
