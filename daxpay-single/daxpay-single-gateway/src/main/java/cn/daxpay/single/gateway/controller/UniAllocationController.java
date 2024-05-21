package cn.daxpay.single.gateway.controller;

import cn.daxpay.single.code.PaymentApiCode;
import cn.daxpay.single.param.payment.allocation.*;
import cn.daxpay.single.result.DaxResult;
import cn.daxpay.single.result.allocation.*;
import cn.daxpay.single.service.annotation.PaymentApi;
import cn.daxpay.single.service.core.payment.allocation.service.AllocationReceiverService;
import cn.daxpay.single.service.core.payment.allocation.service.AllocationService;
import cn.daxpay.single.util.DaxRes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 分账控制器
 * @author xxm
 * @since 2024/5/17
 */
@Tag(name = "分账控制器")
@RestController
@RequestMapping("/unipay/allocation")
@RequiredArgsConstructor
public class UniAllocationController {

    private final AllocationService allocationService;

    private final AllocationReceiverService receiverService;

    @PaymentApi(PaymentApiCode.ALLOCATION)
    @Operation(summary = "触发分账")
    @PostMapping("/open")
    public DaxResult<AllocationResult> open(@RequestBody AllocStartParam param){
        return DaxRes.ok(allocationService.allocation(param));
    }

    @PaymentApi(PaymentApiCode.ALLOCATION_FINISH)
    @Operation(summary = "分账完结接口")
    @PostMapping("/finish")
    public DaxResult<AllocationResult> finish(@RequestBody AllocFinishParam param){
        return DaxRes.ok(allocationService.finish(param));
    }

    @PaymentApi(PaymentApiCode.SYNC_ALLOCATION)
    @Operation(summary = "分账同步接口")
    @PostMapping("/sync")
    public DaxResult<AllocationSyncResult> sync(@RequestBody AllocSyncParam param){
        return DaxRes.ok(allocationService.sync(param));
    }

    @PaymentApi(PaymentApiCode.ALLOCATION_RECEIVER_ADD)
    @Operation(summary = "添加分账接收方接口")
    @PostMapping("/receiver/add")
    public DaxResult<AllocReceiverAddResult> receiverAdd(@RequestBody AllocReceiverAddParam param){
        return DaxRes.ok(receiverService.addAndSync(param));
    }

    @PaymentApi(PaymentApiCode.ALLOCATION_RECEIVER_REMOVE)
    @Operation(summary = "删除分账接收方接口")
    @PostMapping("/receiver/remove")
    public DaxResult<AllocReceiverRemoveResult> receiverRemove(@RequestBody AllocReceiverRemoveParam param){
        return DaxRes.ok(receiverService.remove(param));
    }

}
