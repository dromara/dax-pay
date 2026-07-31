package cn.daxpay.open.plugin.easypay.controller.order;

import cn.daxpay.open.platform.core.annotation.PermCode;
import cn.daxpay.open.platform.core.code.PermCodes;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import cn.daxpay.open.platform.core.rest.result.Result;
import cn.daxpay.open.plugin.easypay.param.order.EasyPayRefundOrderQuery;
import cn.daxpay.open.plugin.easypay.result.order.EasyPayRefundOrderResult;
import cn.daxpay.open.plugin.easypay.service.order.EasyPayRefundOrderAdminQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/// # 易支付协议退款订单(运营端)
///
/// 业务编排委托 [EasyPayRefundOrderAdminQueryService]；跨商户查询并翻译商户名称。
/// 生命周期钩子(双写/成功回写)位于 [cn.daxpay.open.plugin.easypay.service.order.EasyPayRefundOrderService], 与本控制器分离。
@PermCode(menuCode = PermCodes.Plugin.EasyPayRefund.MENU)
@Validated
@Tag(name = "易支付协议退款订单(运营端)")
@RestController
@RequestMapping("/admin/easypay/refund")
@RequiredArgsConstructor
public class EasyPayRefundOrderAdminController {

    private final EasyPayRefundOrderAdminQueryService easyPayRefundOrderAdminQueryService;

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "易支付退款订单分页")
    @GetMapping("/page")
    public Result<PageResult<EasyPayRefundOrderResult>> page(PageParam pageParam, EasyPayRefundOrderQuery query) {
        return Res.ok(easyPayRefundOrderAdminQueryService.page(pageParam, query));
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "根据ID查询易支付退款订单详情")
    @GetMapping("/get-by-id")
    public Result<EasyPayRefundOrderResult> findById(
            @NotNull(message = "{validation.field.id.notNull}") Long id) {
        return Res.ok(easyPayRefundOrderAdminQueryService.findById(id));
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "同步易支付退款状态")
    @PostMapping("/sync")
    public Result<EasyPayRefundOrderResult> sync(
            @NotNull(message = "{validation.field.id.notNull}") Long id) {
        return Res.ok(easyPayRefundOrderAdminQueryService.sync(id));
    }
}
