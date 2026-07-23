package cn.daxpay.open.payment.merchant.controller.gateway;

import cn.daxpay.open.payment.common.context.PaymentContext;
import cn.daxpay.open.payment.merchant.param.gateway.GatewayCodeConfigParam;
import cn.daxpay.open.payment.merchant.result.appinfo.MchAppInfoResult;
import cn.daxpay.open.payment.merchant.result.gateway.GatewayCodeConfigResult;
import cn.daxpay.open.payment.merchant.service.appinfo.MchAppInfoService;
import cn.daxpay.open.payment.merchant.service.gateway.GatewayCodeConfigService;
import cn.daxpay.open.payment.route.service.support.PayRouteStrategyCapabilitySupport;
import cn.daxpay.open.platform.core.annotation.PermCode;
import cn.daxpay.open.platform.core.code.CommonCode;
import cn.daxpay.open.platform.core.code.PermCodes;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.dto.ChannelMchOption;
import cn.daxpay.open.platform.core.rest.dto.LabelValue;
import cn.daxpay.open.platform.core.rest.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/// # 码牌支付策略配置（商户端）
///
/// 对照运营端 [GatewayCodeConfigAdminController]，路径 `/mch/gateway/code-config`。
/// 委托 [GatewayCodeConfigService]；写操作强制当前上下文 mchNo，并通过 [MchAppInfoService#findByAppId] 校验应用归属。
@PermCode(menuCode = PermCodes.Merchant.GatewayCode.MENU)
@Validated
@Tag(name = "码牌支付策略配置(商户端)")
@RestController
@RequestMapping("/mch/gateway/code-config")
@RequiredArgsConstructor
public class MchGatewayCodeConfigController {

    private final GatewayCodeConfigService gatewayCodeConfigService;
    private final PayRouteStrategyCapabilitySupport payRouteStrategyCapabilitySupport;
    private final MchAppInfoService mchAppInfoService;
    private final PaymentContext paymentContext;

    /// 当前登录商户号
    private String requireMchNo() {
        String mchNo = paymentContext.getMchNo();
        if (mchNo == null || mchNo.isBlank()) {
            // 商户上下文缺失
            throw new BizInfoException(CommonCode.FAIL_CODE, "pay.error.assist.mchContextMissing");
        }
        return mchNo;
    }

    /// 校验 appId 属于当前商户（findByAppId 内含 checkApp）
    private MchAppInfoResult assertAppOwned(String appId) {
        return mchAppInfoService.findByAppId(appId);
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "按应用查询码牌支付配置")
    @GetMapping("/get-by-app-id")
    public Result<GatewayCodeConfigResult> getByAppId(
            @NotBlank(message = "{validation.field.appId.notBlank}") String appId) {
        this.assertAppOwned(appId);
        return Res.ok(gatewayCodeConfigService.findByAppId(appId));
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "保存或更新码牌支付配置")
    @PostMapping("/save-or-update")
    public Result<Void> saveOrUpdate(@RequestBody @Validated GatewayCodeConfigParam param) {
        MchAppInfoResult app = this.assertAppOwned(param.getAppId());
        // 强制当前商户号，忽略客户端传入（防越权）
        param.setMchNo(app.getMchNo() != null ? app.getMchNo() : this.requireMchNo());
        gatewayCodeConfigService.saveOrUpdate(param);
        return Res.ok();
    }

    /// DIRECT 模式: 按当前商户+支付渠道列通道商户（忽略请求 mchNo）
    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "直接指定-通道商户候选")
    @GetMapping("/direct-channel-mch-candidates")
    public Result<List<ChannelMchOption>> listDirectChannelMchCandidates(
            @NotBlank(message = "{validation.field.provider.notBlank}") String provider) {
        return Res.ok(payRouteStrategyCapabilitySupport.listDirectChannelMchCandidates(
                this.requireMchNo(), provider));
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
