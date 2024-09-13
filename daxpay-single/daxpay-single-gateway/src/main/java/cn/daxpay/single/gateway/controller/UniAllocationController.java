package cn.daxpay.single.gateway.controller;

import cn.bootx.platform.common.core.annotation.IgnoreAuth;
import cn.daxpay.single.core.code.PaymentApiCode;
import cn.daxpay.single.core.param.payment.allocation.AllocFinishParam;
import cn.daxpay.single.core.param.payment.allocation.AllocReceiverAddParam;
import cn.daxpay.single.core.param.payment.allocation.AllocReceiverRemoveParam;
import cn.daxpay.single.core.param.payment.allocation.AllocationParam;
import cn.daxpay.single.core.result.DaxResult;
import cn.daxpay.single.core.result.allocation.AllocReceiverAddResult;
import cn.daxpay.single.core.result.allocation.AllocReceiverRemoveResult;
import cn.daxpay.single.core.result.allocation.AllocationResult;
import cn.daxpay.single.service.annotation.PaymentVerify;
import cn.daxpay.single.service.annotation.InitPaymentContext;
import cn.daxpay.single.service.core.payment.allocation.service.AllocReceiverService;
import cn.daxpay.single.service.core.payment.allocation.service.AllocationService;
import cn.daxpay.single.core.util.DaxRes;
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
@IgnoreAuth
@Tag(name = "分账控制器")
@RestController
@RequestMapping("/unipay/allocation")
@RequiredArgsConstructor
public class UniAllocationController {

    private final AllocationService allocationService;

    private final AllocReceiverService receiverService;

    @PaymentVerify
    @InitPaymentContext(PaymentApiCode.ALLOCATION)
    @Operation(summary = "发起分账接口")
    @PostMapping("/start")
    public DaxResult<AllocationResult> start(@RequestBody AllocationParam param){
        return DaxRes.ok(allocationService.allocation(param));
    }

    @PaymentVerify
    @InitPaymentContext(PaymentApiCode.ALLOCATION_FINISH)
    @Operation(summary = "分账完结接口")
    @PostMapping("/finish")
    public DaxResult<AllocationResult> finish(@RequestBody AllocFinishParam param){
        return DaxRes.ok(allocationService.finish(param));
    }

    @PaymentVerify
    @InitPaymentContext(PaymentApiCode.ALLOCATION_RECEIVER_ADD)
    @Operation(summary = "分账接收方添加接口")
    @PostMapping("/receiver/add")
    public DaxResult<AllocReceiverAddResult> receiverAdd(@RequestBody AllocReceiverAddParam param){
        return DaxRes.ok(receiverService.addAndSync(param));
    }

    @PaymentVerify
    @InitPaymentContext(PaymentApiCode.ALLOCATION_RECEIVER_REMOVE)
    @Operation(summary = "分账接收方删除接口")
    @PostMapping("/receiver/remove")
    public DaxResult<AllocReceiverRemoveResult> receiverRemove(@RequestBody AllocReceiverRemoveParam param){
        return DaxRes.ok(receiverService.remove(param));
    }


}
