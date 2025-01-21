package org.dromara.daxpay.service.controller.record;

import cn.bootx.platform.core.annotation.RequestGroup;
import cn.bootx.platform.core.annotation.RequestPath;
import cn.bootx.platform.core.rest.Res;
import cn.bootx.platform.core.rest.param.PageParam;
import cn.bootx.platform.core.rest.result.PageResult;
import cn.bootx.platform.core.rest.result.Result;
import org.dromara.daxpay.service.param.record.TradeSyncRecordQuery;
import org.dromara.daxpay.service.result.record.sync.TradeSyncRecordResult;
import org.dromara.daxpay.service.service.record.sync.TradeSyncRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 交易同步记录
 * @author xxm
 * @since 2024/8/17
 */
@Tag(name = "交易同步记录")
@RestController
@RequestGroup(moduleCode = "TradeRecord", groupCode = "TradeSync", groupName = "交易同步记录")
@RequestMapping("/record/sync")
@RequiredArgsConstructor
public class TradeSyncRecordController {
    private final TradeSyncRecordService service;


    @RequestPath("分页查询")
    @Operation(summary = "分页查询")
    @GetMapping("/page")
    public Result<PageResult<TradeSyncRecordResult>> page(PageParam pageParam, TradeSyncRecordQuery query) {
        return Res.ok(service.page(pageParam, query));
    }

    @RequestPath("查询单条")
    @Operation(summary = "查询单条")
    @GetMapping("/findById")
    public Result<TradeSyncRecordResult> findById(Long id) {
        return Res.ok(service.findById(id));
    }
}
