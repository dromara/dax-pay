package cn.daxpay.open.payment.app.admin.controller.merchant.config;

import cn.daxpay.open.payment.merchant.param.gateway.GatewayPayConfigParam;
import cn.daxpay.open.payment.merchant.result.gateway.GatewayPayConfigResult;
import cn.daxpay.open.payment.merchant.service.gateway.GatewayPayConfigService;
import cn.daxpay.open.payment.route.service.support.PayRouteStrategyCapabilitySupport;
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

/// 小程序管理端-网关支付配置管理
///
/// 镜像自 admin 版 `GatewayPayConfigAdminController`(路径 /admin/gateway/pay-config),
/// 同权限码同 Service, 仅保留小程序端使用到的端点; 商户数据按 mchNo 行级隔离。
@PermCode(menuCode = PermCodes.Merchant.GatewayPayConfig.MENU)
@Validated
@Tag(name = "小程序管理端-网关支付配置")
@RestController
@RequestMapping("/app-admin/gateway/pay-config")
@RequiredArgsConstructor
public class AppAdminGatewayPayConfigController {

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

    /// DIRECT 模式: 按商户+支付渠道列通道商户
    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "直接指定-通道商户候选")
    @GetMapping("/direct-channel-mch-candidates")
    public Result<List<ChannelMchOption>> listDirectChannelMchCandidates(
            @NotBlank(message = "{validation.field.mchNo.notBlank}") String mchNo,
            @NotBlank(message = "{validation.field.provider.notBlank}") String provider) {
        return Res.ok(payRouteStrategyCapabilitySupport.listDirectChannelMchCandidates(mchNo, provider));
    }

    /// DIRECT 模式: 按通道商户列全部已挂载支付能力
    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "直接指定-支付能力候选")
    @GetMapping("/direct-capability-candidates")
    public Result<List<LabelValue>> listDirectCapabilityCandidates(
            @NotBlank(message = "{validation.field.channelMchNo.notBlank}") String channelMchNo) {
        return Res.ok(payRouteStrategyCapabilitySupport.listDirectCapabilityCandidates(channelMchNo));
    }
}
