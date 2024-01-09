package cn.bootx.platform.daxpay.admin.controller.record;

import cn.bootx.platform.common.core.rest.PageResult;
import cn.bootx.platform.common.core.rest.Res;
import cn.bootx.platform.common.core.rest.ResResult;
import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.daxpay.service.core.record.sync.service.PaySyncRecordService;
import cn.bootx.platform.daxpay.service.dto.record.sync.PaySyncRecordDto;
import cn.bootx.platform.daxpay.service.param.record.PaySyncRecordQuery;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 支付同步记录控制器
 * @author xxm
 * @since 2024/1/9
 */
@Tag(name = "支付同步记录控制器")
@RestController
@RequestMapping("/record/sync")
@RequiredArgsConstructor
public class PaySyncRecordController {
    private final PaySyncRecordService service;

    @Operation(summary = "分页查询")
    @GetMapping("/page")
    public ResResult<PageResult<PaySyncRecordDto>> page(PageParam pageParam, PaySyncRecordQuery query){
        return Res.ok(service.page(pageParam, query));
    }

    @Operation(summary = "查询单条")
    @GetMapping("/findById")
    public ResResult<PaySyncRecordDto> findById(Long paymentId){
        return Res.ok(service.findById(paymentId));
    }

}
