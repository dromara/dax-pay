package org.dromara.daxpay.service.controller.record;

import cn.bootx.platform.core.annotation.RequestGroup;
import cn.bootx.platform.core.annotation.RequestPath;
import cn.bootx.platform.core.rest.Res;
import cn.bootx.platform.core.rest.param.PageParam;
import cn.bootx.platform.core.rest.result.PageResult;
import cn.bootx.platform.core.rest.result.Result;
import org.dromara.daxpay.service.param.record.TradeCallbackRecordQuery;
import org.dromara.daxpay.service.result.record.callback.TradeCallbackRecordResult;
import org.dromara.daxpay.service.service.record.callback.TradeCallbackRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 交易记录
 * @author xxm
 * @since 2024/7/24
 */
@Tag(name = "交易记录")
@RestController
@RequestGroup(moduleCode = "TradeRecord", moduleName = "交易记录", groupCode = "TradeCallback", groupName = "交易回调记录")
@RequestMapping("/record/callback")
@RequiredArgsConstructor
public class CallbackRecordController {
    private final TradeCallbackRecordService service;

    @RequestPath("分页查询")
    @Operation(summary = "分页查询")
    @GetMapping("/page")
    public Result<PageResult<TradeCallbackRecordResult>> page(PageParam pageParam, TradeCallbackRecordQuery query){
        return Res.ok(service.page(pageParam, query));
    }

    @RequestPath("查询单条")
    @Operation(summary = "查询单条")
    @GetMapping("/findById")
    public Result<TradeCallbackRecordResult> findById(Long id){
        return Res.ok(service.findById(id));
    }
}
