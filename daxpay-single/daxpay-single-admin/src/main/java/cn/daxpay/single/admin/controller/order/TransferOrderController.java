package cn.daxpay.single.admin.controller.order;

import cn.bootx.platform.common.core.rest.PageResult;
import cn.bootx.platform.common.core.rest.Res;
import cn.bootx.platform.common.core.rest.ResResult;
import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.daxpay.single.core.code.PaymentApiCode;
import cn.daxpay.single.core.param.payment.transfer.TransferParam;
import cn.daxpay.single.service.annotation.InitPaymentContext;
import cn.daxpay.single.service.core.order.transfer.service.TransferOrderQueryService;
import cn.daxpay.single.service.core.payment.transfer.service.TransferService;
import cn.daxpay.single.service.dto.order.transfer.TransferOrderDto;
import cn.daxpay.single.service.param.order.TransferOrderQuery;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 转账订单控制器
 * @author xxm
 * @since 2024/6/17
 */
@Tag(name = "转账订单控制器")
@RestController
@RequestMapping("/order/transfer")
@RequiredArgsConstructor
public class TransferOrderController {
    private final TransferOrderQueryService queryService;
    private final TransferService transferService;


    @Operation(summary = "分页查询")
    @GetMapping("/page")
    public ResResult<PageResult<TransferOrderDto>> page(PageParam pageParam, TransferOrderQuery query){
        return Res.ok(queryService.page(pageParam, query));
    }

    @Operation(summary = "根据转账号查询")
    @GetMapping("/findByTransferNo")
    public ResResult<TransferOrderDto> findByTransferNo(String refundNo){
        return Res.ok(queryService.findByTransferNo(refundNo));
    }

    @Operation(summary = "根据商户转账号查询")
    @GetMapping("/findByBizTransferNo")
    public ResResult<TransferOrderDto> findByBizTransferNo(String bizTransferNo){
        return Res.ok(queryService.findByBizTransferNo(bizTransferNo));
    }

    @Operation(summary = "查询单条")
    @GetMapping("/findById")
    public ResResult<TransferOrderDto> findById(Long id){
        return Res.ok(queryService.findById(id));
    }


    @InitPaymentContext(PaymentApiCode.TRANSFER)
    @Operation(summary = "手动发起转账")
    @PostMapping("/transfer")
    public ResResult<Void> transfer(@RequestBody TransferParam param){
        transferService.transfer(param);
        return Res.ok();
    }

    @InitPaymentContext(PaymentApiCode.TRANSFER)
    @Operation(summary = "重新发起转账")
    @PostMapping("/resetTransfer")
    public ResResult<Void> resetTransfer(Long id){
        return Res.ok();
    }

    @Operation(summary = "手动转账同步")
    @PostMapping("/syncByTransferNo")
    public ResResult<Void> syncByTransferNo(String transferNo){
        return Res.ok();
    }

    @Operation(summary = "查询金额汇总")
    @GetMapping("/getTotalAmount")
    public ResResult<Integer> getTotalAmount(TransferOrderQuery param){
        return Res.ok(queryService.getTotalAmount(param));
    }
}
