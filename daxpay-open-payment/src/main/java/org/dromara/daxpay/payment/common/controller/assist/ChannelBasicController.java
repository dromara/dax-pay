package org.dromara.daxpay.payment.common.controller.assist;

import cn.bootx.platform.core.annotation.IgnoreAuth;
import cn.bootx.platform.core.rest.Res;
import cn.bootx.platform.core.rest.dto.LabelValue;
import cn.bootx.platform.core.rest.result.Result;
import org.dromara.daxpay.payment.pay.service.assist.ChannelBasicService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 通道基础数据控制器
 * @author xxm
 * @since 2025/6/4
 */
@Validated
@IgnoreAuth
@Tag(name = "通道基础数据控制器")
@RestController
@RequestMapping("/channel/basic")
@RequiredArgsConstructor
public class ChannelBasicController {
    private final ChannelBasicService channelBasicService;

    @Operation(summary = "查询支付通道下属的支付方式列表")
    @GetMapping("/payMethodList")
    public Result<List<LabelValue>> payMethodList(String channel){
        return Res.ok(channelBasicService.payMethodList(channel));
    }

    @Operation(summary = "查询通道下指定场景的支付方式列表")
    @GetMapping("/findMethodByScene")
    public Result<List<LabelValue>> findMethodByScene(String channel, String scene){
        return Res.ok(channelBasicService.findMethodByScene(channel, scene));
    }

    @Operation(summary = "查询通道下指定场景的网关支付可用的支付方式列表(网关支付)")
    @GetMapping("/gateway/findMethodByScene")
    public Result<List<LabelValue>> findGatewayMethodByScene(String channel, String scene){
        return Res.ok(channelBasicService.findGatewayMethodByScene(channel, scene));
    }
}
