package cn.daxpay.open.payment.merchant.controller.trade;

import cn.daxpay.open.payment.merchant.service.trade.MchOrderCloseService;
import cn.daxpay.open.payment.trade.order.param.OrderCloseParam;
import cn.daxpay.open.platform.core.annotation.PermCode;
import cn.daxpay.open.platform.core.code.PermCodes;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
    @Operation(summary = "关闭订单(网关/普通统一入口)")
    @PostMapping("/close")
    public Result<Void> close(@RequestBody @Valid OrderCloseParam param) {
        mchOrderCloseService.close(param.getContainerId(), param.getTradeType());
        return Res.ok();
    }
}
