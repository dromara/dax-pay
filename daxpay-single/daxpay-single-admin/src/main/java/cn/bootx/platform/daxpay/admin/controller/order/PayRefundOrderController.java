package cn.bootx.platform.daxpay.admin.controller.order;

import cn.bootx.platform.common.core.rest.PageResult;
import cn.bootx.platform.common.core.rest.Res;
import cn.bootx.platform.common.core.rest.ResResult;
import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.common.spring.util.WebServletUtil;
import cn.bootx.platform.daxpay.param.pay.RefundParam;
import cn.bootx.platform.daxpay.service.core.order.refund.service.PayRefundOrderService;
import cn.bootx.platform.daxpay.service.core.payment.refund.service.PayRefundService;
import cn.bootx.platform.daxpay.service.dto.order.refund.PayRefundOrderDto;
import cn.bootx.platform.daxpay.service.param.order.PayOrderRefundParam;
import cn.bootx.platform.daxpay.service.param.order.PayRefundOrderQuery;
import cn.hutool.core.util.IdUtil;
import cn.hutool.extra.servlet.ServletUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * 支付退款控制器
 * @author xxm
 * @since 2024/1/9
 */
@Tag(name = "支付退款控制器")
@RestController
@RequestMapping("/order/refund")
@RequiredArgsConstructor
public class PayRefundOrderController {
    private final PayRefundOrderService payRefundOrderService;
    private final PayRefundService payRefundService;


    @Operation(summary = "分页查询")
    @GetMapping("/page")
    public ResResult<PageResult<PayRefundOrderDto>> page(PageParam pageParam, PayRefundOrderQuery query){
        return Res.ok(payRefundOrderService.page(pageParam, query));
    }

    @Operation(summary = "查询单条")
    @GetMapping("/findById")
    public ResResult<PayRefundOrderDto> findById(Long paymentId){
        return Res.ok(payRefundOrderService.findById(paymentId));
    }

    @Operation(summary = "发起退款")
    @PostMapping("/refund")
    public ResResult<Void> refund(@RequestBody PayOrderRefundParam param){

        String ip = Optional.ofNullable(WebServletUtil.getRequest())
                .map(ServletUtil::getClientIP)
                .orElse("未知");

        RefundParam refundParam = new RefundParam();
        refundParam.setRefundNo(IdUtil.getSnowflakeNextIdStr());
        refundParam.setPaymentId(param.getPaymentId());
        refundParam.setRefundChannels(param.getRefundChannels());
        refundParam.setReqTime(LocalDateTime.now());
        refundParam.setClientIp(ip);
        payRefundService.refund(refundParam);
        return Res.ok();
    }
}
