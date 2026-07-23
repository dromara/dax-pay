package cn.daxpay.open.payment.merchant.controller.wxverify;

import cn.daxpay.open.payment.common.context.PaymentContext;
import cn.daxpay.open.payment.merchant.param.wxverify.WxDomainVerifyParam;
import cn.daxpay.open.payment.merchant.param.wxverify.WxDomainVerifyQuery;
import cn.daxpay.open.payment.merchant.param.wxverify.WxDomainVerifyUploadParam;
import cn.daxpay.open.payment.merchant.result.wxverify.WxDomainVerifyResult;
import cn.daxpay.open.payment.merchant.service.wxverify.WxDomainVerifyService;
import cn.daxpay.open.platform.core.code.CommonCode;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.exception.config.ConfigErrorException;
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

import java.util.Objects;

/// # 微信域名验证文件（商户端）
///
/// 对照运营端 [WxDomainVerifyAdminController]，路径 `/mch/wx-verify`。
/// 上传/分页强制使用 PaymentContext 商户号，不接受 URL mchNo。
@Validated
@Tag(name = "微信域名验证文件(商户端)")
@RestController
@RequestMapping("/mch/wx-verify")
@RequiredArgsConstructor
public class MchWxDomainVerifyController {

    private final WxDomainVerifyService wxDomainVerifyService;
    private final PaymentContext paymentContext;

    private String requireMchNo() {
        String mchNo = paymentContext.getMchNo();
        if (mchNo == null || mchNo.isBlank()) {
            throw new BizInfoException(CommonCode.FAIL_CODE, "pay.error.assist.mchContextMissing");
        }
        return mchNo;
    }

    private void assertOwned(WxDomainVerifyResult result) {
        if (!Objects.equals(result.getMchNo(), requireMchNo())) {
            // 微信域名验证文件不属于当前商户
            throw new ConfigErrorException("error.payment.merchant.wxVerifyNoMatch");
        }
    }

    @Operation(summary = "上传验证文件")
    @PostMapping("/upload")
    public Result<WxDomainVerifyResult> upload(@RequestBody @Validated WxDomainVerifyUploadParam param) {
        // mchNo 取自上下文，不走 query
        return Res.ok(wxDomainVerifyService.upload(param, requireMchNo()));
    }

    @Operation(summary = "修改验证文件元数据")
    @PostMapping("/update")
    public Result<Void> update(@RequestBody @Validated(ValidationGroup.edit.class) WxDomainVerifyParam param) {
        this.assertOwned(wxDomainVerifyService.findById(param.getId()));
        wxDomainVerifyService.update(param);
        return Res.ok();
    }

    @Operation(summary = "分页查询")
    @GetMapping("/page")
    public Result<PageResult<WxDomainVerifyResult>> page(PageParam pageParam, WxDomainVerifyQuery query) {
        query.setMchNo(requireMchNo());
        return Res.ok(wxDomainVerifyService.page(pageParam, query));
    }

    @Operation(summary = "详情")
    @GetMapping("/get")
    public Result<WxDomainVerifyResult> findById(@NotNull(message = "{validation.field.id.notNull}") Long id) {
        WxDomainVerifyResult result = wxDomainVerifyService.findById(id);
        this.assertOwned(result);
        return Res.ok(result);
    }

    @Operation(summary = "删除")
    @PostMapping("/delete")
    public Result<Void> delete(@NotNull(message = "{validation.field.id.notNull}") Long id) {
        this.assertOwned(wxDomainVerifyService.findById(id));
        wxDomainVerifyService.delete(id);
        return Res.ok();
    }
}
