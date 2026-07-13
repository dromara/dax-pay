package cn.daxpay.open.payment.admin.controller.merchant.route;


import cn.daxpay.open.platform.core.code.PermCodes;
import cn.daxpay.open.payment.masterdata.constants.provider.result.PayProviderMethodResult;
import cn.daxpay.open.payment.masterdata.constants.provider.service.PayProviderMethodService;
import cn.daxpay.open.payment.merchant.param.route.basic.PayRouteBasicConfigBatchParam;
import cn.daxpay.open.payment.merchant.param.route.scene.PayRouteSceneCapabilityBatchParam;
import cn.daxpay.open.payment.merchant.param.route.scene.PayRouteSceneConfigBatchParam;
import cn.daxpay.open.payment.merchant.param.route.strategy.PayRouteStrategyParam;
import cn.daxpay.open.platform.core.rest.dto.LabelValue;
import cn.daxpay.open.payment.merchant.result.route.basic.PayRouteBasicConfigResult;
import cn.daxpay.open.payment.merchant.result.route.scene.PayRouteSceneConfigResult;
import cn.daxpay.open.payment.merchant.result.route.strategy.PayRouteStrategyResult;
import cn.daxpay.open.payment.admin.service.merchant.route.PayRouteConfigService;
import cn.daxpay.open.platform.core.annotation.PermCode;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.result.Result;
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
/// 「运营」指 Admin 运营端角色，不是支付通道 channel。提供策略、基础/场景配置及
/// 已启用渠道支付方式扁平目录（`method-directory/flat-list`）。
///
@PermCode(menuCode = PermCodes.Merchant.AppRoute.MENU)
@Validated
@Tag(name = "应用通道路由管理")
@RestController
@RequestMapping("/admin/merchant/pay-route")
@RequiredArgsConstructor
public class PayRouteAdminController {
    private static final String VIEW_NAME_CN = "通道路由查看";
    private static final String VIEW_NAME_EN = "Pay Route View";
    private static final String MANAGE_NAME_CN = "通道路由管理";
    private static final String MANAGE_NAME_EN = "Pay Route Manage";


    private final PayRouteConfigService configService;
    private final PayProviderMethodService payProviderMethodService;

    @PermCode(code = PermCodes.Action.VIEW, nameCn = VIEW_NAME_CN, nameEn = VIEW_NAME_EN)
    @Operation(summary = "已启用渠道支付方式扁平列表")
    @GetMapping("/method-directory/flat-list")
    public Result<List<PayProviderMethodResult>> listMethodDirectoryFlat() {
        return Res.ok(payProviderMethodService.listDirectoryFlat());
    }

    @PermCode(code = PermCodes.Action.VIEW, nameCn = VIEW_NAME_CN, nameEn = VIEW_NAME_EN)
    @Operation(summary = "获取或初始化应用路由策略")
    @GetMapping("/strategy/get-or-init-by-app-id")
    public Result<PayRouteStrategyResult> getOrInitByAppId(@NotBlank(message = "{validation.field.appId.notBlank}") String appId) {
        return Res.ok(configService.getOrInitByAppId(appId));
    }

    @PermCode(code = PermCodes.Action.MANAGE, nameCn = MANAGE_NAME_CN, nameEn = MANAGE_NAME_EN)
    @Operation(summary = "更新路由策略")
    @PostMapping("/strategy/update")
    public Result<PayRouteStrategyResult> updateStrategy(@RequestBody @Validated PayRouteStrategyParam param) {
        return Res.ok(configService.updateStrategy(param));
    }

    @PermCode(code = PermCodes.Action.VIEW, nameCn = VIEW_NAME_CN, nameEn = VIEW_NAME_EN)
    @Operation(summary = "查询场景模式配置列表")
    @GetMapping("/scene-config/list-by-app-id")
    public Result<List<PayRouteSceneConfigResult>> listSceneByAppId(@NotBlank(message = "{validation.field.appId.notBlank}") String appId) {
        return Res.ok(configService.listSceneByAppId(appId));
    }

