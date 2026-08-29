package cn.daxpay.open.payment.app.admin.controller.merchant.config;

import cn.daxpay.open.payment.merchant.param.wxverify.WxDomainVerifyParam;
import cn.daxpay.open.payment.merchant.param.wxverify.WxDomainVerifyQuery;
import cn.daxpay.open.payment.merchant.param.wxverify.WxDomainVerifyUploadParam;
import cn.daxpay.open.payment.merchant.result.wxverify.WxDomainVerifyResult;
import cn.daxpay.open.payment.merchant.service.wxverify.WxDomainVerifyService;
import cn.daxpay.open.platform.core.annotation.PermCode;
import cn.daxpay.open.platform.core.code.PermCodes;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import cn.daxpay.open.platform.core.rest.result.Result;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.validation.ValidationGroup;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/// 小程序管理端-商户微信域名验证文件管理
///
/// 镜像自 admin 版 `WxDomainVerifyAdminController`(路径 /admin/merchant/wx-verify),
/// 同权限码同 Service, 运营在商户工作台代为管理指定商户的验证文件, mchNo 由请求参数指定;
/// 上传走 JSON(fileName + fileContent), 不走 multipart。
@PermCode(menuCode = PermCodes.Merchant.WxDomainVerify.MENU)
@Validated
@Tag(name = "小程序管理端-商户微信域名验证文件")
@RestController
@RequestMapping("/app-admin/merchant/wx-verify")
@RequiredArgsConstructor
public class AppAdminWxDomainVerifyController {

    private final WxDomainVerifyService wxDomainVerifyService;

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "上传验证文件")
    @PostMapping("/upload")
    public Result<WxDomainVerifyResult> upload(@RequestBody @Validated WxDomainVerifyUploadParam param,
                                               @RequestParam("mchNo") @NotBlank(message = "{validation.field.mchNo.notBlank}") String mchNo) {
        return Res.ok(wxDomainVerifyService.upload(param, mchNo));
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
