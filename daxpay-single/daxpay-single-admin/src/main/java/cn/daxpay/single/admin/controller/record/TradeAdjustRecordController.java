package cn.daxpay.single.admin.controller.record;

import cn.bootx.platform.common.core.rest.PageResult;
import cn.bootx.platform.common.core.rest.Res;
import cn.bootx.platform.common.core.rest.ResResult;
import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.daxpay.single.service.core.record.repair.service.TradeAdjustRecordService;
import cn.daxpay.single.service.dto.record.repair.TradeAdjustRecordDto;
import cn.daxpay.single.service.param.report.TradeAdjustRecordQuery;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 支付修复记录控制器
 * @author xxm
 * @since 2024/1/9
 */
@Tag(name = "交易调整记录")
@RestController
@RequestMapping("/record/adjust")
@RequiredArgsConstructor
public class TradeAdjustRecordController {
    private final TradeAdjustRecordService service;

    @Operation(summary = "分页查询")
    @GetMapping("/page")
    public ResResult<PageResult<TradeAdjustRecordDto>> page(PageParam pageParam, TradeAdjustRecordQuery query){
        return Res.ok(service.page(pageParam, query));
    }

    @Operation(summary = "查询单条")
    @GetMapping("/findById")
    public ResResult<TradeAdjustRecordDto> findById(Long id){
        return Res.ok(service.findById(id));
    }

}
