package cn.bootx.platform.daxpay.admin.controller.channel;

import cn.bootx.platform.common.core.rest.PageResult;
import cn.bootx.platform.common.core.rest.Res;
import cn.bootx.platform.common.core.rest.ResResult;
import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.daxpay.service.core.channel.cash.service.CashRecordService;
import cn.bootx.platform.daxpay.service.dto.channel.cash.CashRecordDto;
import cn.bootx.platform.daxpay.service.param.channel.cash.CashRecordQuery;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 现金控制器
 * @author xxm
 * @since 2024/2/18
 */
@Tag(name = "现金控制器")
@RestController
@RequestMapping("/cash")
@RequiredArgsConstructor
public class CashController {
    private final CashRecordService cashRecordService;

    @Operation(summary = "记录分页")
    @GetMapping("/record/page")
    public ResResult<PageResult<CashRecordDto>> recordPage(PageParam pageParam, CashRecordQuery query){
        return Res.ok(cashRecordService.page(pageParam, query));
    }


    @Operation(summary = "查询记录详情")
    @GetMapping("/findById")
    public ResResult<CashRecordDto> findById(Long id){
        return Res.ok(cashRecordService.findById(id));
    }


}
