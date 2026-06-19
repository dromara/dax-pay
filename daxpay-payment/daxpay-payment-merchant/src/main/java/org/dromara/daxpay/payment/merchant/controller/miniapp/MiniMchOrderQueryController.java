package org.dromara.daxpay.payment.merchant.controller.miniapp;

import org.dromara.daxpay.platform.core.rest.Res;
import org.dromara.daxpay.platform.core.rest.result.PageResult;
import org.dromara.daxpay.platform.core.rest.result.Result;
import org.dromara.daxpay.payment.old.pay.result.order.pay.PayOrderVo;
import org.dromara.daxpay.payment.merchant.param.miniapp.order.MiniPayOrderQuery;
import org.dromara.daxpay.payment.merchant.service.miniapp.MiniMchOrderQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/// # 小程序订单查询
///
@Validated
@Tag(name = "小程序订单查询")
@RestController
@RequestMapping("/mini/mch/order/query")
@RequiredArgsConstructor
public class MiniMchOrderQueryController {
    private final MiniMchOrderQueryService miniAppOrderQueryService;

    @Operation(summary = "支付订单分页")
    @PostMapping("/page-by-pay")
    public Result<PageResult<PayOrderVo>> pageByPay(@RequestBody MiniPayOrderQuery query) {
        return Res.ok(miniAppOrderQueryService.pageByPay(query));
    }

    @Operation(summary = "支付订单详情")
    @GetMapping("/find-pay-order-by-id")
    public Result<PayOrderVo> findPayOrderById(@NotNull(message = "{validation.field.id.notNull}") Long id) {
        return Res.ok(miniAppOrderQueryService.findPayOrderById(id));
    }

    @Operation(summary = "根据编号查询支付订单详情")
    @GetMapping("/find-pay-order-by-no")
    public Result<PayOrderVo> findPayOrderByNo(@NotBlank(message = "{validation.field.orderNo.notBlank}") String orderNo,
        @NotBlank(message = "{validation.field.appId.notBlank}") String appId) {
        return Res.ok(miniAppOrderQueryService.findPayOrderByNo(orderNo,appId));
    }

}
