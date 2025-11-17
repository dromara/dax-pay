package org.dromara.daxpay.payment.merchant.controller.config;

import cn.bootx.platform.core.annotation.ClientCode;
import cn.bootx.platform.core.annotation.RequestGroup;
import cn.bootx.platform.core.annotation.RequestPath;
import cn.bootx.platform.core.rest.Res;
import cn.bootx.platform.core.rest.result.Result;
import org.dromara.daxpay.payment.common.code.DaxPayCode;
import org.dromara.daxpay.payment.merchant.param.config.MerchantCredentialParam;
import org.dromara.daxpay.payment.merchant.result.config.MerchantCredentialResult;
import org.dromara.daxpay.payment.merchant.service.config.MerchantCredentialService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 商户API配置控制器
 * @author xxm
 * @since 2025/9/13
 */
@Validated
@ClientCode({DaxPayCode.Client.ADMIN,DaxPayCode.Client.MERCHANT})
@RequestGroup(groupCode = "MerchantCredential", groupName = "商户API配置", moduleCode = "merchant")
@Tag(name = "商户API对接配置")
@RestController
@RequestMapping("/merchant/credential")
@RequiredArgsConstructor
public class MerchantCredentialController {
    private final MerchantCredentialService credentialService;

    @RequestPath("根据商户号查询")
    @Operation(summary = "根据商户号查询")
    @GetMapping("/findByMchNo")
    public Result<MerchantCredentialResult> findByMchNo(@NotBlank(message = "商户号不能为空") String mchNo) {
        return Res.ok(credentialService.findByMchNo(mchNo));
    }

    @RequestPath("更新商户API配置")
    @Operation(summary = "更新商户API配置")
    @PostMapping("/update")
    public Result<Void> update(@RequestBody @Validated MerchantCredentialParam param) {
        credentialService.update(param);
        return Res.ok();
    }


}
