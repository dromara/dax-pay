package cn.bootx.platform.daxpay.admin.controller.order;

import cn.bootx.platform.common.core.exception.DataNotExistException;
import cn.bootx.platform.common.core.rest.PageResult;
import cn.bootx.platform.common.core.rest.Res;
import cn.bootx.platform.common.core.rest.ResResult;
import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.daxpay.param.payment.allocation.AllocationStartParam;
import cn.bootx.platform.daxpay.param.payment.pay.PayCloseParam;
import cn.bootx.platform.daxpay.param.payment.pay.PaySyncParam;
import cn.bootx.platform.daxpay.result.pay.SyncResult;
import cn.bootx.platform.daxpay.service.core.order.pay.entity.PayOrder;
import cn.bootx.platform.daxpay.service.core.order.pay.service.PayOrderExtraService;
import cn.bootx.platform.daxpay.service.core.order.pay.service.PayOrderQueryService;
import cn.bootx.platform.daxpay.service.core.payment.allocation.service.AllocationService;
import cn.bootx.platform.daxpay.service.core.payment.close.service.PayCloseService;
import cn.bootx.platform.daxpay.service.core.payment.sync.service.PaySyncService;
import cn.bootx.platform.daxpay.service.dto.order.pay.PayOrderDetailDto;
import cn.bootx.platform.daxpay.service.dto.order.pay.PayOrderDto;
import cn.bootx.platform.daxpay.service.dto.order.pay.PayOrderExtraDto;
import cn.bootx.platform.daxpay.service.param.order.PayOrderQuery;
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
    private final PayOrderExtraService payOrderExtraService;
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
    public ResResult<PayOrderDetailDto> findByOrderNo(String orderNo){
        PayOrderDto order = queryService.findByOrderNo(orderNo)
                .map(PayOrder::toDto)
                .orElseThrow(() -> new DataNotExistException("支付订单不存在"));
        PayOrderDetailDto detailDto=new PayOrderDetailDto();
        detailDto.setPayOrder(order);
        detailDto.setPayOrderExtra(payOrderExtraService.findById(order.getId()));
        return Res.ok(detailDto);
    }

    @Operation(summary = "查询支付订单扩展信息")
    @GetMapping("/getExtraById")
    public ResResult<PayOrderExtraDto> getExtraById(Long id){
        return Res.ok(payOrderExtraService.findById(id));
    }

    @Operation(summary = "同步支付状态")
    @PostMapping("/syncByOrderNo")
    public ResResult<SyncResult> syncById(String orderNo){
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
    @PostMapping("/allocation")
    public ResResult<Void> allocation(String orderNo){
        AllocationStartParam param = new AllocationStartParam();
        param.setOrderNo(orderNo);
        allocationService.allocation(param);
        return Res.ok();
    }
}
