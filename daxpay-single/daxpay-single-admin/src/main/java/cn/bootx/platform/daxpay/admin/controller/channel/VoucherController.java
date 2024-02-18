package cn.bootx.platform.daxpay.admin.controller.channel;

import cn.bootx.platform.common.core.rest.PageResult;
import cn.bootx.platform.common.core.rest.Res;
import cn.bootx.platform.common.core.rest.ResResult;
import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.common.core.util.ValidationUtil;
import cn.bootx.platform.daxpay.service.core.channel.voucher.service.VoucherQueryService;
import cn.bootx.platform.daxpay.service.core.channel.voucher.service.VoucherRecordService;
import cn.bootx.platform.daxpay.service.core.channel.voucher.service.VoucherService;
import cn.bootx.platform.daxpay.service.dto.channel.voucher.VoucherDto;
import cn.bootx.platform.daxpay.service.dto.channel.voucher.VoucherRecordDto;
import cn.bootx.platform.daxpay.service.param.channel.voucher.VoucherImportParam;
import cn.bootx.platform.daxpay.service.param.channel.voucher.VoucherQuery;
import cn.bootx.platform.daxpay.service.param.channel.voucher.VoucherRecordQuery;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 储值卡管理
 * @author xxm
 * @since 2024/2/18
 */
@Tag(name = "储值卡管理")
@RestController
@RequestMapping("/voucher")
@RequiredArgsConstructor
public class VoucherController {
    private final VoucherService voucherService;
    private final VoucherQueryService voucherQueryService;
    private final VoucherRecordService voucherRecordService;

    @Operation(summary = "导入储值卡")
    @PostMapping("/import")
    public ResResult<Void> voucherImport(@RequestBody VoucherImportParam param){
        ValidationUtil.validateParam(param);
        voucherService.voucherImport(param);
        return Res.ok();
    }

    @Operation(summary = "储值卡分页")
    @GetMapping("/page")
    public ResResult<PageResult<VoucherDto>> page(PageParam pageParam, VoucherQuery query){
        return Res.ok(voucherQueryService.page(pageParam, query));
    }

    @Operation(summary = "查询储值卡详情")
    @GetMapping("/findById")
    public ResResult<VoucherDto> findById(Long id){
        return Res.ok(voucherQueryService.findById(id));
    }

    @Operation(summary = "判断卡号是否存在")
    @GetMapping("/existsByCardNo")
    public ResResult<Boolean> existsByCardNo(String userId){
        return Res.ok(voucherQueryService.existsByCardNo(userId));
    }

    @Operation(summary = "记录分页")
    @GetMapping("/record/page")
    public ResResult<PageResult<VoucherRecordDto>> recordPage(PageParam pageParam, VoucherRecordQuery query){
        return Res.ok(voucherRecordService.page(pageParam, query));
    }
}
