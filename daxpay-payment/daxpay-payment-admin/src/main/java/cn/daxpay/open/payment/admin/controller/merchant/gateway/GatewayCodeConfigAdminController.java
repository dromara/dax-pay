package cn.daxpay.open.payment.admin.controller.merchant.gateway;

import cn.daxpay.open.payment.merchant.param.gateway.GatewayCodeConfigParam;
import cn.daxpay.open.payment.merchant.result.gateway.GatewayCodeConfigResult;
import cn.daxpay.open.payment.merchant.service.gateway.GatewayCodeConfigService;
import cn.daxpay.open.payment.route.service.support.PayRouteStrategyCapabilitySupport;
import cn.daxpay.open.platform.core.annotation.PermCode;
import cn.daxpay.open.platform.core.code.PermCodes;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.result.Result;
import cn.daxpay.open.platform.core.rest.dto.ChannelMchOption;
import cn.daxpay.open.platform.core.rest.dto.LabelValue;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/// # 码牌支付策略配置(管理)
@PermCode(menuCode = PermCodes.Merchant.GatewayCode.MENU)
@Validated
@Tag(name = "码牌支付策略配置")
@RestController
@RequestMapping("/admin/gateway/code-config")
@RequiredArgsConstructor
public class GatewayCodeConfigAdminController {

    private final GatewayCodeConfigService gatewayCodeConfigService;
    private final PayRouteStrategyCapabilitySupport payRouteStrategyCapabilitySupport;

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

    /// DIRECT 模式: 按商户+支付渠道列通道商户(不绑死默认 JSAPI method)
    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "直接指定-通道商户候选")
    @GetMapping("/direct-channel-mch-candidates")
    public Result<List<ChannelMchOption>> listDirectChannelMchCandidates(
            @NotBlank(message = "{validation.field.mchNo.notBlank}") String mchNo,
            @NotBlank(message = "{validation.field.provider.notBlank}") String provider) {
        return Res.ok(payRouteStrategyCapabilitySupport.listDirectChannelMchCandidates(mchNo, provider));
    }

    /// DIRECT 模式: 按通道商户列全部已挂载支付能力(不按默认 method 过滤，便于选 H5/主扫等)
    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "直接指定-支付能力候选")
    @GetMapping("/direct-capability-candidates")
    public Result<List<LabelValue>> listDirectCapabilityCandidates(
            @NotBlank(message = "{validation.field.channelMchNo.notBlank}") String channelMchNo) {
        return Res.ok(payRouteStrategyCapabilitySupport.listDirectCapabilityCandidates(channelMchNo));
    }
}
