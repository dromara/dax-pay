package cn.daxpay.open.payment.app.merchant.controller.route;

import cn.daxpay.open.payment.app.merchant.service.route.AppMerchantPayRouteService;
import cn.daxpay.open.payment.masterdata.result.provider.PayProviderMethodResult;
import cn.daxpay.open.payment.route.param.basic.PayRouteBasicConfigBatchParam;
import cn.daxpay.open.payment.route.param.scene.PayRouteSceneCapabilityBatchParam;
import cn.daxpay.open.payment.route.param.scene.PayRouteSceneConfigBatchParam;
import cn.daxpay.open.payment.route.param.strategy.PayRouteStrategyParam;
import cn.daxpay.open.platform.core.annotation.PermCode;
import cn.daxpay.open.platform.core.code.PermCodes;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.dto.ChannelMchOption;
import cn.daxpay.open.platform.core.rest.dto.LabelValue;
import cn.daxpay.open.platform.core.rest.result.Result;
import cn.daxpay.open.payment.route.result.basic.PayRouteBasicConfigResult;
import cn.daxpay.open.payment.route.result.scene.PayRouteSceneConfigResult;
import cn.daxpay.open.payment.route.result.strategy.PayRouteStrategyResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/// # 商户应用通道路由(商户移动端)
///
/// 面向商户移动端的通道路由配置。业务编排委托 [AppMerchantPayRouteService]。
@PermCode(menuCode = PermCodes.Merchant.AppRoute.MENU)
@Validated
@Tag(name = "应用通道路由管理(商户移动端)")
@RestController
@RequestMapping("/app-merchant/pay-route")
@RequiredArgsConstructor
public class AppMerchantPayRouteController {

    private final AppMerchantPayRouteService payRouteService;

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "已启用渠道支付方式扁平列表")
    @GetMapping("/method-directory/flat-list")
    public Result<List<PayProviderMethodResult>> listMethodDirectoryFlat() {
        return Res.ok(payRouteService.listMethodDirectoryFlat());
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "获取或初始化应用路由策略")
    @GetMapping("/strategy/get-or-init-by-app-id")
    public Result<PayRouteStrategyResult> getOrInitByAppId(
            @NotBlank(message = "{validation.field.appId.notBlank}") String appId) {
        return Res.ok(payRouteService.getOrInitByAppId(appId));
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "更新路由策略")
    @PostMapping("/strategy/update")
    public Result<PayRouteStrategyResult> updateStrategy(@RequestBody @Validated PayRouteStrategyParam param) {
        return Res.ok(payRouteService.updateStrategy(param));
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "查询场景模式配置列表")
    @GetMapping("/scene-config/list-by-app-id")
    public Result<List<PayRouteSceneConfigResult>> listSceneByAppId(
            @NotBlank(message = "{validation.field.appId.notBlank}") String appId) {
        return Res.ok(payRouteService.listSceneByAppId(appId));
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "批量保存场景模式配置")
    @PostMapping("/scene-config/save-batch")
    public Result<Void> saveSceneBatch(@RequestBody @Validated PayRouteSceneConfigBatchParam param) {
        payRouteService.saveSceneBatch(param);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "通道路由白名单目录下全部通道商户候选（批量）")
    @GetMapping("/scene-config/channel-mh-candidates-batch")
    public Result<Map<String, List<ChannelMchOption>>> listSceneChannelMhCandidatesBatch(
            @NotBlank(message = "{validation.field.appId.notBlank}") String appId) {
        return Res.ok(payRouteService.listSceneChannelMhCandidatesBatch(appId));
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "按目录项与通道商户批量返回支付能力候选")
    @PostMapping("/scene-config/capability-candidates-batch")
    public Result<Map<String, List<LabelValue>>> listSceneCapabilityCandidatesBatch(
            @Valid @RequestBody PayRouteSceneCapabilityBatchParam param) {
        return Res.ok(payRouteService.listSceneCapabilityCandidatesBatch(param));
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "目录项下商户已开通的通道商户候选")
    @GetMapping("/scene-config/channel-mh-candidates")
    public Result<List<ChannelMchOption>> listSceneChannelMhCandidates(
            @NotBlank(message = "{validation.field.appId.notBlank}") String appId,
            @NotBlank(message = "{validation.field.provider.notBlank}") String provider,
            @NotBlank(message = "{validation.field.method.notBlank}") String method) {
        return Res.ok(payRouteService.listSceneChannelMhCandidates(appId, provider, method));
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "目录项与通道商户下支付能力候选")
    @GetMapping("/scene-config/capability-candidates")
    public Result<List<LabelValue>> listSceneCapabilityCandidates(
            @NotBlank(message = "{validation.field.appId.notBlank}") String appId,
            @NotBlank(message = "{validation.field.provider.notBlank}") String provider,
            @NotBlank(message = "{validation.field.method.notBlank}") String method,
            @NotBlank(message = "{validation.field.channelMchNo.notBlank}") String channelMchNo) {
        return Res.ok(payRouteService.listSceneCapabilityCandidates(appId, provider, method, channelMchNo));
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "查询基础模式配置列表")
    @GetMapping("/basic-config/list-by-app-id")
    public Result<List<PayRouteBasicConfigResult>> listBasicByAppId(
            @NotBlank(message = "{validation.field.appId.notBlank}") String appId) {
        return Res.ok(payRouteService.listBasicByAppId(appId));
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "批量保存基础模式配置")
    @PostMapping("/basic-config/save-batch")
    public Result<Void> saveBasicBatch(@RequestBody @Validated PayRouteBasicConfigBatchParam param) {
        payRouteService.saveBasicBatch(param);
        return Res.ok();
    }
}
