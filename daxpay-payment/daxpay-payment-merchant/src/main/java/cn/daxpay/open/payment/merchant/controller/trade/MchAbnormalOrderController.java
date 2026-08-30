package cn.daxpay.open.payment.merchant.controller.trade;

import cn.daxpay.open.payment.merchant.service.trade.MchAbnormalOrderService;
import cn.daxpay.open.payment.trade.abnormal.param.AbnormalOrderQuery;
import cn.daxpay.open.payment.trade.abnormal.result.AbnormalOrderResult;
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

/// # 异常订单(商户端)
///
/// 终态订单收到通道收款证据的台账查询, 只读; 处置动作由运营端操作。
@PermCode(menuCode = PermCodes.Trade.AbnormalOrder.MENU)
@Validated
@Tag(name = "异常订单(商户端)")
@RestController
@RequestMapping("/mch/order/abnormal-order")
@RequiredArgsConstructor
public class MchAbnormalOrderController {

    private final MchAbnormalOrderService mchAbnormalOrderService;

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "异常订单分页")
    @GetMapping("/page")
    public Result<PageResult<AbnormalOrderResult>> page(PageParam pageParam, AbnormalOrderQuery query) {
        return Res.ok(mchAbnormalOrderService.page(pageParam, query));
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "异常订单详情")
    @GetMapping("/get-by-id")
    public Result<AbnormalOrderResult> getById(
            @NotNull(message = "{validation.field.id.notNull}") Long id) {
        return Res.ok(mchAbnormalOrderService.findById(id));
    }
}
