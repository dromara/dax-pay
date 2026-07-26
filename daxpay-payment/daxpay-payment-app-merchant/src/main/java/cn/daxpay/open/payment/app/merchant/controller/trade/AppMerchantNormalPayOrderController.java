package cn.daxpay.open.payment.app.merchant.controller.trade;

import cn.daxpay.open.payment.app.merchant.service.trade.AppMerchantNormalPayOrderService;
import cn.daxpay.open.payment.trade.order.param.NormalPayOrderQuery;
import cn.daxpay.open.payment.trade.order.result.NormalPayOrderResult;
import cn.daxpay.open.platform.core.annotation.PermCode;
import cn.daxpay.open.platform.core.code.PermCodes;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import cn.daxpay.open.platform.core.rest.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/// # 普通支付业务单(商户移动端)
///
/// 面向商户移动端的业务订单查询。业务编排委托 [AppMerchantNormalPayOrderService]。
@PermCode(menuCode = PermCodes.Trade.Order.MENU)
@Validated
@Tag(name = "普通支付业务单(商户移动端)")
@RestController
@RequestMapping("/app-merchant/order/normal-pay")
@RequiredArgsConstructor
public class AppMerchantNormalPayOrderController {

    private final AppMerchantNormalPayOrderService normalPayOrderService;

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "普通支付业务单分页")
    @GetMapping("/page")
    public Result<PageResult<NormalPayOrderResult>> page(PageParam pageParam, NormalPayOrderQuery query) {
        return Res.ok(normalPayOrderService.page(pageParam, query));
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "根据ID查询普通支付业务单详情")
    @GetMapping("/get-by-id")
    public Result<NormalPayOrderResult> findById(
            @NotNull(message = "{validation.field.id.notNull}") Long id) {
        return Res.ok(normalPayOrderService.findById(id));
    }
}
