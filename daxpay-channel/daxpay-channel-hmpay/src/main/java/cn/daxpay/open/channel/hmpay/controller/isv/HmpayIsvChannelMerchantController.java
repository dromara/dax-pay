package cn.daxpay.open.channel.hmpay.controller.isv;

import cn.daxpay.open.channel.hmpay.param.isv.HmpayIsvChannelMerchantCreateParam;
import cn.daxpay.open.channel.hmpay.param.isv.HmpayIsvChannelMerchantUpdateParam;
import cn.daxpay.open.channel.hmpay.result.isv.HmpayIsvChannelMerchantResult;
import cn.daxpay.open.channel.hmpay.service.isv.HmpayIsvChannelMerchantService;
import cn.daxpay.open.platform.core.annotation.PermCode;
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

/// # 河马付通道商户管理
@PermCode(menuCode = "channel:merchant")
@Validated
@Tag(name = "河马付通道商户管理")
@RestController
@RequestMapping("/admin/hmpay/isv-channel-merchant")
@RequiredArgsConstructor
public class HmpayIsvChannelMerchantController {

    private final HmpayIsvChannelMerchantService hmpayIsvChannelMerchantService;

    @PermCode(code = "view", nameCn = "通道商户查看", nameEn = "Channel Merchant View")
    @Operation(summary = "根据通道商户号查询河马付通道商户配置")
    @GetMapping("/find-by-channel-mch-no")
    public Result<HmpayIsvChannelMerchantResult> findByChannelMchNo(
            @NotBlank(message = "{validation.field.channelMchNo.notBlank}") String channelMchNo) {
        return Res.ok(hmpayIsvChannelMerchantService.findByChannelMchNo(channelMchNo));
    }

    @PermCode(code = "manage", nameCn = "通道商户管理", nameEn = "Channel Merchant Manage")
    @Operation(summary = "创建河马付通道商户")
    @PostMapping("/create")
    public Result<Void> create(@RequestBody @Validated HmpayIsvChannelMerchantCreateParam param) {
        hmpayIsvChannelMerchantService.create(param);
        return Res.ok();
    }

    @PermCode(code = "manage", nameCn = "通道商户管理", nameEn = "Channel Merchant Manage")
    @Operation(summary = "更新河马付通道商户可选配置")
    @PostMapping("/update-config")
    public Result<Void> updateConfig(@RequestBody @Validated HmpayIsvChannelMerchantUpdateParam param) {
        hmpayIsvChannelMerchantService.updateConfig(param);
        return Res.ok();
    }
}
