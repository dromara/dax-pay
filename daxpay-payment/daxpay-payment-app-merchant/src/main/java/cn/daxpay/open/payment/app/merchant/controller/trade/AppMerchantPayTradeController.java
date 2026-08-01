package cn.daxpay.open.payment.app.merchant.controller.trade;

import cn.daxpay.open.payment.app.merchant.service.trade.AppMerchantPayTradeService;
import cn.daxpay.open.payment.trade.order.param.PayTradeQuery;
import cn.daxpay.open.payment.trade.order.result.PayTradeResult;
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

/// # 资金交易凭证(商户移动端)
///
/// 面向商户移动端的资金交易凭证查询。业务编排委托 [AppMerchantPayTradeService]。
@PermCode(menuCode = PermCodes.Trade.Fund.MENU)
@Validated
@Tag(name = "资金交易凭证(商户移动端)")
@RestController
@RequestMapping("/app-mch/order/pay-trade")
@RequiredArgsConstructor
public class AppMerchantPayTradeController {

    private final AppMerchantPayTradeService payTradeService;

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "资金交易凭证分页")
    @GetMapping("/page")
    public Result<PageResult<PayTradeResult>> page(PageParam pageParam, PayTradeQuery query) {
        return Res.ok(payTradeService.page(pageParam, query));
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "根据ID查询资金交易凭证详情")
    @GetMapping("/get-by-id")
    public Result<PayTradeResult> findById(
            @NotNull(message = "{validation.field.id.notNull}") Long id) {
        return Res.ok(payTradeService.findById(id));
    }
}
