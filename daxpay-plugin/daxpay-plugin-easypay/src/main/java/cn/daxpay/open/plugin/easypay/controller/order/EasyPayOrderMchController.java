package cn.daxpay.open.plugin.easypay.controller.order;

import cn.daxpay.open.payment.unipay.result.trade.pay.NormalPaySyncResult;
import cn.daxpay.open.platform.core.annotation.PermCode;
import cn.daxpay.open.platform.core.code.PermCodes;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import cn.daxpay.open.platform.core.rest.result.Result;
import cn.daxpay.open.plugin.easypay.param.order.EasyPayOrderQuery;
import cn.daxpay.open.plugin.easypay.result.order.EasyPayOrderResult;
import cn.daxpay.open.plugin.easypay.service.order.EasyPayOrderMchQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/// # 易支付协议订单(商户端)
///
/// 业务编排委托 [EasyPayOrderMchQueryService]；强制当前商户过滤。
/// 生命周期回写钩子位于 [cn.daxpay.open.plugin.easypay.service.order.EasyPayOrderService], 与本控制器分离。
@PermCode(menuCode = PermCodes.Plugin.EasyPayOrder.MENU)
@Validated
@Tag(name = "易支付协议订单(商户端)")
@RestController
@RequestMapping("/merchant/easypay/order")
@RequiredArgsConstructor
public class EasyPayOrderMchController {

    private final EasyPayOrderMchQueryService easyPayOrderMchQueryService;

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "易支付订单分页")
    @GetMapping("/page")
    public Result<PageResult<EasyPayOrderResult>> page(PageParam pageParam, EasyPayOrderQuery query) {
        return Res.ok(easyPayOrderMchQueryService.page(pageParam, query));
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "根据ID查询易支付订单详情")
    @GetMapping("/get-by-id")
    public Result<EasyPayOrderResult> findById(
            @NotNull(message = "{validation.field.id.notNull}") Long id) {
        return Res.ok(easyPayOrderMchQueryService.findById(id));
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "同步易支付订单状态")
    @PostMapping("/sync")
    public Result<NormalPaySyncResult> sync(
            @NotNull(message = "{validation.field.id.notNull}") Long id) {
        return Res.ok(easyPayOrderMchQueryService.sync(id));
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "关闭/撤销易支付订单")
    @PostMapping("/close")
    public Result<Void> close(
            @NotNull(message = "{validation.field.id.notNull}") Long id) {
        easyPayOrderMchQueryService.close(id);
        return Res.ok();
    }
}
