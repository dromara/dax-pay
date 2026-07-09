package cn.daxpay.open.payment.merchant.controller.admin.wxverify;

import cn.daxpay.open.payment.merchant.param.wxverify.WxDomainVerifyParam;
import cn.daxpay.open.payment.merchant.param.wxverify.WxDomainVerifyQuery;
import cn.daxpay.open.payment.merchant.param.wxverify.WxDomainVerifyUploadParam;
import cn.daxpay.open.payment.merchant.result.wxverify.WxDomainVerifyResult;
import cn.daxpay.open.payment.merchant.service.wxverify.WxDomainVerifyService;
import cn.daxpay.open.platform.core.annotation.PermCode;
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
import org.springframework.web.bind.annotation.*;

/// # 平台微信域名验证文件（运营端，支付配置）
///
/// 管理平台自身的公众号/小程序验证文件，挂载在支付配置菜单下。
/// 上传走 JSON（fileName + fileContent），不走 multipart。
@PermCode(menuCode = "payment:config:wx_verify")
@Validated
@Tag(name = "平台微信域名验证文件")
@RestController
@RequestMapping("/admin/platform/wx-verify")
@RequiredArgsConstructor
public class PlatformWxDomainVerifyAdminController {

    private final WxDomainVerifyService wxDomainVerifyService;

    @PermCode(code = "manage", nameCn = "管理", nameEn = "Manage")
    @Operation(summary = "上传验证文件")
    @PostMapping("/upload")
    public Result<WxDomainVerifyResult> upload(@RequestBody @Validated WxDomainVerifyUploadParam param) {
        return Res.ok(wxDomainVerifyService.uploadPlatform(param));
    }

    @PermCode(code = "manage", nameCn = "管理", nameEn = "Manage")
    @Operation(summary = "修改验证文件元数据")
    @PostMapping("/update")
    public Result<Void> update(@RequestBody @Validated(ValidationGroup.edit.class) WxDomainVerifyParam param) {
        wxDomainVerifyService.update(param);
        return Res.ok();
    }

    @PermCode(code = "view", nameCn = "查看", nameEn = "View")
    @Operation(summary = "分页查询")
    @GetMapping("/page")
    public Result<PageResult<WxDomainVerifyResult>> page(PageParam pageParam, WxDomainVerifyQuery query) {
        // 平台管理端查询全部（平台 + 所有商户），由前端按需筛选归属/商户号
        return Res.ok(wxDomainVerifyService.page(pageParam, query));
    }

    @PermCode(code = "view", nameCn = "查看", nameEn = "View")
    @Operation(summary = "详情")
    @GetMapping("/get")
    public Result<WxDomainVerifyResult> findById(@NotNull(message = "{validation.field.id.notNull}") Long id) {
        return Res.ok(wxDomainVerifyService.findById(id));
    }

    @PermCode(code = "manage", nameCn = "管理", nameEn = "Manage")
    @Operation(summary = "删除")
    @PostMapping("/delete")
    public Result<Void> delete(@NotNull(message = "{validation.field.id.notNull}") Long id) {
        wxDomainVerifyService.delete(id);
        return Res.ok();
    }

}
