package cn.daxpay.open.channel.leshua.controller.isv;

import cn.daxpay.open.channel.leshua.param.isv.LeshuaIsvChannelMerchantCreateParam;
import cn.daxpay.open.channel.leshua.result.isv.LeshuaIsvChannelMerchantResult;
import cn.daxpay.open.channel.leshua.service.isv.LeshuaIsvChannelMerchantService;
import cn.daxpay.open.platform.core.annotation.PermCode;
import cn.daxpay.open.platform.core.code.PermCodes;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/// # 乐刷通道商户管理
///
@PermCode(menuCode = PermCodes.Channel.Merchant.MENU)
@Validated
@Tag(name = "乐刷通道商户管理")
@RestController
@RequestMapping("/admin/leshua/isv-channel-merchant")
@RequiredArgsConstructor
public class LeshuaIsvChannelMerchantController {

    private final LeshuaIsvChannelMerchantService leshuaIsvChannelMerchantService;

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "根据通道商户号查询乐刷通道商户配置")
    @GetMapping("/find-by-channel-mch-no")
    public Result<LeshuaIsvChannelMerchantResult> findByChannelMchNo(
            @NotBlank(message = "{validation.field.channelMchNo.notBlank}") String channelMchNo) {
        return Res.ok(leshuaIsvChannelMerchantService.findByChannelMchNo(channelMchNo));
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "创建乐刷通道商户")
    @PostMapping("/create")
    public Result<Void> create(@RequestBody @Validated LeshuaIsvChannelMerchantCreateParam param) {
        leshuaIsvChannelMerchantService.create(param);
        return Res.ok();
    }
}
