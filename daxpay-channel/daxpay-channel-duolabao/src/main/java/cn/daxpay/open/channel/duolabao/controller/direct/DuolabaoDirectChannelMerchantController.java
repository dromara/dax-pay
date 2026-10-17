package cn.daxpay.open.channel.duolabao.controller.direct;

import cn.daxpay.open.channel.duolabao.param.direct.DuolabaoDirectChannelMerchantCreateParam;
import cn.daxpay.open.channel.duolabao.param.direct.DuolabaoDirectKeyConfigParam;
import cn.daxpay.open.channel.duolabao.result.direct.DuolabaoDirectKeyConfigResult;
import cn.daxpay.open.channel.duolabao.service.direct.DuolabaoDirectChannelMerchantService;
import cn.daxpay.open.channel.duolabao.service.direct.DuolabaoDirectKeyConfigService;
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

/// # 哆啦宝直连通道商户管理
///
/// 提供通道商户创建和密钥配置管理。
/// 商户身份(商户编号/店铺编号/代理商编号)创建时录入, 密钥对由密钥配置维护。
@PermCode(menuCode = PermCodes.Channel.Merchant.MENU)
@Validated
@Tag(name = "哆啦宝直连通道商户管理")
@RestController
@RequestMapping("/admin/duolabao/direct-channel-merchant")
@RequiredArgsConstructor
public class DuolabaoDirectChannelMerchantController {

    private final DuolabaoDirectChannelMerchantService duolabaoDirectChannelMerchantService;

    private final DuolabaoDirectKeyConfigService duolabaoDirectKeyConfigService;

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "创建哆啦宝直连通道商户")
    @PostMapping("/create")
    public Result<Void> create(@RequestBody @Validated DuolabaoDirectChannelMerchantCreateParam param) {
        duolabaoDirectChannelMerchantService.create(param);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "根据通道商户号查询密钥配置")
    @GetMapping("/find-key-config")
    public Result<DuolabaoDirectKeyConfigResult> findKeyConfig(
            @NotBlank(message = "{validation.field.channelMerchantNo.notBlank}") String channelMchNo,
            @NotNull(message = "{validation.field.sandbox.notNull}") Boolean sandbox) {
        var config = duolabaoDirectKeyConfigService.findByChannelMchNo(channelMchNo, sandbox);
        var result = config.toResult();
        result.setAccessKeyConfigured(Objects.nonNull(config.getAccessKey()));
        result.setSecretKeyConfigured(Objects.nonNull(config.getSecretKey()));
        return Res.ok(result);
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "保存密钥配置")
    @PostMapping("/save-key-config")
    public Result<Void> saveKeyConfig(@RequestBody @Validated DuolabaoDirectKeyConfigParam param) {
        duolabaoDirectKeyConfigService.save(param);
        return Res.ok();
    }
}
