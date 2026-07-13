package cn.daxpay.open.channel.douyin.controller.direct;


import cn.daxpay.open.platform.core.code.PermCodes;
import cn.daxpay.open.platform.core.annotation.PermCode;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.result.Result;
import cn.daxpay.open.channel.douyin.param.direct.DouyinDirectChannelMerchantCreateParam;
import cn.daxpay.open.channel.douyin.param.direct.DouyinDirectKeyConfigParam;
import cn.daxpay.open.channel.douyin.result.direct.DouyinDirectChannelMerchantResult;
import cn.daxpay.open.channel.douyin.result.direct.DouyinDirectKeyConfigResult;
import cn.daxpay.open.channel.douyin.service.direct.DouyinDirectChannelMerchantService;
import cn.daxpay.open.channel.douyin.service.direct.DouyinDirectKeyConfigService;
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

/// # 抖音直连通道商户管理
///
@PermCode(menuCode = PermCodes.Channel.Merchant.MENU)
@Validated
@Tag(name = "抖音直连通道商户管理")
@RestController
@RequestMapping("/admin/douyin/direct-channel-merchant")
@RequiredArgsConstructor
public class DouyinDirectChannelMerchantController {

    private final DouyinDirectChannelMerchantService douyinDirectChannelMerchantService;
    private final DouyinDirectKeyConfigService douyinDirectKeyConfigService;

    @PermCode(code = PermCodes.Action.VIEW, nameCn = PermCodes.Channel.Merchant.VIEW_NAME_CN, nameEn = PermCodes.Channel.Merchant.VIEW_NAME_EN)
    @Operation(summary = "根据通道商户号查询抖音直连通道商户配置")
    @GetMapping("/find-by-channel-mch-no")
    public Result<DouyinDirectChannelMerchantResult> findByChannelMchNo(
            @NotBlank(message = "{validation.field.channelMerchantNo.notBlank}") String channelMchNo) {
        return Res.ok(douyinDirectChannelMerchantService.findByChannelMchNo(channelMchNo));
    }

    @PermCode(code = PermCodes.Action.MANAGE, nameCn = PermCodes.Channel.Merchant.MANAGE_NAME_CN, nameEn = PermCodes.Channel.Merchant.MANAGE_NAME_EN)
    @Operation(summary = "创建抖音直连通道商户")
    @PostMapping("/create")
    public Result<Void> create(@RequestBody @Validated DouyinDirectChannelMerchantCreateParam param) {
        douyinDirectChannelMerchantService.create(param);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.VIEW, nameCn = PermCodes.Channel.Merchant.VIEW_NAME_CN, nameEn = PermCodes.Channel.Merchant.VIEW_NAME_EN)
    @Operation(summary = "根据通道商户号查询密钥配置")
    @GetMapping("/find-key-config")
    public Result<DouyinDirectKeyConfigResult> findKeyConfig(
            @NotBlank(message = "{validation.field.channelMerchantNo.notBlank}") String channelMchNo) {
        var config = douyinDirectKeyConfigService.findByChannelMchNo(channelMchNo);
        var result = config.toResult();
        result.setPrivateKeyConfigured(config.getMerchantPrivateKey() != null);
        result.setEncryptKeyConfigured(config.getEncryptKey() != null);
        return Res.ok(result);
    }

    @PermCode(code = PermCodes.Action.MANAGE, nameCn = PermCodes.Channel.Merchant.MANAGE_NAME_CN, nameEn = PermCodes.Channel.Merchant.MANAGE_NAME_EN)
    @Operation(summary = "保存密钥配置")
    @PostMapping("/save-key-config")
    public Result<Void> saveKeyConfig(@RequestBody @Validated DouyinDirectKeyConfigParam param) {
        douyinDirectKeyConfigService.save(param);
        return Res.ok();
    }
}
