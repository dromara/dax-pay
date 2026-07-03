package cn.daxpay.open.payment.web.assist;

import cn.daxpay.open.platform.core.annotation.IgnoreAuth;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.dto.LabelValue;
import cn.daxpay.open.platform.core.rest.result.Result;
import cn.daxpay.open.payment.core.assist.ChannelBasicService;
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
