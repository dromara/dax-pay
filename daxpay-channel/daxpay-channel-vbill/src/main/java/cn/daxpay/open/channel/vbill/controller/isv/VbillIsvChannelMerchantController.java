package cn.daxpay.open.channel.vbill.controller.isv;

import cn.daxpay.open.channel.vbill.param.isv.VbillIsvChannelMerchantCreateParam;
import cn.daxpay.open.channel.vbill.result.isv.VbillIsvChannelMerchantResult;
import cn.daxpay.open.channel.vbill.service.isv.VbillIsvChannelMerchantService;
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

/// # 随行付通道商户管理
@PermCode(menuCode = "channel:merchant")
@Validated
@Tag(name = "随行付通道商户管理")
@RestController
@RequestMapping("/admin/vbill/isv-channel-merchant")
@RequiredArgsConstructor
public class VbillIsvChannelMerchantController {

    private final VbillIsvChannelMerchantService vbillIsvChannelMerchantService;

    @PermCode(code = "view", nameCn = "通道商户查看", nameEn = "Channel Merchant View")
    @Operation(summary = "根据通道商户号查询随行付通道商户配置")
    @GetMapping("/find-by-channel-mch-no")
    public Result<VbillIsvChannelMerchantResult> findByChannelMchNo(
            @NotBlank(message = "{validation.field.channelMchNo.notBlank}") String channelMchNo) {
        return Res.ok(vbillIsvChannelMerchantService.findByChannelMchNo(channelMchNo));
    }

    @PermCode(code = "manage", nameCn = "通道商户管理", nameEn = "Channel Merchant Manage")
    @Operation(summary = "创建随行付通道商户")
    @PostMapping("/create")
    public Result<Void> create(@RequestBody @Validated VbillIsvChannelMerchantCreateParam param) {
        vbillIsvChannelMerchantService.create(param);
        return Res.ok();
    }
}
