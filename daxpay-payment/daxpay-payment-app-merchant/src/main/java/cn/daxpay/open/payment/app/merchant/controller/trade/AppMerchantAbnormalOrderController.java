package cn.daxpay.open.payment.app.merchant.controller.trade;

import cn.daxpay.open.payment.app.merchant.service.trade.AppMerchantAbnormalOrderService;
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

/// # 异常订单(商户移动端)
///
/// 终态订单收到通道收款证据的查询入口, 只读。处置(确认成功/忽略)为运营端责任, 商户端不开放。
/// 业务编排委托 [AppMerchantAbnormalOrderService]。
@PermCode(menuCode = PermCodes.Trade.AbnormalOrder.MENU)
@Validated
@Tag(name = "异常订单(商户移动端)")
@RestController
@RequestMapping("/app-mch/order/abnormal-order")
@RequiredArgsConstructor
public class AppMerchantAbnormalOrderController {

    private final AppMerchantAbnormalOrderService abnormalOrderService;

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "异常订单分页")
    @GetMapping("/page")
    public Result<PageResult<AbnormalOrderResult>> page(PageParam pageParam, AbnormalOrderQuery query) {
        return Res.ok(abnormalOrderService.page(pageParam, query));
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "根据ID查询异常订单详情")
    @GetMapping("/get-by-id")
    public Result<AbnormalOrderResult> findById(
            @NotNull(message = "{validation.field.id.notNull}") Long id) {
        return Res.ok(abnormalOrderService.findById(id));
    }
}
