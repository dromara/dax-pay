package cn.daxpay.open.payment.admin.controller.masterdata.capability;

import cn.daxpay.open.platform.core.code.PermCodes;
import cn.daxpay.open.payment.masterdata.param.capability.PayCapabilityQuery;
import cn.daxpay.open.payment.masterdata.result.capability.PayCapabilityResult;
import cn.daxpay.open.payment.admin.service.masterdata.capability.PayCapabilityService;
import cn.daxpay.open.platform.core.annotation.PermCode;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import cn.daxpay.open.platform.core.rest.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/// # 支付能力（管理端，只读）
@PermCode(menuCode = PermCodes.Payment.Platform.Capability.MENU)
@Validated
@Tag(name = "支付能力管理")
@RestController
@RequestMapping("/admin/payment/pay-capability")
@RequiredArgsConstructor
public class PayCapabilityController {

    private final PayCapabilityService payCapabilityService;

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "分页查询")
    @GetMapping("/page")
    public Result<PageResult<PayCapabilityResult>> page(PageParam pageParam, PayCapabilityQuery query, String name) {
        return Res.ok(payCapabilityService.page(pageParam, query, name));
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "根据编码查询详情")
    @GetMapping("/get")
    public Result<PayCapabilityResult> findByCode(@NotBlank(message = "{validation.field.code.notBlank}") String code) {
        return Res.ok(payCapabilityService.findByCode(code));
    }
}
