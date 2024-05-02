package cn.bootx.platform.daxpay.admin.controller.record;

import cn.bootx.platform.common.core.rest.PageResult;
import cn.bootx.platform.common.core.rest.Res;
import cn.bootx.platform.common.core.rest.ResResult;
import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.daxpay.service.core.record.close.service.PayCloseRecordService;
import cn.bootx.platform.daxpay.service.dto.record.close.PayCloseRecordDto;
import cn.bootx.platform.daxpay.service.param.record.PayCloseRecordQuery;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author xxm
 * @since 2024/1/9
 */
@Tag(name = "支付订单关闭记录")
@RestController
@RequestMapping("/record/close")
@RequiredArgsConstructor
public class PayCloseRecordController {
    private final PayCloseRecordService service;

    @Operation(summary = "分页查询")
    @GetMapping("/page")
    public ResResult<PageResult<PayCloseRecordDto>> page(PageParam pageParam, PayCloseRecordQuery query){
        return Res.ok(service.page(pageParam, query));
    }

    @Operation(summary = "查询单条")
    @GetMapping("/findById")
    public ResResult<PayCloseRecordDto> findById(Long id){
        return Res.ok(service.findById(id));
    }

}
