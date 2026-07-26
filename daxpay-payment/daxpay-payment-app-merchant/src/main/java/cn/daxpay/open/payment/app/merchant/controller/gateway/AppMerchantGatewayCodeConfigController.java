package cn.daxpay.open.payment.app.merchant.controller.gateway;

import cn.daxpay.open.payment.app.merchant.service.gateway.AppMerchantGatewayCodeConfigService;
import cn.daxpay.open.payment.merchant.param.gateway.GatewayCodeConfigParam;
import cn.daxpay.open.payment.merchant.result.gateway.GatewayCodeConfigResult;
import cn.daxpay.open.platform.core.annotation.PermCode;
import cn.daxpay.open.platform.core.code.PermCodes;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.dto.ChannelMchOption;
import cn.daxpay.open.platform.core.rest.dto.LabelValue;
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

import java.util.List;

/// # 码牌支付策略配置(商户移动端)
///
/// 面向商户移动端的码牌支付配置。业务编排委托 [AppMerchantGatewayCodeConfigService]；
/// 写操作强制当前上下文 mchNo，并通过 MchAppInfoService 校验应用归属。
@PermCode(menuCode = PermCodes.Merchant.GatewayCode.MENU)
@Validated
@Tag(name = "码牌支付策略配置(商户移动端)")
@RestController
@RequestMapping("/app-merchant/gateway/code-config")
@RequiredArgsConstructor
public class AppMerchantGatewayCodeConfigController {

    private final AppMerchantGatewayCodeConfigService gatewayCodeConfigService;

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "按应用查询码牌支付配置")
    @GetMapping("/get-by-app-id")
    public Result<GatewayCodeConfigResult> getByAppId(
            @NotBlank(message = "{validation.field.appId.notBlank}") String appId) {
        return Res.ok(gatewayCodeConfigService.findByAppId(appId));
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "保存或更新码牌支付配置")
    @PostMapping("/save-or-update")
    public Result<Void> saveOrUpdate(@RequestBody @Validated GatewayCodeConfigParam param) {
        gatewayCodeConfigService.saveOrUpdate(param);
        return Res.ok();
    }

    /// DIRECT 模式: 按当前商户+支付渠道列通道商户
    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "直接指定-通道商户候选")
    @GetMapping("/direct-channel-mch-candidates")
    public Result<List<ChannelMchOption>> listDirectChannelMchCandidates(
            @NotBlank(message = "{validation.field.provider.notBlank}") String provider) {
        return Res.ok(gatewayCodeConfigService.listDirectChannelMchCandidates(provider));
    }

    /// DIRECT 模式: 按通道商户列全部已挂载支付能力
    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "直接指定-支付能力候选")
    @GetMapping("/direct-capability-candidates")
    public Result<List<LabelValue>> listDirectCapabilityCandidates(
            @NotBlank(message = "{validation.field.channelMchNo.notBlank}") String channelMchNo) {
        return Res.ok(gatewayCodeConfigService.listDirectCapabilityCandidates(channelMchNo));
    }
}
