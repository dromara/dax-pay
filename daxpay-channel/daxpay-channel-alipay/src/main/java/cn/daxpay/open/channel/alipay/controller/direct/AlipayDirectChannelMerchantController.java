package cn.daxpay.open.channel.alipay.controller.direct;


import cn.daxpay.open.platform.core.code.PermCodes;
import cn.daxpay.open.platform.core.annotation.PermCode;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.result.Result;
import cn.daxpay.open.channel.alipay.param.direct.AlipayDirectChannelMerchantCreateParam;
import cn.daxpay.open.channel.alipay.result.direct.AlipayDirectChannelMerchantResult;
import cn.daxpay.open.channel.alipay.service.direct.AlipayDirectChannelMerchantService;
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

/// # 支付宝直连通道商户管理
///
@PermCode(menuCode = PermCodes.Channel.Merchant.MENU)
@Validated
@Tag(name = "支付宝直连通道商户管理")
@RestController
@RequestMapping("/admin/alipay/direct-channel-merchant")
@RequiredArgsConstructor
public class AlipayDirectChannelMerchantController {

    private final AlipayDirectChannelMerchantService alipayDirectChannelMerchantService;

    @PermCode(code = PermCodes.Action.VIEW, nameCn = PermCodes.Channel.Merchant.VIEW_NAME_CN, nameEn = PermCodes.Channel.Merchant.VIEW_NAME_EN)
    @Operation(summary = "根据通道商户号查询支付宝直连通道商户配置")
    @GetMapping("/find-by-channel-mch-no")
    public Result<AlipayDirectChannelMerchantResult> findByChannelMchNo(
            @NotBlank(message = "{validation.field.channelMerchantNo.notBlank}") String channelMchNo) {
        return Res.ok(alipayDirectChannelMerchantService.findByChannelMchNo(channelMchNo));
    }

    @PermCode(code = PermCodes.Action.MANAGE, nameCn = PermCodes.Channel.Merchant.MANAGE_NAME_CN, nameEn = PermCodes.Channel.Merchant.MANAGE_NAME_EN)
    @Operation(summary = "创建支付宝直连通道商户")
    @PostMapping("/create")
    public Result<Void> create(@RequestBody @Validated AlipayDirectChannelMerchantCreateParam param) {
        alipayDirectChannelMerchantService.create(param);
        return Res.ok();
    }
}
