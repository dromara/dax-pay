package cn.daxpay.open.payment.admin.controller.merchant.config;


import cn.daxpay.open.platform.core.code.PermCodes;
import cn.daxpay.open.platform.core.annotation.PermCode;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.result.Result;
import cn.daxpay.open.payment.merchant.param.config.MerchantCredentialParam;
import cn.daxpay.open.payment.merchant.result.config.MerchantCredentialResult;
import cn.daxpay.open.payment.merchant.service.config.MerchantCredentialService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/// # 商户对接配置管理控制器
///
@PermCode(menuCode = PermCodes.Merchant.Credential.MENU)
@Validated
@Tag(name = "商户对接配置管理")
@RestController
@RequestMapping("/admin/merchant/credential")
@RequiredArgsConstructor
public class MerchantCredentialAdminController {

    private final MerchantCredentialService credentialService;

    @PermCode(code = PermCodes.Action.VIEW, nameCn = PermCodes.Merchant.Credential.VIEW_NAME_CN, nameEn = PermCodes.Merchant.Credential.VIEW_NAME_EN)
    @Operation(summary = "根据商户号查询对接配置")
    @GetMapping("/get-by-mch-no")
    public Result<MerchantCredentialResult> findByMchNo(
            @NotBlank(message = "{validation.field.mchNo.notBlank}") String mchNo) {
        return Res.ok(credentialService.findByMchNo(mchNo));
    }

    @PermCode(code = PermCodes.Action.MANAGE, nameCn = PermCodes.Merchant.Credential.MANAGE_NAME_CN, nameEn = PermCodes.Merchant.Credential.MANAGE_NAME_EN)
    @Operation(summary = "更新商户对接配置")
    @PostMapping("/update")
    public Result<Void> update(@RequestBody @Validated MerchantCredentialParam param) {
        credentialService.update(param);
        return Res.ok();
    }
}