    @PermCode(code = PermCodes.Action.MANAGE, nameCn = MANAGE_NAME_CN, nameEn = MANAGE_NAME_EN)
    @Operation(summary = "批量保存场景模式配置")
    @PostMapping("/scene-config/save-batch")
    public Result<Void> saveSceneBatch(@RequestBody @Validated PayRouteSceneConfigBatchParam param) {
        configService.saveSceneBatch(param);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.VIEW, nameCn = VIEW_NAME_CN, nameEn = VIEW_NAME_EN)
    @Operation(summary = "通道路由白名单目录下全部通道商户候选（批量）")
    @GetMapping("/scene-config/channel-mch-candidates-batch")
    public Result<Map<String, List<LabelValue>>> listSceneChannelMchCandidatesBatch(
            @NotBlank(message = "{validation.field.appId.notBlank}") String appId) {
        return Res.ok(configService.listSceneChannelMchCandidatesBatch(appId));
    }

    @PermCode(code = PermCodes.Action.VIEW, nameCn = VIEW_NAME_CN, nameEn = VIEW_NAME_EN)
    @Operation(summary = "按目录项与通道商户批量返回支付能力候选")
    @PostMapping("/scene-config/capability-candidates-batch")
    public Result<Map<String, List<LabelValue>>> listSceneCapabilityCandidatesBatch(
            @Valid @RequestBody PayRouteSceneCapabilityBatchParam param) {
        return Res.ok(configService.listSceneCapabilityCandidatesBatch(param));
    }

    @PermCode(code = PermCodes.Action.VIEW, nameCn = VIEW_NAME_CN, nameEn = VIEW_NAME_EN)
    @Operation(summary = "目录项下商户已开通的通道商户候选")
    @GetMapping("/scene-config/channel-mch-candidates")
    public Result<List<LabelValue>> listSceneChannelMchCandidates(
            @NotBlank(message = "{validation.field.appId.notBlank}") String appId,
            @NotBlank(message = "{validation.field.provider.notBlank}") String provider,
            @NotBlank(message = "{validation.field.method.notBlank}") String method) {
        return Res.ok(configService.listSceneChannelMchCandidatesForMethod(appId, provider, method));
    }

    @PermCode(code = PermCodes.Action.VIEW, nameCn = VIEW_NAME_CN, nameEn = VIEW_NAME_EN)
    @Operation(summary = "目录项与通道商户下支付能力候选")
    @GetMapping("/scene-config/capability-candidates")
    public Result<List<LabelValue>> listSceneCapabilityCandidates(
            @NotBlank(message = "{validation.field.appId.notBlank}") String appId,
            @NotBlank(message = "{validation.field.provider.notBlank}") String provider,
            @NotBlank(message = "{validation.field.method.notBlank}") String method,
            @NotBlank(message = "{validation.field.channelMchNo.notBlank}") String channelMchNo) {
        return Res.ok(configService.listSceneCapabilityCandidatesForMethod(appId, provider, method, channelMchNo));
    }

    @PermCode(code = PermCodes.Action.VIEW, nameCn = VIEW_NAME_CN, nameEn = VIEW_NAME_EN)
    @Operation(summary = "查询基础模式配置列表")
    @GetMapping("/basic-config/list-by-app-id")
    public Result<List<PayRouteBasicConfigResult>> listBasicByAppId(@NotBlank(message = "{validation.field.appId.notBlank}") String appId) {
        return Res.ok(configService.listBasicByAppId(appId));
    }

    @PermCode(code = PermCodes.Action.MANAGE, nameCn = MANAGE_NAME_CN, nameEn = MANAGE_NAME_EN)
    @Operation(summary = "批量保存基础模式配置")
    @PostMapping("/basic-config/save-batch")
    public Result<Void> saveBasicBatch(@RequestBody @Validated PayRouteBasicConfigBatchParam param) {
        configService.saveBasicBatch(param);
        return Res.ok();
    }

}
