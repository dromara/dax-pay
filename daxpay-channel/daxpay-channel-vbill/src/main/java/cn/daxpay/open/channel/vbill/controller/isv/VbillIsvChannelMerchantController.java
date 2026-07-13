package cn.daxpay.open.channel.vbill.controller.isv;

import cn.daxpay.open.channel.vbill.param.isv.VbillIsvChannelMerchantCreateParam;
import cn.daxpay.open.channel.vbill.result.isv.VbillIsvChannelMerchantResult;
import cn.daxpay.open.channel.vbill.service.isv.VbillIsvChannelMerchantService;
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

/// # 随行付通道商户管理
@PermCode(menuCode = PermCodes.Channel.Merchant.MENU)
@Validated
@Tag(name = "随行付通道商户管理")
@RestController
@RequestMapping("/admin/vbill/isv-channel-merchant")
@RequiredArgsConstructor
public class VbillIsvChannelMerchantController {

    private final VbillIsvChannelMerchantService vbillIsvChannelMerchantService;

    @PermCode(code = PermCodes.Action.VIEW, nameCn = PermCodes.Channel.Merchant.VIEW_NAME_CN, nameEn = PermCodes.Channel.Merchant.VIEW_NAME_EN)
    @Operation(summary = "根据通道商户号查询随行付通道商户配置")
    @GetMapping("/find-by-channel-mch-no")
    public Result<VbillIsvChannelMerchantResult> findByChannelMchNo(
            @NotBlank(message = "{validation.field.channelMchNo.notBlank}") String channelMchNo) {
        return Res.ok(vbillIsvChannelMerchantService.findByChannelMchNo(channelMchNo));
    }

    @PermCode(code = PermCodes.Action.MANAGE, nameCn = PermCodes.Channel.Merchant.MANAGE_NAME_CN, nameEn = PermCodes.Channel.Merchant.MANAGE_NAME_EN)
    @Operation(summary = "创建随行付通道商户")
    @PostMapping("/create")
    public Result<Void> create(@RequestBody @Validated VbillIsvChannelMerchantCreateParam param) {
        vbillIsvChannelMerchantService.create(param);
        return Res.ok();
    }
}
