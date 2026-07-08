package cn.daxpay.open.channel.dougong.controller.isv;

import cn.daxpay.open.channel.dougong.param.isv.DougongIsvChannelMerchantCreateParam;
import cn.daxpay.open.channel.dougong.result.isv.DougongIsvChannelMerchantResult;
import cn.daxpay.open.channel.dougong.service.isv.DougongIsvChannelMerchantService;
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

/// # 斗拱通道商户管理
@PermCode(menuCode = "channel:merchant")
@Validated
@Tag(name = "斗拱通道商户管理")
@RestController
@RequestMapping("/admin/dougong/isv-channel-merchant")
@RequiredArgsConstructor
public class DougongIsvChannelMerchantController {

    private final DougongIsvChannelMerchantService dougongIsvChannelMerchantService;

    @PermCode(code = "view", nameCn = "通道商户查看", nameEn = "Channel Merchant View")
    @Operation(summary = "根据通道商户号查询斗拱通道商户配置")
    @GetMapping("/find-by-channel-mch-no")
    public Result<DougongIsvChannelMerchantResult> findByChannelMchNo(
            @NotBlank(message = "{validation.field.channelMchNo.notBlank}") String channelMchNo) {
        return Res.ok(dougongIsvChannelMerchantService.findByChannelMchNo(channelMchNo));
    }

    @PermCode(code = "manage", nameCn = "通道商户管理", nameEn = "Channel Merchant Manage")
    @Operation(summary = "创建斗拱通道商户")
    @PostMapping("/create")
    public Result<Void> create(@RequestBody @Validated DougongIsvChannelMerchantCreateParam param) {
        dougongIsvChannelMerchantService.create(param);
        return Res.ok();
    }

    @PermCode(code = "manage", nameCn = "通道商户管理", nameEn = "Channel Merchant Manage")
    @Operation(summary = "更新商户AppId")
    @PostMapping("/update-app-id")
    public Result<Void> updateAppId(
            @NotBlank(message = "{validation.field.channelMerchantNo.notBlank}") String channelMchNo,
            @NotBlank(message = "{validation.field.dougongAppId.notBlank}") String appId) {
        dougongIsvChannelMerchantService.updateAppId(channelMchNo, appId);
        return Res.ok();
    }
}
