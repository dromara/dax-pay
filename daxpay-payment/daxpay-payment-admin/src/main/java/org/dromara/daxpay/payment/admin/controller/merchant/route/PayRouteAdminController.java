package org.dromara.daxpay.payment.admin.controller.merchant.route;

import org.dromara.daxpay.payment.pay.result.masterdata.provider.PayProviderMethodResult;
import org.dromara.daxpay.payment.pay.service.masterdata.provider.PayProviderMethodService;
import org.dromara.daxpay.payment.merchant.param.route.basic.PayRouteBasicConfigBatchParam;
import org.dromara.daxpay.payment.merchant.param.route.resolve.PayRouteSimulateParam;
import org.dromara.daxpay.payment.merchant.param.route.scene.PayRouteSceneCapabilityBatchParam;
import org.dromara.daxpay.payment.merchant.param.route.scene.PayRouteSceneConfigBatchParam;
import org.dromara.daxpay.payment.merchant.param.route.strategy.PayRouteStrategyParam;
import org.dromara.daxpay.platform.core.rest.dto.LabelValue;
import org.dromara.daxpay.payment.merchant.result.route.basic.PayRouteBasicConfigResult;
import org.dromara.daxpay.payment.merchant.result.route.resolve.PayRouteResolveResult;
import org.dromara.daxpay.payment.merchant.result.route.scene.PayRouteSceneConfigResult;
import org.dromara.daxpay.payment.merchant.result.route.strategy.PayRouteStrategyResult;
import org.dromara.daxpay.payment.merchant.service.route.facade.PayRouteConfigService;
import org.dromara.daxpay.platform.core.annotation.PermCode;
import org.dromara.daxpay.platform.core.rest.Res;
import org.dromara.daxpay.platform.core.rest.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/// # 商户应用通道路由（管理端 API）
///
/// 「运营」指 Admin 运营端角色，不是支付通道 channel。提供策略、基础/场景配置、试算及
/// 已启用渠道支付方式扁平目录（`method-directory/flat-list`）；精细模式 API 已移除。
///
@PermCode(menuCode = "payment:merchant:app:payRoute")
@Validated
@Tag(name = "应用通道路由管理")
@RestController
@RequestMapping("/admin/merchant/pay-route")
@RequiredArgsConstructor
public class PayRouteAdminController {

    private final PayRouteConfigService configService;
    private final PayProviderMethodService payProviderMethodService;

    @PermCode(code = "view", nameCn = "通道路由查看", nameEn = "Pay Route View")
    @Operation(summary = "已启用渠道支付方式扁平列表")
    @GetMapping("/method-directory/flat-list")
    public Result<List<PayProviderMethodResult>> listMethodDirectoryFlat() {
        return Res.ok(payProviderMethodService.listDirectoryFlat());
    }

    @PermCode(code = "view", nameCn = "通道路由查看", nameEn = "Pay Route View")
    @Operation(summary = "获取或初始化应用路由策略")
    @GetMapping("/strategy/get-or-init-by-app-id")
    public Result<PayRouteStrategyResult> getOrInitByAppId(@NotBlank(message = "{validation.field.appId.notBlank}") String appId) {
        return Res.ok(configService.getOrInitByAppId(appId));
    }

    @PermCode(code = "edit", nameCn = "通道路由编辑", nameEn = "Pay Route Edit")
    @Operation(summary = "更新路由策略")
    @PostMapping("/strategy/update")
    public Result<PayRouteStrategyResult> updateStrategy(@RequestBody @Validated PayRouteStrategyParam param) {
        return Res.ok(configService.updateStrategy(param));
    }

    @PermCode(code = "view", nameCn = "通道路由查看", nameEn = "Pay Route View")
    @Operation(summary = "查询场景模式配置列表")
    @GetMapping("/scene-config/list-by-app-id")
    public Result<List<PayRouteSceneConfigResult>> listSceneByAppId(@NotBlank(message = "{validation.field.appId.notBlank}") String appId) {
        return Res.ok(configService.listSceneByAppId(appId));
    }

    @PermCode(code = "edit", nameCn = "通道路由编辑", nameEn = "Pay Route Edit")
    @Operation(summary = "批量保存场景模式配置")
    @PostMapping("/scene-config/save-batch")
    public Result<Void> saveSceneBatch(@RequestBody @Validated PayRouteSceneConfigBatchParam param) {
        configService.saveSceneBatch(param);
        return Res.ok();
    }

