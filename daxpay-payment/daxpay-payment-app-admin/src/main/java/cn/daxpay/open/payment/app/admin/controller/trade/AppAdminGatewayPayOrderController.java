package cn.daxpay.open.payment.app.admin.controller.trade;

import cn.daxpay.open.payment.app.admin.service.trade.AppAdminGatewayPayOrderService;
import cn.daxpay.open.payment.trade.order.param.GatewayPayOrderQuery;
import cn.daxpay.open.payment.trade.order.result.GatewayPayOrderResult;
import cn.daxpay.open.payment.unipay.result.trade.pay.NormalPaySyncResult;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/// # 网关支付业务单(运营移动端)
@PermCode(menuCode = PermCodes.Trade.GatewayOrder.MENU)
@Validated
@Tag(name = "网关支付业务单(运营移动端)")
@RestController
@RequestMapping("/app-admin/order/gateway-pay")
@RequiredArgsConstructor
public class AppAdminGatewayPayOrderController {

    private final AppAdminGatewayPayOrderService gatewayPayOrderService;

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "网关支付业务单分页")
    @GetMapping("/page")
    public Result<PageResult<GatewayPayOrderResult>> page(PageParam pageParam, GatewayPayOrderQuery query) {
        return Res.ok(gatewayPayOrderService.page(pageParam, query));
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "根据ID查询详情")
    @GetMapping("/get-by-id")
    public Result<GatewayPayOrderResult> findById(
            @NotNull(message = "{validation.field.id.notNull}") Long id) {
        return Res.ok(gatewayPayOrderService.findById(id));
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "同步支付状态")
    @PostMapping("/sync")
    public Result<NormalPaySyncResult> sync(
            @NotNull(message = "{validation.field.id.notNull}") Long id) {
        return Res.ok(gatewayPayOrderService.sync(id));
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "关闭订单")
    @PostMapping("/close")
    public Result<Void> close(
            @NotNull(message = "{validation.field.id.notNull}") Long id,
            @RequestParam(defaultValue = "false") boolean useCancel) {
        gatewayPayOrderService.close(id, useCancel);
        return Res.ok();
    }
}
