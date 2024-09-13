package cn.daxpay.single.admin.controller.order;

import cn.bootx.platform.common.core.exception.DataNotExistException;
import cn.bootx.platform.common.core.rest.PageResult;
import cn.bootx.platform.common.core.rest.Res;
import cn.bootx.platform.common.core.rest.ResResult;
import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.daxpay.single.core.code.PaymentApiCode;
import cn.daxpay.single.core.param.payment.allocation.AllocationParam;
import cn.daxpay.single.core.param.payment.pay.PayCloseParam;
import cn.daxpay.single.core.param.payment.pay.PaySyncParam;
import cn.daxpay.single.core.result.sync.PaySyncResult;
import cn.daxpay.single.service.annotation.InitPaymentContext;
import cn.daxpay.single.service.core.order.pay.entity.PayOrder;
import cn.daxpay.single.service.core.order.pay.service.PayOrderQueryService;
import cn.daxpay.single.service.core.payment.allocation.service.AllocationService;
import cn.daxpay.single.service.core.payment.close.service.PayCloseService;
import cn.daxpay.single.service.core.payment.sync.service.PaySyncService;
import cn.daxpay.single.service.dto.order.pay.PayOrderDto;
import cn.daxpay.single.service.param.order.PayOrderQuery;
import cn.daxpay.single.core.util.TradeNoGenerateUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    private final PayOrderQueryService queryService;
    private final PayCloseService payCloseService;


    private final PaySyncService paySyncService;
    private final AllocationService allocationService;

    @Operation(summary = "分页查询")
    @GetMapping("/page")
    public ResResult<PageResult<PayOrderDto>> page(PageParam pageParam, PayOrderQuery param){
        return Res.ok(queryService.page(pageParam,param));
    }

    @Operation(summary = "查询订单详情")
    @GetMapping("/findById")
    public ResResult<PayOrderDto> findById(Long id){
        PayOrderDto order = queryService.findById(id)
                .map(PayOrder::toDto)
                .orElseThrow(() -> new DataNotExistException("支付订单不存在"));
        return Res.ok(order);
    }

    @Operation(summary = "查询订单详情")
    @GetMapping("/findByOrderNo")
    public ResResult<PayOrderDto> findByOrderNo(String orderNo){
        PayOrderDto order = queryService.findByOrderNo(orderNo)
                .map(PayOrder::toDto)
                .orElseThrow(() -> new DataNotExistException("支付订单不存在"));
        return Res.ok(order);
    }

    @Operation(summary = "同步支付状态")
    @PostMapping("/syncByOrderNo")
    public ResResult<PaySyncResult> syncById(String orderNo){
        PaySyncParam param = new PaySyncParam();
        param.setOrderNo(orderNo);
        return Res.ok(paySyncService.sync(param));
    }

    @Operation(summary = "关闭支付记录")
    @PostMapping("/close")
    public ResResult<Void> close(String orderNo){
        PayCloseParam param = new PayCloseParam();
        param.setOrderNo(orderNo);
        payCloseService.close(param);
        return Res.ok();
    }

    @Operation(summary = "发起分账")
    @InitPaymentContext(PaymentApiCode.ALLOCATION)
    @PostMapping("/allocation")
    public ResResult<Void> allocation(String orderNo){
        AllocationParam param = new AllocationParam();
        param.setOrderNo(orderNo);
        param.setBizAllocNo(TradeNoGenerateUtil.allocation());
        allocationService.allocation(param);
        return Res.ok();
    }

    @Operation(summary = "查询金额汇总")
    @GetMapping("/getTotalAmount")
    public ResResult<Integer> getTotalAmount(PayOrderQuery param){
        return Res.ok(queryService.getTotalAmount(param));
    }
}
