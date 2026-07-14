package cn.daxpay.open.channel.wechat.controller.isv;

import cn.daxpay.open.platform.core.code.PermCodes;
import cn.daxpay.open.platform.core.annotation.PermCode;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.result.Result;
import cn.daxpay.open.channel.wechat.param.isv.WechatIsvAuthAppTypeUpdateParam;
import cn.daxpay.open.channel.wechat.param.isv.WechatIsvChannelMerchantCreateParam;
import cn.daxpay.open.channel.wechat.result.isv.WechatIsvChannelMerchantResult;
import cn.daxpay.open.channel.wechat.service.isv.WechatIsvChannelMerchantService;
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

/// # 微信服务商通道商户管理
///
@PermCode(menuCode = PermCodes.Channel.Merchant.MENU)
@Validated
@Tag(name = "微信服务商通道商户管理")
@RestController
@RequestMapping("/admin/wechat/isv-channel-merchant")
@RequiredArgsConstructor
public class WechatIsvChannelMerchantController {

    private final WechatIsvChannelMerchantService wechatIsvChannelMerchantService;

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "根据通道商户号查询微信服务商通道商户配置")
    @GetMapping("/find-by-channel-mch-no")
    public Result<WechatIsvChannelMerchantResult> findByChannelMchNo(
            @NotBlank(message = "{validation.field.channelMerchantNo.notBlank}") String channelMchNo) {
        return Res.ok(wechatIsvChannelMerchantService.findByChannelMchNo(channelMchNo));
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "创建微信服务商通道商户")
    @PostMapping("/create")
    public Result<Void> create(@RequestBody @Validated WechatIsvChannelMerchantCreateParam param) {
        wechatIsvChannelMerchantService.create(param);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "更新认证应用类型")
    @PostMapping("/update-auth-app-type")
    public Result<Void> updateAuthAppType(@RequestBody @Validated WechatIsvAuthAppTypeUpdateParam param) {
        wechatIsvChannelMerchantService.updateAuthAppType(param);
        return Res.ok();
    }
}
