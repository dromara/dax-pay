package org.dromara.daxpay.payment.admin.controller.masterdata.provider;

import org.dromara.daxpay.payment.masterdata.constants.provider.result.PayProviderGroupResult;
import org.dromara.daxpay.payment.masterdata.constants.provider.result.PayProviderMethodResult;
import org.dromara.daxpay.payment.masterdata.constants.provider.service.PayProviderService;
import org.dromara.daxpay.platform.core.annotation.PermCode;
import org.dromara.daxpay.platform.core.rest.Res;
import org.dromara.daxpay.platform.core.rest.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/// # 支付渠道（管理端）
///
@PermCode(menuCode = "payment:platform:provider")
@Validated
@Tag(name = "支付渠道")
@RestController
@RequestMapping("/admin/payment/pay-provider")
@RequiredArgsConstructor
public class PayProviderController {

    private final PayProviderService payProviderService;

    @PermCode(code = "view", nameCn = "品牌目录查看", nameEn = "Brand Method Directory View")
    @Operation(summary = "按支付渠道分组查询支付方式列表")
    @GetMapping("/list-by-provider")
    public Result<List<PayProviderGroupResult>> listByProvider() {
        return Res.ok(payProviderService.listByProvider());
    }

    @PermCode(code = "view", nameCn = "品牌目录查看", nameEn = "Brand Method Directory View")
    @Operation(summary = "查询单条支付渠道项")
    @GetMapping("/get")
    public Result<PayProviderMethodResult> get(
            @NotBlank(message = "{validation.field.provider.notBlank}") String provider,
            @NotBlank(message = "{validation.field.method.notBlank}") String method) {
        return Res.ok(payProviderService.get(provider, method));
    }
}
