package cn.bootx.platform.daxpay.admin.controller.order;

import cn.bootx.platform.common.core.exception.DataNotExistException;
import cn.bootx.platform.common.core.rest.PageResult;
import cn.bootx.platform.common.core.rest.Res;
import cn.bootx.platform.common.core.rest.ResResult;
import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.daxpay.service.core.order.pay.entity.PayOrder;
import cn.bootx.platform.daxpay.service.core.order.pay.service.PayOrderChannelService;
import cn.bootx.platform.daxpay.service.core.order.pay.service.PayOrderExtraService;
import cn.bootx.platform.daxpay.service.core.order.pay.service.PayOrderService;
import cn.bootx.platform.daxpay.service.dto.order.pay.PayOrderChannelDto;
import cn.bootx.platform.daxpay.service.dto.order.pay.PayOrderDto;
import cn.bootx.platform.daxpay.service.dto.order.pay.PayOrderExtraDto;
import cn.bootx.platform.daxpay.service.param.order.PayOrderQuery;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 支付订单控制器
 * @author xxm
 * @since 2024/1/9
 */
@Tag(name = "支付订单控制器")
@RestController
@RequestMapping("/order/pay")
@RequiredArgsConstructor
public class PayOrderController {
    private final PayOrderService payOrderService;
    private final PayOrderExtraService payOrderExtraService;
    private final PayOrderChannelService payOrderChannelService;

    @Operation(summary = "分页查询")
    @GetMapping("/page")
    public ResResult<PageResult<PayOrderDto>> page(PageParam pageParam, PayOrderQuery param){
        return Res.ok(payOrderService.page(pageParam,param));
    }

    @Operation(summary = "查询订单详情")
    @GetMapping("/findById")
    public ResResult<PayOrderDto> findById(Long id){
        PayOrderDto order = payOrderService.findById(id)
                .map(PayOrder::toDto)
                .orElseThrow(() -> new DataNotExistException("支付订单不存在"));
        return Res.ok(order);
    }

    @Operation(summary = "查询支付订单扩展信息")
    @GetMapping("/getExtraById")
    public ResResult<PayOrderExtraDto> getExtraById(Long id){
        return Res.ok(payOrderExtraService.findById(id));
    }

    @Operation(summary = "查询支付订单关联支付通道")
    @GetMapping("/getChannels")
    public ResResult<List<PayOrderChannelDto>> getChannels(Long paymentId){
        return Res.ok(payOrderChannelService.findAllByPaymentId(paymentId));
    }
}
