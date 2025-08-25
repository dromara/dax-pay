package org.dromara.daxpay.controller.constant;

import cn.bootx.platform.core.annotation.IgnoreAuth;
import cn.bootx.platform.core.rest.Res;
import cn.bootx.platform.core.rest.dto.LabelValue;
import cn.bootx.platform.core.rest.param.PageParam;
import cn.bootx.platform.core.rest.result.PageResult;
import cn.bootx.platform.core.rest.result.Result;
import org.dromara.daxpay.service.pay.param.constant.ChannelConstQuery;
import org.dromara.daxpay.service.pay.result.constant.ChannelConstResult;
import org.dromara.daxpay.service.pay.service.constant.ChannelConstService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 支付通道常量控制器
 * @author xxm
 * @since 2024/7/14
 */
@IgnoreAuth
@Validated
@Tag(name = "支付通道常量控制器")
@RestController
@RequestMapping("/const/channel")
@RequiredArgsConstructor
public class ChannelConstController {
    private final ChannelConstService channelConstService;

    @Operation(summary = "支付通道分页")
    @GetMapping("/page")
    public Result<PageResult<ChannelConstResult>> page(PageParam pageParam, ChannelConstQuery query) {
        return Res.ok(channelConstService.page(pageParam, query));
    }

    @Operation(summary = "服务商通道下拉列表")
    @GetMapping("/dropdownByIsv")
    public Result<List<LabelValue>> dropdownByIsv() {
        return Res.ok(channelConstService.dropdownByIsv());
    }

    @Operation(summary = "可进件通道下拉列表")
    @GetMapping("/dropdownByApply")
    public Result<List<LabelValue>> dropdownByApply() {
        return Res.ok(channelConstService.dropdownByApply());
    }
}
