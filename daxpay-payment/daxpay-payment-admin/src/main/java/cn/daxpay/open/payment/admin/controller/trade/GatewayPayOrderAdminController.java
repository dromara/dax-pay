package cn.daxpay.open.payment.admin.controller.trade;

import cn.daxpay.open.payment.gateway.param.GatewayPayOrderQuery;
import cn.daxpay.open.payment.gateway.result.GatewayPayOrderResult;
import cn.daxpay.open.payment.admin.service.trade.GatewayPayOrderAdminService;
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
import org.springframework.web.bind.annotation.*;

/// # 网关支付业务单(管理)
@PermCode(menuCode = PermCodes.Payment.GatewayOrder.MENU)
@Validated
@Tag(name = "网关支付业务单(管理)")
@RestController
@RequestMapping("/admin/order/gateway-pay")
@RequiredArgsConstructor
public class GatewayPayOrderAdminController {

    private final GatewayPayOrderAdminService gatewayPayOrderAdminService;

    @PermCode(code = PermCodes.Action.VIEW, nameCn = "订单查看", nameEn = "Order View")
    @Operation(summary = "网关支付业务单分页")
    @GetMapping("/page")
    public Result<PageResult<GatewayPayOrderResult>> page(PageParam pageParam, GatewayPayOrderQuery query) {
        return Res.ok(gatewayPayOrderAdminService.page(pageParam, query));
    }

    @PermCode(code = PermCodes.Action.VIEW, nameCn = "订单查看", nameEn = "Order View")
    @Operation(summary = "根据ID查询详情")
    @GetMapping("/get-by-id")
    public Result<GatewayPayOrderResult> findById(
            @NotNull(message = "{validation.field.id.notNull}") Long id) {
        return Res.ok(gatewayPayOrderAdminService.findById(id));
    }

    @PermCode(code = PermCodes.Action.MANAGE, nameCn = "订单管理", nameEn = "Order Manage")
    @Operation(summary = "同步支付状态")
    @PostMapping("/sync")
    public Result<NormalPaySyncResult> sync(
            @NotNull(message = "{validation.field.id.notNull}") Long id) {
        return Res.ok(gatewayPayOrderAdminService.sync(id));
    }

    @PermCode(code = PermCodes.Action.MANAGE, nameCn = "订单管理", nameEn = "Order Manage")
    @Operation(summary = "关闭订单")
    @PostMapping("/close")
    public Result<Void> close(
            @NotNull(message = "{validation.field.id.notNull}") Long id,
            @RequestParam(defaultValue = "false") boolean useCancel) {
        gatewayPayOrderAdminService.close(id, useCancel);
        return Res.ok();
    }
}
