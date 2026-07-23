package cn.daxpay.open.payment.admin.controller.trade;

import cn.daxpay.open.platform.core.annotation.PermCode;
import cn.daxpay.open.platform.core.code.PermCodes;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import cn.daxpay.open.platform.core.rest.result.Result;
import cn.daxpay.open.payment.trade.order.param.NormalPayOrderQuery;
import cn.daxpay.open.payment.trade.order.result.NormalPayOrderResult;
import cn.daxpay.open.payment.trade.order.service.NormalPayOrderService;
import cn.daxpay.open.payment.unipay.result.trade.pay.NormalPaySyncResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/// # 普通支付业务单(管理)
///
/// 面向运营后台的业务订单(容器)管理。业务编排委托 core [NormalPayOrderService]。
@PermCode(menuCode = PermCodes.Trade.Order.MENU)
@Validated
@Tag(name = "普通支付业务单(管理)")
@RestController
@RequestMapping("/admin/order/normal-pay")
@RequiredArgsConstructor
public class NormalPayOrderAdminController {

    private final NormalPayOrderService normalPayOrderService;

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

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "同步支付状态")
    @PostMapping("/sync")
    public Result<NormalPaySyncResult> sync(
            @NotNull(message = "{validation.field.id.notNull}") Long id) {
        return Res.ok(normalPayOrderService.sync(id));
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "关闭/撤销订单")
    @PostMapping("/close")
    public Result<Void> close(
            @NotNull(message = "{validation.field.id.notNull}") Long id,
            @RequestParam(defaultValue = "false") boolean useCancel) {
        normalPayOrderService.close(id, useCancel);
        return Res.ok();
    }
}
