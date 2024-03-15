package cn.bootx.platform.daxpay.admin.controller.channel;

import cn.bootx.platform.common.core.rest.PageResult;
import cn.bootx.platform.common.core.rest.Res;
import cn.bootx.platform.common.core.rest.ResResult;
import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.daxpay.service.core.channel.wechat.service.WeChatPayRecordService;
import cn.bootx.platform.daxpay.service.dto.channel.wechat.WeChatPayRecordDto;
import cn.bootx.platform.daxpay.service.param.channel.wechat.WeChatPayRecordQuery;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 微信支付控制器
 * @author xxm
 * @since 2024/2/20
 */
@Tag(name = "微信支付控制器")
@RestController
@RequestMapping("/wechat/pay")
@RequiredArgsConstructor
public class WeChatPayController {
    private final WeChatPayRecordService weChatPayRecordService;


    @Operation(summary = "记录分页")
    @GetMapping("/record/page")
    public ResResult<PageResult<WeChatPayRecordDto>> recordPage(PageParam pageParam, WeChatPayRecordQuery query){
        return Res.ok(weChatPayRecordService.page(pageParam, query));
    }

    @Operation(summary = "查询记录详情")
    @GetMapping("/record/findById")
    public ResResult<WeChatPayRecordDto> findById(Long id){
        return Res.ok(weChatPayRecordService.findById(id));
    }

}
