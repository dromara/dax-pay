package org.dromara.daxpay.service.controller.record;

import cn.bootx.platform.core.annotation.RequestGroup;
import cn.bootx.platform.core.annotation.RequestPath;
import cn.bootx.platform.core.rest.Res;
import cn.bootx.platform.core.rest.param.PageParam;
import cn.bootx.platform.core.rest.result.PageResult;
import cn.bootx.platform.core.rest.result.Result;
import org.dromara.daxpay.service.param.record.TradeFlowRecordQuery;
import org.dromara.daxpay.service.result.record.flow.TradeFlowAmountResult;
import org.dromara.daxpay.service.result.record.flow.TradeFlowRecordResult;
import org.dromara.daxpay.service.service.record.flow.TradeFlowRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 交易流水记录控制器
 * @author xxm
 * @since 2024/5/17
 */
@Tag(name = "交易流水记录控制器")
@RestController
@RequestMapping("/record/flow")
@RequestGroup(moduleCode = "TradeRecord", groupCode = "TradeFlow", groupName = "交易流水记录")
@RequiredArgsConstructor
public class TradeFlowRecordController {
    private final TradeFlowRecordService service;

    @RequestPath("分页查询")
    @Operation(summary = "分页查询")
    @GetMapping("/page")
    public Result<PageResult<TradeFlowRecordResult>> page(PageParam pageParam, TradeFlowRecordQuery query) {
        return Res.ok(service.page(pageParam, query));
    }

    @RequestPath("查询单条")
    @Operation(summary = "查询单条")
    @GetMapping("/findById")
    public Result<TradeFlowRecordResult> findById(Long id) {
        return Res.ok(service.findById(id));
    }

    @RequestPath("查询各类金额汇总")
    @Operation(summary = "查询各类金额汇总")
    @GetMapping("/summary")
    public Result<TradeFlowAmountResult> summary(TradeFlowRecordQuery query) {
        return Res.ok(service.summary(query));
    }
}
