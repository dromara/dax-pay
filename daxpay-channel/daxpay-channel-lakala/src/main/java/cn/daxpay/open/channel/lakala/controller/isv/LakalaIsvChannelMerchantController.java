package cn.daxpay.open.channel.lakala.controller.isv;

import cn.daxpay.open.channel.lakala.param.isv.LakalaIsvChannelMerchantCreateParam;
import cn.daxpay.open.channel.lakala.result.isv.LakalaIsvChannelMerchantResult;
import cn.daxpay.open.channel.lakala.service.isv.LakalaIsvChannelMerchantService;
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

/// # 拉卡拉通道商户管理
///
@PermCode(menuCode = PermCodes.Channel.Merchant.MENU)
@Validated
@Tag(name = "拉卡拉通道商户管理")
@RestController
@RequestMapping("/admin/lakala/isv-channel-merchant")
@RequiredArgsConstructor
public class LakalaIsvChannelMerchantController {

    private final LakalaIsvChannelMerchantService lakalaIsvChannelMerchantService;

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "根据通道商户号查询拉卡拉通道商户配置")
    @GetMapping("/find-by-channel-mch-no")
    public Result<LakalaIsvChannelMerchantResult> findByChannelMchNo(
            @NotBlank(message = "{validation.field.channelMchNo.notBlank}") String channelMchNo) {
        return Res.ok(lakalaIsvChannelMerchantService.findByChannelMchNo(channelMchNo));
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "创建拉卡拉通道商户")
    @PostMapping("/create")
    public Result<Void> create(@RequestBody @Validated LakalaIsvChannelMerchantCreateParam param) {
        lakalaIsvChannelMerchantService.create(param);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "更新终端号")
    @PostMapping("/update-term-no")
    public Result<Void> updateTermNo(
            @NotBlank(message = "{validation.field.channelMerchantNo.notBlank}") String channelMchNo,
            @NotBlank(message = "{validation.field.termNo.notBlank}") String termNo) {
        lakalaIsvChannelMerchantService.updateTermNo(channelMchNo, termNo);
        return Res.ok();
    }
}
