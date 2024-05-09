package cn.daxpay.single.admin.controller.record;

import cn.bootx.platform.common.core.rest.PageResult;
import cn.bootx.platform.common.core.rest.Res;
import cn.bootx.platform.common.core.rest.ResResult;
import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.daxpay.single.service.core.record.callback.service.PayCallbackRecordService;
import cn.daxpay.single.service.dto.record.callback.PayCallbackRecordDto;
import cn.daxpay.single.service.param.record.PayCallbackRecordQuery;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 支付回调信息记录
 * @author xxm
 * @since 2024/1/9
 */
@Tag(name = "支付回调信息记录")
@RestController
@RequestMapping("/record/callback")
@RequiredArgsConstructor
public class PayCallbackRecordController {
    private final PayCallbackRecordService service;

    @Operation(summary = "分页查询")
    @GetMapping("/page")
    public ResResult<PageResult<PayCallbackRecordDto>> page(PageParam pageParam, PayCallbackRecordQuery query){
        return Res.ok(service.page(pageParam, query));
    }

    @Operation(summary = "查询单条")
    @GetMapping("/findById")
    public ResResult<PayCallbackRecordDto> findById(Long id){
        return Res.ok(service.findById(id));
    }

}
