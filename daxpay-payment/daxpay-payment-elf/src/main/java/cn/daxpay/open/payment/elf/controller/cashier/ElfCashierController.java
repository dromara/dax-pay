package cn.daxpay.open.payment.elf.controller.cashier;

import cn.daxpay.open.payment.elf.result.cashier.ElfCashierConfigOptionsResult;
import cn.daxpay.open.payment.elf.service.cashier.ElfCashierConfigService;
import cn.daxpay.open.payment.merchant.param.cashier.MchCashierOrderStatusParam;
import cn.daxpay.open.payment.merchant.param.cashier.MchCashierPayParam;
import cn.daxpay.open.payment.merchant.result.cashier.MchCashierPayResult;
import cn.daxpay.open.payment.merchant.service.cashier.MchCashierPayService;
import cn.daxpay.open.platform.core.annotation.PermCode;
import cn.daxpay.open.platform.core.code.PermCodes;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/// # 精灵收银(PC收款精灵专用端)
///
/// PC 收款精灵专属的被扫收银入口(`/elf/**`, 身份域仍 merchant), 复用普通支付全链路;
/// 业务实现复用商户模块的 [MchCashierPayService], 权限复用交易订单现有的查看/管理码,
/// 收银员账号使用内置角色 [cn.daxpay.open.platform.core.enums.role.RoleCodeEnum#MERCHANT_CASHIER]。
@Validated
@Tag(name = "精灵收银(PC收款精灵)")
@RestController
@RequestMapping("/elf/cashier")
@RequiredArgsConstructor
public class ElfCashierController {

    private final MchCashierPayService mchCashierPayService;

    private final ElfCashierConfigService elfCashierConfigService;

    @PermCode(menuCode = PermCodes.Trade.Order.MENU, code = PermCodes.Action.MANAGE)
    @Operation(summary = "收银被扫支付(付款码)")
    @PostMapping("/pay-by-auth-code")
    public Result<MchCashierPayResult> payByAuthCode(@RequestBody @Valid MchCashierPayParam param) {
        return Res.ok(mchCashierPayService.pay(param));
    }

    @PermCode(menuCode = PermCodes.Trade.Order.MENU, code = PermCodes.Action.VIEW)
    @Operation(summary = "收银订单状态查询(可选主动同步通道)")
    @GetMapping("/order-status")
    public Result<MchCashierPayResult> orderStatus(MchCashierOrderStatusParam param) {
        return Res.ok(mchCashierPayService.orderStatus(param));
    }

    @PermCode(menuCode = PermCodes.Trade.Order.MENU, code = PermCodes.Action.VIEW)
    @Operation(summary = "收银配置选项(启用门店与应用)")
    @GetMapping("/config-options")
    public Result<ElfCashierConfigOptionsResult> configOptions() {
        return Res.ok(elfCashierConfigService.configOptions());
    }

}
