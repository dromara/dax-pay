package cn.daxpay.open.payment.merchant.controller.wx;

import cn.daxpay.open.payment.common.context.PaymentContext;
import cn.daxpay.open.payment.wx.convert.WxMchAppAuthConfigConvert;
import cn.daxpay.open.payment.wx.param.WxMchAppAuthConfigParam;
import cn.daxpay.open.payment.wx.param.WxMchAppParam;
import cn.daxpay.open.payment.wx.result.WxMchAppAuthConfigResult;
import cn.daxpay.open.payment.wx.result.WxMchAppResult;
import cn.daxpay.open.payment.wx.service.WxMchAppAuthConfigService;
import cn.daxpay.open.payment.wx.service.WxMchAppService;
import cn.daxpay.open.platform.core.annotation.PermCode;
import cn.daxpay.open.platform.core.code.CommonCode;
import cn.daxpay.open.platform.core.code.PermCodes;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.exception.config.ConfigErrorException;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.result.Result;
import cn.daxpay.open.platform.core.util.ValidationUtil;
import cn.daxpay.open.platform.core.validation.ValidationGroup;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

/// # 商户微信应用管理（商户端）
///
/// 对照运营端 [WxMchAppController]，路径 `/mch/wx/mch-app`。
/// 商户号一律取自 [PaymentContext]，写操作覆盖请求中的 mchNo，防越权。
@PermCode(menuCode = PermCodes.Payment.Wx.MchApp.MENU)
@Validated
@Tag(name = "商户微信应用管理(商户端)")
@RestController
@RequestMapping("/mch/wx/mch-app")
@RequiredArgsConstructor
public class MchWxMchAppController {

    private final WxMchAppService wxMchAppService;
    private final WxMchAppAuthConfigService wxMchAppAuthConfigService;
    private final PaymentContext paymentContext;

    /// 当前登录商户号
    private String requireMchNo() {
        String mchNo = paymentContext.getMchNo();
        if (mchNo == null || mchNo.isBlank()) {
            // 商户上下文缺失
            throw new BizInfoException(CommonCode.FAIL_CODE, "pay.error.assist.mchContextMissing");
        }
        return mchNo;
    }

    /// 校验资源归属当前商户
    private void assertOwned(WxMchAppResult result) {
        if (!Objects.equals(result.getMchNo(), this.requireMchNo())) {
            // 商户微信应用不属于当前商户
            throw new ConfigErrorException("error.payment.wx.mchAppNotFound");
        }
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "当前商户微信应用列表")
    @GetMapping("/list-all")
    public Result<List<WxMchAppResult>> listAll() {
        return Res.ok(wxMchAppService.listByMchNo(this.requireMchNo()));
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "查询应用详情")
    @GetMapping("/find-by-id")
    public Result<WxMchAppResult> findById(
            @NotNull(message = "{validation.field.id.notNull}") Long id) {
        WxMchAppResult result = wxMchAppService.findById(id);
        this.assertOwned(result);
        return Res.ok(result);
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "微信应用AppId是否已存在")
    @GetMapping("/exists-wx-app-id")
    public Result<Boolean> existsWxAppId(
            @NotBlank(message = "{validation.field.wxAppId.notBlank}") String wxAppId) {
        return Res.ok(wxMchAppService.existsWxAppId(this.requireMchNo(), wxAppId, null));
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "微信应用AppId是否已存在(排除自身)")
    @GetMapping("/exists-wx-app-id-not-id")
    public Result<Boolean> existsWxAppIdNotId(
            @NotBlank(message = "{validation.field.wxAppId.notBlank}") String wxAppId,
            @NotNull(message = "{validation.field.id.notNull}") Long id) {
        return Res.ok(wxMchAppService.existsWxAppId(this.requireMchNo(), wxAppId, id));
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "新增商户微信应用")
    @PostMapping("/add")
    public Result<Void> add(@RequestBody WxMchAppParam param) {
        // 强制当前商户号，忽略客户端传入（防越权）
        param.setMchNo(this.requireMchNo());
        ValidationUtil.validateParam(param, ValidationGroup.add.class);
        wxMchAppService.add(param);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "修改商户微信应用")
    @PostMapping("/update")
    public Result<Void> update(@RequestBody WxMchAppParam param) {
        this.assertOwned(wxMchAppService.findById(param.getId()));
        // 强制当前商户号，忽略客户端传入（防越权）
        param.setMchNo(this.requireMchNo());
        ValidationUtil.validateParam(param, ValidationGroup.edit.class);
        wxMchAppService.update(param);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "删除商户微信应用")
    @PostMapping("/delete")
    public Result<Void> delete(@NotNull(message = "{validation.field.id.notNull}") Long id) {
        this.assertOwned(wxMchAppService.findById(id));
        wxMchAppService.delete(id);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "查询应用授权认证配置")
    @GetMapping("/find-auth-config-by-app-id")
    public Result<WxMchAppAuthConfigResult> findAuthConfigByAppId(
            @NotNull(message = "{validation.field.wxMchAppId.notNull}") Long wxMchAppId) {
        this.assertOwned(wxMchAppService.findById(wxMchAppId));
        var config = wxMchAppAuthConfigService.findByWxMchAppId(wxMchAppId);
        return Res.ok(WxMchAppAuthConfigConvert.CONVERT.toResult(config));
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "保存应用授权认证配置")
    @PostMapping("/save-auth-config")
    public Result<Void> saveAuthConfig(@RequestBody @Validated WxMchAppAuthConfigParam param) {
        this.assertOwned(wxMchAppService.findById(param.getWxMchAppId()));
        wxMchAppAuthConfigService.save(param);
        return Res.ok();
    }
}
