package org.dromara.daxpay.controller.order;

import cn.bootx.platform.core.annotation.ClientCode;
import cn.bootx.platform.core.annotation.RequestGroup;
import cn.bootx.platform.core.annotation.RequestPath;
import cn.bootx.platform.core.rest.Res;
import cn.bootx.platform.core.rest.param.PageParam;
import cn.bootx.platform.core.rest.result.PageResult;
import cn.bootx.platform.core.rest.result.Result;
import org.dromara.daxpay.service.common.code.DaxPayCode;
import org.dromara.daxpay.service.pay.param.order.transfer.TransferOrderQuery;
import org.dromara.daxpay.service.pay.result.order.transfer.TransferOrderVo;
import org.dromara.daxpay.service.pay.service.order.transfer.TransferOrderQueryService;
import org.dromara.daxpay.service.pay.service.order.transfer.TransferOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.dromara.core.trans.anno.TransMethodResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

/**
 * 转账订单控制器
 * @author xxm
 * @since 2024/6/17
 */
@Validated
@Tag(name = "转账订单控制器")
@RestController
@RequestMapping("/order/transfer")
@ClientCode({DaxPayCode.Client.ADMIN, DaxPayCode.Client.MERCHANT})
@RequestGroup(moduleCode = "TradeOrder", groupCode = "TransferOrder", groupName = "转账订单")
@RequiredArgsConstructor
public class TransferOrderController {
    private final TransferOrderQueryService queryService;
    private final TransferOrderService transferOrderService;

    @TransMethodResult
    @RequestPath("分页查询")
    @Operation(summary = "分页查询")
    @GetMapping("/page")
    public Result<PageResult<TransferOrderVo>> page(PageParam pageParam, TransferOrderQuery query){
        return Res.ok(queryService.page(pageParam, query));
    }

    @TransMethodResult
    @RequestPath("根据转账号查询")
    @Operation(summary = "根据转账号查询")
    @GetMapping("/findByTransferNo")
    public Result<TransferOrderVo> findByTransferNo(@NotBlank(message = "转账号不可为空") @Parameter(description = "转账号") String transferNo){
        return Res.ok(queryService.findByTransferNo(transferNo));
    }

    @TransMethodResult
    @RequestPath("查询单条")
    @Operation(summary = "查询单条")
    @GetMapping("/findById")
    public Result<TransferOrderVo> findById(@NotNull(message = "主键不可为空") Long id){
        return Res.ok(queryService.findById(id));
    }


    @RequestPath("查询金额汇总")
    @Operation(summary = "查询金额汇总")
    @GetMapping("/getTotalAmount")
    public Result<BigDecimal> getTotalAmount(TransferOrderQuery param){
        return Res.ok(queryService.getTotalAmount(param));
    }


    @RequestPath("转账同步")
    @Operation(summary = "转账同步")
    @PostMapping("/sync")
    public Result<Void> sync(@NotNull(message = "转账ID不可为空") Long id){
        transferOrderService.sync(id);
        return Res.ok();
    }

    @RequestPath("转账重试")
    @Operation(summary = "转账重试")
    @PostMapping("/retry")
    public Result<Void> retry(@NotNull(message = "转账ID不可为空") Long id){
        transferOrderService.retry(id);
        return Res.ok();
    }

    @RequestPath("转账关闭")
    @Operation(summary = "转账关闭")
    @PostMapping("/close")
    public Result<Void> close(@NotNull(message = "转账ID不可为空") Long id){
        transferOrderService.close(id);
        return Res.ok();
    }
}
