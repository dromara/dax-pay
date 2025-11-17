package org.dromara.daxpay.payment.miniapp.controller;

import cn.bootx.platform.core.annotation.ClientCode;
import cn.bootx.platform.core.annotation.RequestGroup;
import cn.bootx.platform.core.annotation.RequestPath;
import cn.bootx.platform.core.rest.Res;
import cn.bootx.platform.core.rest.result.PageResult;
import cn.bootx.platform.core.rest.result.Result;
import org.dromara.daxpay.payment.common.code.DaxPayCode;
import org.dromara.daxpay.payment.miniapp.param.order.MiniPayOrderQuery;
import org.dromara.daxpay.payment.miniapp.param.order.MiniRefundOrderQuery;
import org.dromara.daxpay.payment.miniapp.service.MiniMchOrderQueryService;
import org.dromara.daxpay.payment.pay.result.order.pay.PayOrderVo;
import org.dromara.daxpay.payment.pay.result.order.refund.RefundOrderVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.dromara.core.trans.anno.TransMethodResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 小程序订单查询
 * @author xxm
 * @since 2025/4/22
 */
@Validated
@ClientCode(DaxPayCode.Client.MERCHANT)
@Tag(name = "小程序订单查询")
@RestController
@RequestMapping("/mini/mch/order/query")
@RequestGroup(groupCode = "MiniMchOrderQuery", groupName = "小程序订单查询", moduleCode = "MiniMch")
@RequiredArgsConstructor
public class MiniMchOrderQueryController {
    private final MiniMchOrderQueryService miniAppOrderQueryService;

    @RequestPath("支付订单分页")
    @Operation(summary = "支付订单分页")
    @PostMapping("/pageByPay")
    public Result<PageResult<PayOrderVo>> pageByPay(@RequestBody MiniPayOrderQuery query) {
        return Res.ok(miniAppOrderQueryService.pageByPay(query));
    }

    @TransMethodResult
    @RequestPath("支付订单详情")
    @Operation(summary = "支付订单详情")
    @GetMapping("/findPayOrderById")
    public Result<PayOrderVo> findPayOrderById(@NotNull(message = "主键不可为空") Long id) {
        return Res.ok(miniAppOrderQueryService.findPayOrderById(id));
    }

    @TransMethodResult
    @RequestPath("根据编号查询支付订单详情")
    @Operation(summary = "根据编号查询支付订单详情")
    @GetMapping("/findPayOrderByNo")
    public Result<PayOrderVo> findPayOrderByNo(@NotBlank(message = "订单编号不可为空") String orderNo,
                                               @NotBlank(message = "商户应用号不可为空") String appId) {
        return Res.ok(miniAppOrderQueryService.findPayOrderByNo(orderNo,appId));
    }

    @RequestPath("退款订单分页")
    @Operation(summary = "退款订单分页")
    @PostMapping("/pageByRefund")
    public Result<PageResult<RefundOrderVo>> pageByRefund(@RequestBody MiniRefundOrderQuery query) {
        return Res.ok(miniAppOrderQueryService.pageByRefund(query));
    }

    @TransMethodResult
    @RequestPath("退款订单详情")
    @Operation(summary = "退款订单详情")
    @GetMapping("/findRefundOrderById")
    public Result<RefundOrderVo> findRefundOrderById(@NotNull(message = "主键不可为空") Long id) {
        return Res.ok(miniAppOrderQueryService.findRefundOrderById(id));
    }
}
