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
import cn.daxpay.open.plugin.easypay.service.order.EasyPayOrderQueryService;
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

/// # 易支付协议订单（管理）
///
/// 运营端 / 商户端共用。同步/关单透传内核 [cn.daxpay.open.payment.trade.order.service.NormalPayOrderService];
/// 生命周期回写钩子位于 [cn.daxpay.open.plugin.easypay.service.order.EasyPayOrderService], 与本控制器分离。
@PermCode(menuCode = PermCodes.Plugin.EasyPayOrder.MENU)
@Validated
@Tag(name = "易支付协议订单")
@RestController
@RequestMapping({"/admin/easypay/order", "/merchant/easypay/order"})
@RequiredArgsConstructor
public class EasyPayOrderController {

    private final EasyPayOrderQueryService easyPayOrderQueryService;

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "易支付订单分页")
    @GetMapping("/page")
    public Result<PageResult<EasyPayOrderResult>> page(PageParam pageParam, EasyPayOrderQuery query) {
        return Res.ok(easyPayOrderQueryService.page(pageParam, query));
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "根据ID查询易支付订单详情")
    @GetMapping("/get-by-id")
    public Result<EasyPayOrderResult> findById(
            @NotNull(message = "{validation.field.id.notNull}") Long id) {
        return Res.ok(easyPayOrderQueryService.findById(id));
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "同步易支付订单状态")
    @PostMapping("/sync")
    public Result<NormalPaySyncResult> sync(
            @NotNull(message = "{validation.field.id.notNull}") Long id) {
        return Res.ok(easyPayOrderQueryService.sync(id));
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "关闭/撤销易支付订单")
    @PostMapping("/close")
    public Result<Void> close(
            @NotNull(message = "{validation.field.id.notNull}") Long id,
            @RequestParam(defaultValue = "false") boolean useCancel) {
        easyPayOrderQueryService.close(id, useCancel);
        return Res.ok();
    }
}
