package org.dromara.daxpay.service.controller.constant;

import cn.bootx.platform.core.annotation.RequestGroup;
import cn.bootx.platform.core.annotation.RequestPath;
import cn.bootx.platform.core.rest.Res;
import cn.bootx.platform.core.rest.param.PageParam;
import cn.bootx.platform.core.rest.result.PageResult;
import cn.bootx.platform.core.rest.result.Result;
import org.dromara.daxpay.service.param.constant.ChannelConstQuery;
import org.dromara.daxpay.service.result.constant.ChannelConstResult;
import org.dromara.daxpay.service.service.constant.ChannelConstService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 支付通道常量控制器
 * @author xxm
 * @since 2024/7/14
 */
@Tag(name = "支付通道常量控制器")
@RestController
@RequestMapping("/const/channel")
@RequiredArgsConstructor
@RequestGroup(groupCode = "PayConst", moduleCode = "PayConfig")
public class ChannelConstController {
    private final ChannelConstService channelConstService;

    @RequestPath("支付通道分页")
    @Operation(summary = "支付通道分页")
    @GetMapping("/page")
    public Result<PageResult<ChannelConstResult>> page(PageParam pageParam, ChannelConstQuery query) {
        return Res.ok(channelConstService.page(pageParam, query));
    }
}
