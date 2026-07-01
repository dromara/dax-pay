package cn.daxpay.open.payment.merchant.controller.config;

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

/// # 商户API配置控制器
///
@Validated
@Tag(name = "商户API对接配置")
@RestController
@RequestMapping("/merchant/credential")
@RequiredArgsConstructor
public class MerchantCredentialController {
    private final MerchantCredentialService credentialService;

    @Operation(summary = "根据商户号查询")
    @GetMapping("/get-by-mch-no")
    public Result<MerchantCredentialResult> findByMchNo(@NotBlank(message = "{validation.field.mchNo.notBlank}") String mchNo) {
        return Res.ok(credentialService.findByMchNo(mchNo));
    }

    @Operation(summary = "更新商户API配置")
    @PostMapping("/update")
    public Result<Void> update(@RequestBody @Validated MerchantCredentialParam param) {
        credentialService.update(param);
        return Res.ok();
    }
}
