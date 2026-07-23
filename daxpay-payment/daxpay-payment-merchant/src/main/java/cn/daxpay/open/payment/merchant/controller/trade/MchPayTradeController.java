package cn.daxpay.open.payment.merchant.controller.trade;

import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import cn.daxpay.open.platform.core.rest.result.Result;
import cn.daxpay.open.payment.merchant.service.trade.MchPayTradeService;
import cn.daxpay.open.payment.trade.order.param.PayTradeQuery;
import cn.daxpay.open.payment.trade.order.result.PayTradeResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/// # 资金交易凭证(商户端)
///
/// 工作台「最近资金交易」等只读列表；完整订单管理页后续再挂菜单。
@Validated
@Tag(name = "资金交易凭证(商户端)")
@RestController
@RequestMapping("/mch/order/pay-trade")
@RequiredArgsConstructor
public class MchPayTradeController {

    private final MchPayTradeService mchPayTradeService;

    @Operation(summary = "资金交易凭证分页")
    @GetMapping("/page")
    public Result<PageResult<PayTradeResult>> page(PageParam pageParam, PayTradeQuery query) {
        return Res.ok(mchPayTradeService.page(pageParam, query));
    }
}
