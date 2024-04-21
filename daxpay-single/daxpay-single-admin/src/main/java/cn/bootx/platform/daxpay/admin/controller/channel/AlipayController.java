package cn.bootx.platform.daxpay.admin.controller.channel;

import cn.bootx.platform.common.core.rest.PageResult;
import cn.bootx.platform.common.core.rest.Res;
import cn.bootx.platform.common.core.rest.ResResult;
import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.daxpay.service.dto.channel.alipay.AliPayRecordDto;
import cn.bootx.platform.daxpay.service.param.channel.alipay.AliPayRecordQuery;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 支付宝控制器
 * @author xxm
 * @since 2024/2/20
 */
@Tag(name = "支付宝控制器")
@RestController
@RequestMapping("/alipay")
@RequiredArgsConstructor
public class AlipayController {
    private final AliPayRecordService aliPayRecordService;


    @Operation(summary = "记录分页")
    @GetMapping("/record/page")
    public ResResult<PageResult<AliPayRecordDto>> recordPage(PageParam pageParam, AliPayRecordQuery query){
        return Res.ok(aliPayRecordService.page(pageParam, query));
    }


    @Operation(summary = "查询记录详情")
    @GetMapping("/record/findById")
    public ResResult<AliPayRecordDto> findById(Long id){
        return Res.ok(aliPayRecordService.findById(id));
    }

}
