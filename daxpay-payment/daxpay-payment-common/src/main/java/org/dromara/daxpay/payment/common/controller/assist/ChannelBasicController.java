package org.dromara.daxpay.payment.common.controller.assist;

import org.dromara.daxpay.platform.core.annotation.IgnoreAuth;
import org.dromara.daxpay.platform.core.rest.Res;
import org.dromara.daxpay.platform.core.rest.dto.LabelValue;
import org.dromara.daxpay.platform.core.rest.result.Result;
import org.dromara.daxpay.payment.pay.service.assist.ChannelBasicService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/// # 通道基础数据控制器
///
@Validated
@IgnoreAuth
@Tag(name = "通道基础数据控制器")
@RestController
@RequestMapping("/channel/basic")
@RequiredArgsConstructor
public class ChannelBasicController {
    private final ChannelBasicService channelBasicService;

    @Operation(summary = "查询支付通道下属的支付方式列表")
    @GetMapping("/pay-method-list")
    public Result<List<LabelValue>> payMethodList(String channel){
        return Res.ok(channelBasicService.payMethodList(channel));
    }
}
