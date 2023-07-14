package cn.bootx.platform.daxpay.controller;

import cn.bootx.platform.common.core.rest.PageResult;
import cn.bootx.platform.common.core.rest.Res;
import cn.bootx.platform.common.core.rest.ResResult;
import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.daxpay.core.sync.record.service.PaySyncRecordService;
import cn.bootx.platform.daxpay.dto.sync.PaySyncRecordDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "支付同步记录")
@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class PaySyncRecordController {
    private final PaySyncRecordService syncRecordService;

    @Operation(summary = "分页")
    @GetMapping("/page")
    public ResResult<PageResult<PaySyncRecordDto>> page(PageParam pageParam, PaySyncRecordDto param) {
        return Res.ok(syncRecordService.page(pageParam, param));
    }

    @Operation(summary = "根据id查询")
    @GetMapping("/findById")
    public ResResult<PaySyncRecordDto> findById(Long id) {
        return Res.ok(syncRecordService.findById(id));
    }
}
