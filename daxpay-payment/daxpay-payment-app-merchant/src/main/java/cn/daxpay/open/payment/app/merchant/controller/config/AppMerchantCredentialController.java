package cn.daxpay.open.payment.app.merchant.controller.config;

import cn.daxpay.open.payment.app.merchant.service.config.AppMerchantCredentialService;
import cn.daxpay.open.payment.common.context.PaymentContext;
import cn.daxpay.open.payment.merchant.param.config.MchMerchantCredentialParam;
import cn.daxpay.open.payment.merchant.param.config.MerchantCredentialParam;
import cn.daxpay.open.payment.merchant.result.config.MerchantCredentialResult;
import cn.daxpay.open.platform.core.annotation.PermCode;
import cn.daxpay.open.platform.core.code.CommonCode;
import cn.daxpay.open.platform.core.code.PermCodes;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

/// # 商户API对接配置(商户移动端)
///
/// 面向商户移动端的 API 凭证管理。业务编排委托 [AppMerchantCredentialService]。
@PermCode(menuCode = PermCodes.Merchant.Credential.MENU)
@Validated
@Tag(name = "商户API对接配置(商户移动端)")
@RestController
@RequestMapping("/app-mch/credential")
@RequiredArgsConstructor
public class AppMerchantCredentialController {

    private final AppMerchantCredentialService credentialService;
    private final PaymentContext paymentContext;

    /// 当前登录商户号（上下文必有；缺则视为会话异常）
    private String requireMchNo() {
        String mchNo = paymentContext.getMchNo();
        if (Objects.isNull(mchNo) || mchNo.isBlank()) {
            // 商户上下文缺失
            throw new BizInfoException(CommonCode.FAIL_CODE, "pay.error.assist.mchContextMissing");
        }
        return mchNo;
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "根据商户号查询")
    @GetMapping("/get-by-mch-no")
    public Result<MerchantCredentialResult> findByMchNo(
            @NotBlank(message = "{validation.field.mchNo.notBlank}") String mchNo) {
        return Res.ok(credentialService.findByMchNo(mchNo));
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "更新商户API配置")
    @PostMapping("/update")
    public Result<Void> update(@RequestBody @Validated MchMerchantCredentialParam param) {
        // 商户号强制取自登录上下文(防越权)
        MerchantCredentialParam saveParam = new MerchantCredentialParam()
                .setMchNo(requireMchNo())
                .setPublicKey(param.getPublicKey())
                .setSecretKey(param.getSecretKey());
        credentialService.update(saveParam);
        return Res.ok();
    }
}
