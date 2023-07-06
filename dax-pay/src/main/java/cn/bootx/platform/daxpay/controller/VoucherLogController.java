package cn.bootx.platform.daxpay.controller;

import cn.bootx.platform.common.core.rest.PageResult;
import cn.bootx.platform.common.core.rest.Res;
import cn.bootx.platform.common.core.rest.ResResult;
import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.daxpay.core.channel.voucher.service.VoucherLogService;
import cn.bootx.platform.daxpay.dto.channel.voucher.VoucherLogDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 储值卡日志
 * @author xxm
 * @since 2023/7/6
 */
@Tag(name = "储值卡日志")
@RestController
@RequestMapping("/voucher/log")
@RequiredArgsConstructor
public class VoucherLogController {
    private final VoucherLogService voucherLogService;

    @Operation(summary = "储值卡日志分页")
    @GetMapping("/pageByVoucherId")
    public ResResult<PageResult<VoucherLogDto>> pageByVoucherId(PageParam pageParam, Long voucherId){
        return Res.ok(voucherLogService.pageByVoucherId(pageParam,voucherId));
    }
}
