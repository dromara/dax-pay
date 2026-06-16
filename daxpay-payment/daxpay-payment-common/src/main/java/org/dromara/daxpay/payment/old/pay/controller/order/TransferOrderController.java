package org.dromara.daxpay.payment.old.pay.controller.order;

import org.dromara.daxpay.platform.core.rest.Res;
import org.dromara.daxpay.platform.core.rest.param.PageParam;
import org.dromara.daxpay.platform.core.rest.result.PageResult;
import org.dromara.daxpay.platform.core.rest.result.Result;
import org.dromara.daxpay.payment.old.pay.param.order.transfer.TransferOrderQuery;
import org.dromara.daxpay.payment.old.pay.result.order.transfer.TransferOrderVo;
import org.dromara.daxpay.payment.old.pay.service.order.transfer.TransferOrderQueryService;
import org.dromara.daxpay.payment.old.pay.service.order.transfer.TransferOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

/// # 转账订单控制器
///
@Validated
@Tag(name = "转账订单控制器")
@RestController
@RequestMapping("/order/transfer")
@RequiredArgsConstructor
public class TransferOrderController {
    private final TransferOrderQueryService queryService;
    private final TransferOrderService transferOrderService;

    @Operation(summary = "分页查询")
    @GetMapping("/page")
    public Result<PageResult<TransferOrderVo>> page(PageParam pageParam, TransferOrderQuery query){
        return Res.ok(queryService.page(pageParam, query));
    }

    @Operation(summary = "根据转账号查询")
    @GetMapping("/get-by-transfer-no")
    public Result<TransferOrderVo> findByTransferNo(@NotBlank(message = "{validation.field.transferNo.notBlank}") @Parameter(description = "转账号") String transferNo){
        return Res.ok(queryService.findByTransferNo(transferNo));
    }

    @Operation(summary = "查询单条")
    @GetMapping("/get")
    public Result<TransferOrderVo> findById(@NotNull(message = "{validation.field.id.notNull}") Long id){
        return Res.ok(queryService.findById(id));
    }

    @Operation(summary = "查询金额汇总")
    @GetMapping("/get-total-amount")
    public Result<BigDecimal> getTotalAmount(TransferOrderQuery param){
        return Res.ok(queryService.getTotalAmount(param));
    }

    @Operation(summary = "转账同步")
    @PostMapping("/sync")
    public Result<Void> sync(@NotNull(message = "{validation.field.id.notNull}") Long id){
        transferOrderService.sync(id);
        return Res.ok();
    }
    @Operation(summary = "转账重试")
    @PostMapping("/retry")
    public Result<Void> retry(@NotNull(message = "{validation.field.id.notNull}") Long id){
        transferOrderService.retry(id);
        return Res.ok();
    }

    @Operation(summary = "转账关闭")
    @PostMapping("/close")
    public Result<Void> close(@NotNull(message = "{validation.field.id.notNull}") Long id){
        transferOrderService.close(id);
        return Res.ok();
    }
}
