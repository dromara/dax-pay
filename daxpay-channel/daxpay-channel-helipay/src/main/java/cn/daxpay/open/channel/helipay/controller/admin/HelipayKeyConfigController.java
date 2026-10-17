package cn.daxpay.open.channel.helipay.controller.admin;

import cn.daxpay.open.channel.helipay.param.HelipayKeyConfigParam;
import cn.daxpay.open.channel.helipay.result.HelipayKeyConfigResult;
import cn.daxpay.open.channel.helipay.service.config.HelipayKeyConfigService;
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

import java.util.Objects;

/// # 合利宝通道商户密钥配置
///
/// 提供通道商户维度的合利宝密钥配置管理。
/// 合利宝无独立沙箱网关, 每通道商户仅一条配置(无环境维度参数)。
@PermCode(menuCode = PermCodes.Channel.Merchant.MENU)
@Validated
@Tag(name = "合利宝通道商户密钥配置")
@RestController
@RequestMapping("/admin/helipay/key-config")
@RequiredArgsConstructor
public class HelipayKeyConfigController {

    private final HelipayKeyConfigService helipayKeyConfigService;

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "根据通道商户号查询合利宝密钥配置")
    @GetMapping("/find-config")
    public Result<HelipayKeyConfigResult> findConfig(
            @NotBlank(message = "{validation.field.channelMerchantNo.notBlank}") String channelMchNo) {
        var config = helipayKeyConfigService.findByChannelMchNo(channelMchNo);
        var result = config.toResult();
        result.setSignKeyConfigured(Objects.nonNull(config.getSignKey()));
        return Res.ok(result);
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "保存合利宝密钥配置")
    @PostMapping("/save-config")
    public Result<Void> saveConfig(@RequestBody @Validated HelipayKeyConfigParam param) {
        helipayKeyConfigService.save(param);
        return Res.ok();
    }
}
