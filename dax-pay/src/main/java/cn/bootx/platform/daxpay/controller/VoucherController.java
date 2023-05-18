package cn.bootx.platform.daxpay.controller;

import cn.bootx.platform.common.core.rest.PageResult;
import cn.bootx.platform.common.core.rest.Res;
import cn.bootx.platform.common.core.rest.ResResult;
import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.daxpay.core.channel.voucher.service.VoucherQueryService;
import cn.bootx.platform.daxpay.core.channel.voucher.service.VoucherService;
import cn.bootx.platform.daxpay.dto.paymodel.voucher.VoucherDto;
import cn.bootx.platform.daxpay.param.paymodel.voucher.VoucherGenerationParam;
import cn.bootx.platform.daxpay.param.paymodel.voucher.VoucherParam;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author xxm
 * @date 2022/3/14
 */
@Tag(name = "储值卡")
@RestController
@RequestMapping("/voucher")
@RequiredArgsConstructor
public class VoucherController {

    private final VoucherService voucherService;

    private final VoucherQueryService voucherQueryService;

    @Operation(summary = "分页")
    @GetMapping("/page")
    public ResResult<PageResult<VoucherDto>> page(PageParam pageParam, VoucherParam param) {
        return Res.ok(voucherQueryService.page(pageParam, param));
    }

    @Operation(summary = "单条查询")
    @GetMapping("/findById")
    public ResResult<VoucherDto> findById(Long id) {
        return Res.ok(voucherQueryService.findById(id));
    }

    @Operation(summary = "根据卡号查询")
    @GetMapping("/findByCardNo")
    public ResResult<VoucherDto> findByCardNo(String cardNo) {
        return Res.ok(voucherQueryService.findByCardNo(cardNo));
    }

    @Operation(summary = "批量生成储值卡")
    @PostMapping("/generationBatch")
    public ResResult<Void> generationBatch(@RequestBody VoucherGenerationParam param) {
        voucherService.generationBatch(param);
        return Res.ok();
    }

    @Operation(summary = "冻结")
    @PostMapping("/lock")
    public ResResult<Void> lock(Long id) {
        voucherService.lock(id);
        return Res.ok();
    }

    @Operation(summary = "启用")
    @PostMapping("/unlock")
    public ResResult<Void> unlock(Long id) {
        voucherService.unlock(id);
        return Res.ok();
    }

    @Operation(summary = "批量冻结")
    @PostMapping("/lockBatch")
    public ResResult<Void> lockBatch(@RequestBody List<Long> ids) {
        voucherService.lockBatch(ids);
        return Res.ok();
    }

    @Operation(summary = "批量启用")
    @PostMapping("/unlockBatch")
    public ResResult<Void> unlockBatch(@RequestBody List<Long> ids) {
        voucherService.unlockBatch(ids);
        return Res.ok();
    }

}
