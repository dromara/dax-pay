package cn.bootx.platform.daxpay.admin.controller.channel;

import cn.bootx.platform.common.core.rest.PageResult;
import cn.bootx.platform.common.core.rest.Res;
import cn.bootx.platform.common.core.rest.ResResult;
import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.daxpay.service.dto.channel.union.UnionPayRecordDto;
import cn.bootx.platform.daxpay.service.param.channel.union.UnionPayRecordQuery;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 云闪付控制器
 * @author xxm
 * @since 2024/3/9
 */
@Tag(name = "云闪付控制器")
@RestController
@RequestMapping("/union/pay")
@RequiredArgsConstructor
public class UnionPayController {

    private final UnionPayRecordService unionPayRecordService;;

    @Operation(summary = "记录分页")
    @GetMapping("/record/page")
    public ResResult<PageResult<UnionPayRecordDto>> recordPage(PageParam pageParam, UnionPayRecordQuery query){
        return Res.ok(unionPayRecordService.page(pageParam, query));
    }

    @Operation(summary = "查询记录详情")
    @GetMapping("/record/findById")
    public ResResult<UnionPayRecordDto> findById(Long id){
        return Res.ok(unionPayRecordService.findById(id));
    }
}
