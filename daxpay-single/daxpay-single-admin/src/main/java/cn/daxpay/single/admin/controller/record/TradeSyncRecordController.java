package cn.daxpay.single.admin.controller.record;

import cn.bootx.platform.common.core.rest.PageResult;
import cn.bootx.platform.common.core.rest.Res;
import cn.bootx.platform.common.core.rest.ResResult;
import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.daxpay.single.service.core.record.sync.service.TradeSyncRecordService;
import cn.daxpay.single.service.dto.record.sync.SyncRecordDto;
import cn.daxpay.single.service.param.record.TradeSyncRecordQuery;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 交易同步记录控制器
 * @author xxm
 * @since 2024/1/9
 */
@Tag(name = "交易同步记录控制器")
@RestController
@RequestMapping("/record/sync")
@RequiredArgsConstructor
public class TradeSyncRecordController {
    private final TradeSyncRecordService service;

    @Operation(summary = "分页查询")
    @GetMapping("/page")
    public ResResult<PageResult<SyncRecordDto>> page(PageParam pageParam, TradeSyncRecordQuery query){
        return Res.ok(service.page(pageParam, query));
    }

    @Operation(summary = "查询单条")
    @GetMapping("/findById")
    public ResResult<SyncRecordDto> findById(Long id){
        return Res.ok(service.findById(id));
    }

}
