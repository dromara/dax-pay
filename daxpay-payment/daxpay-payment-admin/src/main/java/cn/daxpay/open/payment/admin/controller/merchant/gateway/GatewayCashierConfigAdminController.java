package cn.daxpay.open.payment.admin.controller.merchant.gateway;

import cn.daxpay.open.payment.merchant.param.gateway.GatewayCashierItemParam;
import cn.daxpay.open.payment.merchant.result.gateway.GatewayCashierItemResult;
import cn.daxpay.open.payment.admin.service.merchant.gateway.GatewayCashierConfigService;
import cn.daxpay.open.platform.core.annotation.PermCode;
import cn.daxpay.open.platform.core.code.PermCodes;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.result.Result;
import cn.daxpay.open.platform.core.validation.ValidationGroup;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/// # 网关收银台配置(管理)
@PermCode(menuCode = PermCodes.Merchant.GatewayCashier.MENU)
@Validated
@Tag(name = "网关收银台配置")
@RestController
/// 双路径: 运营端 /admin/... 与商户端 /mch/... 共用同一 Service；
/// 客户端隔离由网关 ClientCode + @PermCode 菜单权限保证；商户端 TenantLine 按 mchNo 行级隔离。
@RequestMapping({"/admin/gateway/cashier-config", "/mch/gateway/cashier-config"})
@RequiredArgsConstructor
public class GatewayCashierConfigAdminController {

    private final GatewayCashierConfigService gatewayCashierConfigService;

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "按应用与分桶查询收银台支付项列表")
    @GetMapping("/list")
    public Result<List<GatewayCashierItemResult>> list(
            @NotBlank(message = "{validation.field.appId.notBlank}") String appId,
            @NotBlank(message = "{validation.field.cashierType.notBlank}") String cashierType,
            String clientEnv) {
        return Res.ok(gatewayCashierConfigService.list(appId, cashierType, clientEnv));
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "按ID查询收银台支付项")
    @GetMapping("/get-by-id")
    public Result<GatewayCashierItemResult> getById(
            @NotNull(message = "{validation.field.id.notNull}") Long id) {
        return Res.ok(gatewayCashierConfigService.findById(id));
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "新建收银台支付项")
    @PostMapping("/save")
    public Result<Void> save(@RequestBody @Validated GatewayCashierItemParam param) {
        gatewayCashierConfigService.save(param);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "更新收银台支付项")
    @PostMapping("/update")
    public Result<Void> update(@RequestBody @Validated({jakarta.validation.groups.Default.class, ValidationGroup.edit.class}) GatewayCashierItemParam param) {
        gatewayCashierConfigService.update(param);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "删除收银台支付项")
    @PostMapping("/delete")
    public Result<Void> delete(@NotNull(message = "{validation.field.id.notNull}") Long id) {
        gatewayCashierConfigService.delete(id);
        return Res.ok();
    }
}
