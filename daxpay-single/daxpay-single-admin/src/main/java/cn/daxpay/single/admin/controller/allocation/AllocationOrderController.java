package cn.daxpay.single.admin.controller.allocation;

import cn.bootx.platform.common.core.rest.PageResult;
import cn.bootx.platform.common.core.rest.Res;
import cn.bootx.platform.common.core.rest.ResResult;
import cn.bootx.platform.common.core.rest.dto.LabelValue;
import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.daxpay.single.param.payment.allocation.AllocationStartParam;
import cn.daxpay.single.param.payment.allocation.AllocationSyncParam;
import cn.daxpay.single.param.payment.allocation.AllocationFinishParam;
import cn.daxpay.single.service.core.order.allocation.service.AllocationOrderService;
import cn.daxpay.single.service.core.payment.allocation.service.AllocationService;
import cn.daxpay.single.service.dto.order.allocation.AllocationOrderDetailDto;
import cn.daxpay.single.service.dto.order.allocation.AllocationOrderDto;
import cn.daxpay.single.service.param.order.AllocationOrderQuery;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 分账订单控制器
 * @author xxm
 * @since 2024/4/7
 */
@Tag(name = "分账订单控制器")
@RestController
@RequestMapping("/order/allocation")
@RequiredArgsConstructor
public class AllocationOrderController {

    private final AllocationOrderService allocationOrderService;

    private final AllocationService allocationService;

    @Operation(summary = "分页")
    @GetMapping("/page")
    public ResResult<PageResult<AllocationOrderDto>> page(PageParam pageParam, AllocationOrderQuery param){
        return Res.ok(allocationOrderService.page(pageParam,param));
    }

    @Operation(summary = "分账明细列表")
    @GetMapping("/detail/findAll")
    public ResResult<List<AllocationOrderDetailDto>> findDetailsByOrderId(Long orderId){
        return Res.ok(allocationOrderService.findDetailsByOrderId(orderId));
    }

    @Operation(summary = "查询详情")
    @GetMapping("/findById")
    public ResResult<AllocationOrderDto> findById(Long id){
        return Res.ok(allocationOrderService.findById(id));
    }


    @Operation(summary = "查询明细详情")
    @GetMapping("/detail/findById")
    public ResResult<AllocationOrderDetailDto> findDetailById(Long id){
        return Res.ok(allocationOrderService.findDetailById(id));
    }

    @Operation(summary = "获取可以分账的通道")
    @GetMapping("/findChannels")
    public ResResult<List<LabelValue>> findChannels(){
        return Res.ok(allocationOrderService.findChannels());
    }

    @Operation(summary = "同步分账结果")
    @PostMapping("/sync")
    public ResResult<Void> sync(String allocationNo){
        AllocationSyncParam param = new AllocationSyncParam();
        param.setAllocationNo(allocationNo);
        allocationService.sync(param);
        return Res.ok();
    }

    @Operation(summary = "分账完结")
    @PostMapping("/finish")
    public ResResult<Void> finish(String allocationNo){
        AllocationFinishParam param = new AllocationFinishParam();
        param.setAllocationNo(allocationNo);
        allocationService.finish(param);
        return Res.ok();
    }

    @Operation(summary = "重新发起分账")
    @PostMapping("/retry")
    public ResResult<Void> retryAllocation(String bizAllocationNo){
        AllocationStartParam param = new AllocationStartParam();
        param.setBizAllocationNo(bizAllocationNo);
        allocationService.allocation(param);
        return Res.ok();
    }
}
