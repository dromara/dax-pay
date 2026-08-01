package cn.daxpay.open.payment.merchant.controller.trade;

import cn.daxpay.open.platform.core.annotation.PermCode;
import cn.daxpay.open.platform.core.code.PermCodes;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import cn.daxpay.open.platform.core.rest.result.Result;
import cn.daxpay.open.payment.merchant.service.trade.MchPayTradeService;
import cn.daxpay.open.payment.trade.order.param.PayTradeQuery;
import cn.daxpay.open.payment.trade.order.result.PayTradeResult;
import cn.daxpay.open.payment.unipay.result.trade.pay.NormalPaySyncResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/// # 资金交易凭证(商户端)
///
/// 业务编排委托 [MchPayTradeService]；强制当前商户过滤。
@PermCode(menuCode = PermCodes.Trade.Fund.MENU)
@Validated
@Tag(name = "资金交易凭证(商户端)")
@RestController
@RequestMapping("/mch/order/pay-trade")
@RequiredArgsConstructor
public class MchPayTradeController {

    private final MchPayTradeService mchPayTradeService;

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "资金交易凭证分页")
    @GetMapping("/page")
    public Result<PageResult<PayTradeResult>> page(PageParam pageParam, PayTradeQuery query) {
        return Res.ok(mchPayTradeService.page(pageParam, query));
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "根据ID查询资金交易凭证详情")
    @GetMapping("/get-by-id")
    public Result<PayTradeResult> findById(
            @NotNull(message = "{validation.field.id.notNull}") Long id) {
        return Res.ok(mchPayTradeService.findById(id));
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "同步支付状态")
    @PostMapping("/sync")
    public Result<NormalPaySyncResult> sync(
            @NotNull(message = "{validation.field.id.notNull}") Long id) {
        return Res.ok(mchPayTradeService.sync(id));
    }
}