    @PermCode(code = "view", nameCn = "通道路由查看", nameEn = "Pay Route View")
    @Operation(summary = "通道路由白名单目录下全部产品候选（批量）")
    @GetMapping("/scene-config/product-candidates-batch")
    public Result<Map<String, List<LabelValue>>> listSceneProductCandidatesBatch(
            @NotBlank(message = "{validation.field.appId.notBlank}") String appId) {
        return Res.ok(configService.listSceneProductCandidatesBatch(appId));
    }

    @PermCode(code = "view", nameCn = "通道路由查看", nameEn = "Pay Route View")
    @Operation(summary = "按目录项与产品批量返回支付能力候选")
    @PostMapping("/scene-config/capability-candidates-batch")
    public Result<Map<String, List<LabelValue>>> listSceneCapabilityCandidatesBatch(
            @Valid @RequestBody PayRouteSceneCapabilityBatchParam param) {
        return Res.ok(configService.listSceneCapabilityCandidatesBatch(param));
    }

    @PermCode(code = "view", nameCn = "通道路由查看", nameEn = "Pay Route View")
    @Operation(summary = "目录项下商户可用支付产品候选")
    @GetMapping("/scene-config/product-candidates")
    public Result<List<LabelValue>> listSceneProductCandidates(
            @NotBlank(message = "{validation.field.appId.notBlank}") String appId,
            @NotBlank(message = "{validation.field.provider.notBlank}") String provider,
            @NotBlank(message = "{validation.field.method.notBlank}") String method) {
        return Res.ok(configService.listSceneProductCandidatesForMethod(appId, provider, method));
    }

    @PermCode(code = "view", nameCn = "通道路由查看", nameEn = "Pay Route View")
    @Operation(summary = "目录项与产品下支付能力候选")
    @GetMapping("/scene-config/capability-candidates")
    public Result<List<LabelValue>> listSceneCapabilityCandidates(
            @NotBlank(message = "{validation.field.appId.notBlank}") String appId,
            @NotBlank(message = "{validation.field.provider.notBlank}") String provider,
            @NotBlank(message = "{validation.field.method.notBlank}") String method,
            @NotBlank(message = "{validation.field.product.notBlank}") String product) {
        return Res.ok(configService.listSceneCapabilityCandidatesForMethod(appId, provider, method, product));
    }

    @PermCode(code = "view", nameCn = "通道路由查看", nameEn = "Pay Route View")
    @Operation(summary = "推断目录项与产品下唯一支付能力（仅供回显）")
    @GetMapping("/scene-config/infer-capability")
    public Result<String> inferSceneCapability(
            @NotBlank(message = "{validation.field.appId.notBlank}") String appId,
            @NotBlank(message = "{validation.field.provider.notBlank}") String provider,
            @NotBlank(message = "{validation.field.method.notBlank}") String method,
            @NotBlank(message = "{validation.field.product.notBlank}") String product) {
        return Res.ok(configService.inferSceneCapability(appId, provider, method, product));
    }

    @PermCode(code = "view", nameCn = "通道路由查看", nameEn = "Pay Route View")
    @Operation(summary = "查询基础模式配置列表")
    @GetMapping("/basic-config/list-by-app-id")
    public Result<List<PayRouteBasicConfigResult>> listBasicByAppId(@NotBlank(message = "{validation.field.appId.notBlank}") String appId) {
        return Res.ok(configService.listBasicByAppId(appId));
    }

    @PermCode(code = "edit", nameCn = "通道路由编辑", nameEn = "Pay Route Edit")
    @Operation(summary = "批量保存基础模式配置")
    @PostMapping("/basic-config/save-batch")
    public Result<Void> saveBasicBatch(@RequestBody @Validated PayRouteBasicConfigBatchParam param) {
        configService.saveBasicBatch(param);
        return Res.ok();
    }

    @PermCode(code = "view", nameCn = "通道路由查看", nameEn = "Pay Route View")
    @Operation(summary = "模拟路由")
    @PostMapping("/simulate")
    public Result<PayRouteResolveResult> simulate(@RequestBody @Validated PayRouteSimulateParam param) {
        return Res.ok(configService.simulate(param));
    }
}
