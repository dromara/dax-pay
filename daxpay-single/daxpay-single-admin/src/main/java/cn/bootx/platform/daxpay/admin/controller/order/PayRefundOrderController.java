package cn.bootx.platform.daxpay.admin.controller.order;

import cn.bootx.platform.common.core.rest.PageResult;
import cn.bootx.platform.common.core.rest.Res;
import cn.bootx.platform.common.core.rest.ResResult;
import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.daxpay.service.core.order.refund.service.PayRefundOrderService;
import cn.bootx.platform.daxpay.service.dto.order.refund.PayRefundOrderDto;
import cn.bootx.platform.daxpay.service.param.order.PayRefundOrderQuery;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
