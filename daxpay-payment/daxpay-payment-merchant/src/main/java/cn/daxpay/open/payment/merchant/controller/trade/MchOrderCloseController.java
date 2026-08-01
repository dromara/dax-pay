package cn.daxpay.open.payment.merchant.controller.trade;

import cn.daxpay.open.payment.merchant.service.trade.MchOrderCloseService;
import cn.daxpay.open.platform.core.annotation.PermCode;
import cn.daxpay.open.platform.core.code.PermCodes;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/// # 订单关闭(统一入口, 商户)
///
/// 收归网关支付/普通支付的关闭入口, 前端按 type 区分; 权限由路由层统一校验 trade:order:manage。
@Validated
@Tag(name = "订单关闭(统一入口)")
@RestController
@RequestMapping("/mch/order")
@RequiredArgsConstructor
public class MchOrderCloseController {

    private final MchOrderCloseService mchOrderCloseService;

    @PermCode(menuCode = PermCodes.Trade.Order.MENU, code = PermCodes.Action.MANAGE)
    @Operation(summary = "关闭/撤销订单(网关/普通统一入口)")
    @PostMapping("/close")
    public Result<Void> close(
            @NotNull(message = "{validation.field.id.notNull}") Long id,
            @NotBlank String type,
            @RequestParam(defaultValue = "false") boolean useCancel) {
        mchOrderCloseService.close(id, type, useCancel);
        return Res.ok();
    }
}
