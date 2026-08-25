package cn.daxpay.open.payment.app.admin.controller.merchant.config;

import cn.daxpay.open.payment.admin.service.merchant.route.PayRouteConfigService;
import cn.daxpay.open.payment.masterdata.result.provider.PayProviderMethodResult;
import cn.daxpay.open.payment.masterdata.service.provider.PayProviderMethodService;
import cn.daxpay.open.payment.route.param.basic.PayRouteBasicConfigBatchParam;
import cn.daxpay.open.payment.route.param.strategy.PayRouteStrategyParam;
import cn.daxpay.open.payment.route.result.basic.PayRouteBasicConfigResult;
import cn.daxpay.open.payment.route.result.scene.PayRouteSceneConfigResult;
import cn.daxpay.open.payment.route.result.strategy.PayRouteStrategyResult;
import cn.daxpay.open.platform.core.annotation.PermCode;
import cn.daxpay.open.platform.core.code.PermCodes;
import cn.daxpay.open.platform.core.rest.Res;
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

/// 小程序管理端-应用通道路由管理
///
/// 镜像自 admin 版 `PayRouteAdminController`(admin 为运营/商户双路径, 小程序端仅保留
/// /app-admin/merchant/pay-route 单路径), 同权限码同 Service; 仅镜像小程序端所需方法,
/// 场景模式配置在小程序端只读。
@PermCode(menuCode = PermCodes.Merchant.AppRoute.MENU)
@Validated
@Tag(name = "小程序管理端-应用通道路由管理")
@RestController
@RequestMapping("/app-admin/merchant/pay-route")
@RequiredArgsConstructor
public class AppAdminPayRouteController {

    private final PayRouteConfigService configService;
    private final PayProviderMethodService payProviderMethodService;

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "获取或初始化应用路由策略")
    @GetMapping("/strategy/get-or-init-by-app-id")
    public Result<PayRouteStrategyResult> getOrInitByAppId(@NotBlank(message = "{validation.field.appId.notBlank}") String appId) {
        return Res.ok(configService.getOrInitByAppId(appId));
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "更新路由策略")
    @PostMapping("/strategy/update")
    public Result<PayRouteStrategyResult> updateStrategy(@RequestBody @Validated PayRouteStrategyParam param) {
        return Res.ok(configService.updateStrategy(param));
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "已启用渠道支付方式扁平列表")
    @GetMapping("/method-directory/flat-list")
    public Result<List<PayProviderMethodResult>> listMethodDirectoryFlat() {
        return Res.ok(payProviderMethodService.listDirectoryFlat());
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "查询基础模式配置列表")
    @GetMapping("/basic-config/list-by-app-id")
    public Result<List<PayRouteBasicConfigResult>> listBasicByAppId(@NotBlank(message = "{validation.field.appId.notBlank}") String appId) {
        return Res.ok(configService.listBasicByAppId(appId));
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "批量保存基础模式配置")
    @PostMapping("/basic-config/save-batch")
    public Result<Void> saveBasicBatch(@RequestBody @Validated PayRouteBasicConfigBatchParam param) {
        configService.saveBasicBatch(param);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "查询场景模式配置列表(小程序端只读)")
    @GetMapping("/scene-config/list-by-app-id")
    public Result<List<PayRouteSceneConfigResult>> listSceneByAppId(@NotBlank(message = "{validation.field.appId.notBlank}") String appId) {
        return Res.ok(configService.listSceneByAppId(appId));
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "目录项与通道商户下支付能力候选")
    @GetMapping("/scene-config/capability-candidates")
    public Result<List<LabelValue>> listSceneCapabilityCandidates(
            @NotBlank(message = "{validation.field.appId.notBlank}") String appId,
            @NotBlank(message = "{validation.field.provider.notBlank}") String provider,
            @NotBlank(message = "{validation.field.method.notBlank}") String method,
            @NotBlank(message = "{validation.field.channelMchNo.notBlank}") String channelMchNo) {
        return Res.ok(configService.listSceneCapabilityCandidatesForMethod(appId, provider, method, channelMchNo));
    }
}