package cn.daxpay.open.channel.yizhifubj.controller.direct;

import cn.daxpay.open.channel.yizhifubj.param.direct.YizhifubjDirectChannelMerchantCreateParam;
import cn.daxpay.open.channel.yizhifubj.param.direct.YizhifubjDirectKeyConfigParam;
import cn.daxpay.open.channel.yizhifubj.result.direct.YizhifubjDirectKeyConfigResult;
import cn.daxpay.open.channel.yizhifubj.service.direct.YizhifubjDirectChannelMerchantService;
import cn.daxpay.open.channel.yizhifubj.service.direct.YizhifubjDirectKeyConfigService;
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

import java.util.Objects;

/// # 一直付直连通道商户管理
///
/// 提供通道商户创建和密钥配置管理。
/// 商户身份(一直付商户编号)创建时录入, 服务端ID/报备序列号/商户 pfx 证书由密钥配置维护。
@PermCode(menuCode = PermCodes.Channel.Merchant.MENU)
@Validated
@Tag(name = "一直付直连通道商户管理")
@RestController
@RequestMapping("/admin/yizhifubj/direct-channel-merchant")
@RequiredArgsConstructor
public class YizhifubjDirectChannelMerchantController {

    private final YizhifubjDirectChannelMerchantService yizhifubjDirectChannelMerchantService;
    private final YizhifubjDirectKeyConfigService yizhifubjDirectKeyConfigService;

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "创建一直付直连通道商户")
    @PostMapping("/create")
    public Result<Void> create(@RequestBody @Validated YizhifubjDirectChannelMerchantCreateParam param) {
        yizhifubjDirectChannelMerchantService.create(param);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "根据通道商户号查询密钥配置")
    @GetMapping("/find-key-config")
    public Result<YizhifubjDirectKeyConfigResult> findKeyConfig(
            @NotBlank(message = "{validation.field.channelMerchantNo.notBlank}") String channelMchNo,
            @NotNull(message = "{validation.field.sandbox.notNull}") Boolean sandbox) {
        var config = yizhifubjDirectKeyConfigService.findByChannelMchNo(channelMchNo, sandbox);
        var result = config.toResult();
        result.setCertPfxConfigured(Objects.nonNull(config.getCertPfx()));
        result.setCertPasswordConfigured(Objects.nonNull(config.getCertPassword()));
        result.setPlatformCertConfigured(Objects.nonNull(config.getPlatformCert()));
        return Res.ok(result);
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "保存密钥配置")
    @PostMapping("/save-key-config")
    public Result<Void> saveKeyConfig(@RequestBody @Validated YizhifubjDirectKeyConfigParam param) {
        yizhifubjDirectKeyConfigService.save(param);
        return Res.ok();
    }
}
