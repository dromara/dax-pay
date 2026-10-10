package cn.daxpay.open.payment.app.merchant.controller.gateway;

import cn.daxpay.open.payment.app.merchant.service.gateway.AppMerchantGatewayCashierService;
import cn.daxpay.open.payment.common.context.PaymentContext;
import cn.daxpay.open.payment.merchant.param.gateway.GatewayCashierItemParam;
import cn.daxpay.open.payment.merchant.param.gateway.MchGatewayCashierItemParam;
import cn.daxpay.open.payment.merchant.result.gateway.GatewayCashierItemResult;
import cn.daxpay.open.platform.core.annotation.PermCode;
import cn.daxpay.open.platform.core.code.CommonCode;
import cn.daxpay.open.platform.core.code.PermCodes;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.result.Result;
import cn.daxpay.open.platform.core.validation.ValidationGroup;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

/// # 网关收银台配置(商户移动端)
///
/// 面向商户移动端的收银台支付项管理。业务编排委托 [AppMerchantGatewayCashierService]。
@PermCode(menuCode = PermCodes.Merchant.GatewayCashier.MENU)
@Validated
@Tag(name = "网关收银台配置(商户移动端)")
@RestController
@RequestMapping("/app-mch/gateway/cashier-config")
@RequiredArgsConstructor
public class AppMerchantGatewayCashierController {

    private final AppMerchantGatewayCashierService gatewayCashierService;
    private final PaymentContext paymentContext;

    /// 当前登录商户号（上下文必有；缺则视为会话异常）
    private String requireMchNo() {
        String mchNo = paymentContext.getMchNo();
        if (Objects.isNull(mchNo) || mchNo.isBlank()) {
            // 商户上下文缺失
            throw new BizInfoException(CommonCode.FAIL_CODE, "pay.error.assist.mchContextMissing");
        }
        return mchNo;
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "按应用与分桶查询收银台支付项列表")
    @GetMapping("/list")
    public Result<List<GatewayCashierItemResult>> list(
            @NotBlank(message = "{validation.field.appId.notBlank}") String appId,
            @NotBlank(message = "{validation.field.cashierType.notBlank}") String cashierType,
            String clientEnv) {
        return Res.ok(gatewayCashierService.list(appId, cashierType, clientEnv));
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "按ID查询收银台支付项")
    @GetMapping("/get-by-id")
    public Result<GatewayCashierItemResult> getById(
            @NotNull(message = "{validation.field.id.notNull}") Long id) {
        return Res.ok(gatewayCashierService.findById(id));
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "新建收银台支付项")
    @PostMapping("/save")
    public Result<Void> save(@RequestBody @Validated MchGatewayCashierItemParam param) {
        gatewayCashierService.save(assembleSaveParam(param));
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "更新收银台支付项")
    @PostMapping("/update")
    public Result<Void> update(@RequestBody @Validated({jakarta.validation.groups.Default.class, ValidationGroup.edit.class}) MchGatewayCashierItemParam param) {
        gatewayCashierService.update(assembleSaveParam(param));
        return Res.ok();
    }

    /// 商户端参数不含商户号, 强制取当前登录商户(防越权), 组装为完整参数
    private GatewayCashierItemParam assembleSaveParam(MchGatewayCashierItemParam param) {
        GatewayCashierItemParam saveParam = new GatewayCashierItemParam();
        saveParam.setMchNo(requireMchNo());
        saveParam.setId(param.getId());
        saveParam.setAppId(param.getAppId());
        saveParam.setCashierType(param.getCashierType());
        saveParam.setClientEnv(param.getClientEnv());
        saveParam.setName(param.getName());
        saveParam.setIcon(param.getIcon());
        saveParam.setRecommend(param.getRecommend());
        saveParam.setSortNo(param.getSortNo());
        saveParam.setResolveMode(param.getResolveMode());
        saveParam.setMethod(param.getMethod());
        saveParam.setChannelMchNo(param.getChannelMchNo());
        saveParam.setCapability(param.getCapability());
        return saveParam;
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "删除收银台支付项")
    @PostMapping("/delete")
    public Result<Void> delete(@NotNull(message = "{validation.field.id.notNull}") Long id) {
        gatewayCashierService.delete(id);
        return Res.ok();
    }
}
