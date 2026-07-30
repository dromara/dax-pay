package cn.daxpay.open.payment.admin.controller.merchant.gateway;

import cn.daxpay.open.payment.merchant.param.gateway.GatewayPayConfigParam;
import cn.daxpay.open.payment.merchant.result.gateway.GatewayPayConfigResult;
import cn.daxpay.open.payment.merchant.service.gateway.GatewayPayConfigService;
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

/// # 网关支付配置(管理, 码牌/聚合共用)
@PermCode(menuCode = PermCodes.Merchant.GatewayPayConfig.MENU)
@Validated
@Tag(name = "网关支付配置")
@RestController
@RequestMapping("/admin/gateway/pay-config")
@RequiredArgsConstructor
public class GatewayPayConfigAdminController {

    private final GatewayPayConfigService gatewayPayConfigService;
    private final PayRouteStrategyCapabilitySupport payRouteStrategyCapabilitySupport;

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "按应用查询网关支付配置")
    @GetMapping("/get-by-app-id")
    public Result<GatewayPayConfigResult> getByAppId(
            @NotBlank(message = "{validation.field.appId.notBlank}") String appId) {
        return Res.ok(gatewayPayConfigService.findByAppId(appId));
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "保存或更新网关支付配置")
    @PostMapping("/save-or-update")
    public Result<Void> saveOrUpdate(@RequestBody @Validated GatewayPayConfigParam param) {
        gatewayPayConfigService.saveOrUpdate(param);
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
