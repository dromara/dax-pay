package cn.bootx.platform.daxpay.controller;

import cn.bootx.platform.common.core.rest.PageResult;
import cn.bootx.platform.common.core.rest.Res;
import cn.bootx.platform.common.core.rest.ResResult;
import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.daxpay.core.refund.record.service.PayRefundRecordService;
import cn.bootx.platform.daxpay.dto.refund.PayRefundRecordDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 退款记录
 *
 * @author xxm
 * @since 2022/3/3
 */
@Tag(name = "退款记录")
@RestController
@RequestMapping("/pay/refund")
@RequiredArgsConstructor
public class PayRefundRecordController {

    private final PayRefundRecordService payRefundRecordService;

    @Operation(summary = "分页")
    @GetMapping("/page")
    public ResResult<PageResult<PayRefundRecordDto>> page(PageParam pageParam, PayRefundRecordDto param) {
        return Res.ok(payRefundRecordService.page(pageParam, param));
    }

    @Operation(summary = "根据id查询")
    @GetMapping("/findById")
    public ResResult<PayRefundRecordDto> findById(Long id) {
        return Res.ok(payRefundRecordService.findById(id));
    }

}
