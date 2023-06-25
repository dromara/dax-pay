package cn.bootx.platform.daxpay.controller;

import cn.bootx.platform.common.core.rest.PageResult;
import cn.bootx.platform.common.core.rest.Res;
import cn.bootx.platform.common.core.rest.ResResult;
import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.daxpay.core.notify.service.PayNotifyRecordService;
import cn.bootx.platform.daxpay.dto.notify.PayNotifyRecordDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 回调记录
 *
 * @author xxm
 * @since 2021/7/22
 */
@Tag(name = "支付回调记录")
@RestController
@RequestMapping("/pay/notify/record")
@RequiredArgsConstructor
public class PayNotifyRecordController {

    private final PayNotifyRecordService notifyRecordService;

    @Operation(summary = "分页")
    @GetMapping("/page")
    public ResResult<PageResult<PayNotifyRecordDto>> page(PageParam pageParam, PayNotifyRecordDto param) {
        return Res.ok(notifyRecordService.page(pageParam, param));
    }

    @Operation(summary = "根据id查询")
    @GetMapping("/findById")
    public ResResult<PayNotifyRecordDto> findById(Long id) {
        return Res.ok(notifyRecordService.findById(id));
    }

}
