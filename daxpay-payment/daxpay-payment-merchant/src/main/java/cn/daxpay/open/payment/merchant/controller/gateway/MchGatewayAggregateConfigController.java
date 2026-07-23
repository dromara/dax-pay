package cn.daxpay.open.payment.merchant.controller.gateway;

import cn.daxpay.open.payment.common.context.PaymentContext;
import cn.daxpay.open.payment.merchant.param.gateway.GatewayAggregateConfigParam;
import cn.daxpay.open.payment.merchant.result.appinfo.MchAppInfoResult;
import cn.daxpay.open.payment.merchant.result.gateway.GatewayAggregateConfigResult;
import cn.daxpay.open.payment.merchant.service.appinfo.MchAppInfoService;
import cn.daxpay.open.payment.merchant.service.gateway.GatewayAggregateConfigService;
import cn.daxpay.open.platform.core.annotation.PermCode;
import cn.daxpay.open.platform.core.code.CommonCode;
import cn.daxpay.open.platform.core.code.PermCodes;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/// # 网关聚合扫码配置（商户端）
///
/// 对照运营端 [GatewayAggregateConfigAdminController]，路径 `/mch/gateway/aggregate-config`。
/// 委托 [GatewayAggregateConfigService]；写操作强制当前上下文 mchNo，并通过 [MchAppInfoService#findByAppId] 校验应用归属。
@PermCode(menuCode = PermCodes.Merchant.GatewayAggregate.MENU)
@Validated
@Tag(name = "网关聚合扫码配置(商户端)")
@RestController
@RequestMapping("/mch/gateway/aggregate-config")
@RequiredArgsConstructor
public class MchGatewayAggregateConfigController {

    private final GatewayAggregateConfigService gatewayAggregateConfigService;
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
    @Operation(summary = "按应用查询聚合扫码配置")
    @GetMapping("/get-by-app-id")
    public Result<GatewayAggregateConfigResult> getByAppId(
            @NotBlank(message = "{validation.field.appId.notBlank}") String appId) {
        this.assertAppOwned(appId);
        return Res.ok(gatewayAggregateConfigService.findByAppId(appId));
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "保存或更新聚合扫码配置")
    @PostMapping("/save-or-update")
    public Result<Void> saveOrUpdate(@RequestBody @Validated GatewayAggregateConfigParam param) {
        MchAppInfoResult app = this.assertAppOwned(param.getAppId());
        // 强制当前商户号，忽略客户端传入（防越权）
        param.setMchNo(app.getMchNo() != null ? app.getMchNo() : this.requireMchNo());
        gatewayAggregateConfigService.saveOrUpdate(param);
        return Res.ok();
    }
}
