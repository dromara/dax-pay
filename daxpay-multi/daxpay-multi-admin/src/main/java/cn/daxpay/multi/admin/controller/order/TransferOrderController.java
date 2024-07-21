package cn.daxpay.multi.admin.controller.order;

import cn.bootx.platform.core.annotation.RequestGroup;
import cn.bootx.platform.core.annotation.RequestPath;
import cn.bootx.platform.core.rest.Res;
import cn.bootx.platform.core.rest.param.PageParam;
import cn.bootx.platform.core.rest.result.PageResult;
import cn.bootx.platform.core.rest.result.Result;
import cn.daxpay.multi.service.param.order.transfer.TransferOrderQuery;
import cn.daxpay.multi.service.result.order.transfer.TransferOrderResult;
import cn.daxpay.multi.service.service.order.transfer.TransferOrderQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 转账订单控制器
 * @author xxm
 * @since 2024/6/17
 */
@Tag(name = "转账订单控制器")
@RestController
@RequestMapping("/order/transfer")
@RequestGroup(moduleCode = "TradeOrder", groupCode = "TransferOrder", groupName = "转账订单")
@RequiredArgsConstructor
public class TransferOrderController {
    private final TransferOrderQueryService queryService;

    @RequestPath("分页查询")
    @Operation(summary = "分页查询")
    @GetMapping("/page")
    public Result<PageResult<TransferOrderResult>> page(PageParam pageParam, TransferOrderQuery query){
        return Res.ok(queryService.page(pageParam, query));
    }

    @RequestPath("根据转账号查询")
    @Operation(summary = "根据转账号查询")
    @GetMapping("/findByTransferNo")
    public Result<TransferOrderResult> findByTransferNo(String refundNo){
        return Res.ok(queryService.findByTransferNo(refundNo));
    }

    @RequestPath("根据商户转账号查询")
    @Operation(summary = "根据商户转账号查询")
    @GetMapping("/findByBizTransferNo")
    public Result<TransferOrderResult> findByBizTransferNo(String bizTransferNo){
        return Res.ok(queryService.findByBizTransferNo(bizTransferNo));
    }

    @RequestPath("查询单条")
    @Operation(summary = "查询单条")
    @GetMapping("/findById")
    public Result<TransferOrderResult> findById(Long id){
        return Res.ok(queryService.findById(id));
    }



    @RequestPath("查询金额汇总")
    @Operation(summary = "查询金额汇总")
    @GetMapping("/getTotalAmount")
    public Result<Integer> getTotalAmount(TransferOrderQuery param){
        return Res.ok(queryService.getTotalAmount(param));
    }
}
