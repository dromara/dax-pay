package cn.daxpay.open.channel.union.controller.direct;

import cn.daxpay.open.channel.union.param.direct.UnionDirectChannelMerchantCreateParam;
import cn.daxpay.open.channel.union.param.direct.UnionDirectKeyConfigParam;
import cn.daxpay.open.channel.union.result.direct.UnionDirectKeyConfigResult;
import cn.daxpay.open.channel.union.service.direct.UnionDirectChannelMerchantService;
import cn.daxpay.open.channel.union.service.direct.UnionDirectKeyConfigService;
import cn.daxpay.open.platform.core.annotation.PermCode;
import cn.daxpay.open.platform.core.code.PermCodes;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/// # 云闪付直连通道商户管理
///
/// 提供通道商户创建和 RSA2 证书配置管理。
/// 银联商户号(merId)创建时录入, 私钥/中级/根证书由密钥配置维护。
@PermCode(menuCode = PermCodes.Channel.Merchant.MENU)
@Validated
@Tag(name = "云闪付直连通道商户管理")
@RestController
@RequestMapping("/admin/union/direct-channel-merchant")
@RequiredArgsConstructor
public class UnionDirectChannelMerchantController {

    private final UnionDirectChannelMerchantService unionDirectChannelMerchantService;
    private final UnionDirectKeyConfigService unionDirectKeyConfigService;

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "创建云闪付直连通道商户")
    @PostMapping("/create")
    public Result<Void> create(@RequestBody @Validated UnionDirectChannelMerchantCreateParam param) {
        unionDirectChannelMerchantService.create(param);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "根据通道商户号查询证书配置")
    @GetMapping("/find-key-config")
    public Result<UnionDirectKeyConfigResult> findKeyConfig(
            @NotBlank(message = "{validation.field.channelMerchantNo.notBlank}") String channelMchNo,
            @NotNull(message = "{validation.field.sandbox.notNull}") Boolean sandbox) {
        var config = unionDirectKeyConfigService.findByChannelMchNo(channelMchNo, sandbox);
        var result = config.toResult();
        result.setKeyPrivateCertConfigured(config.getKeyPrivateCert() != null);
        result.setKeyPrivateCertPwdConfigured(config.getKeyPrivateCertPwd() != null);
        return Res.ok(result);
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "保存证书配置")
    @PostMapping("/save-key-config")
    public Result<Void> saveKeyConfig(@RequestBody @Validated UnionDirectKeyConfigParam param) {
        unionDirectKeyConfigService.save(param);
        return Res.ok();
    }
}
