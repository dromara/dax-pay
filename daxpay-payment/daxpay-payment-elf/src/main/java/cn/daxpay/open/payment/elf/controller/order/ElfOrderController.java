package cn.daxpay.open.payment.elf.controller.order;

import cn.daxpay.open.payment.merchant.service.trade.MchOrderCloseService;
import cn.daxpay.open.payment.merchant.service.trade.MchRefundOrderService;
import cn.daxpay.open.payment.trade.order.param.OrderCloseParam;
import cn.daxpay.open.payment.trade.order.result.RefundOrderResult;
import cn.daxpay.open.payment.trade.runtime.param.RefundParam;
import cn.daxpay.open.platform.core.annotation.PermCode;
import cn.daxpay.open.platform.core.code.PermCodes;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/// # 精灵订单操作(PC收款精灵专用端)
///
/// 精灵的关闭订单/发起退款原复用商户 Web 域 `/mch/order/**`, 而该前缀在统一网关恒封禁,
/// 导致面板部署环境下精灵退款/关单 404; 现独立挂到 `/elf/**`, 使 `/mch/**` 恒封禁策略得以保留。
/// 业务实现直接复用商户模块 [MchOrderCloseService] 与 [MchRefundOrderService](不复制业务逻辑),
/// 权限复用交易订单/退款的既有管理码(收银员内置角色 [cn.daxpay.open.platform.core.enums.role.RoleCodeEnum#MERCHANT_CASHIER] 已绑定)。
/// 身份域守卫见 [cn.daxpay.open.payment.elf.configuration.ElfWebConfig](`/elf/**` 要求 `x-client-code: merchant`)。
@Validated
@Tag(name = "精灵订单操作(PC收款精灵)")
@RestController
@RequestMapping("/elf/order")
@RequiredArgsConstructor
public class ElfOrderController {

    private final MchOrderCloseService mchOrderCloseService;

    private final MchRefundOrderService mchRefundOrderService;

    @PermCode(menuCode = PermCodes.Trade.Order.MENU, code = PermCodes.Action.MANAGE)
    @Operation(summary = "关闭订单(网关/普通统一入口)")
    @PostMapping("/close")
    public Result<Void> close(@RequestBody @Valid OrderCloseParam param) {
        mchOrderCloseService.close(param.getContainerId(), param.getTradeType());
        return Res.ok();
    }

    @PermCode(menuCode = PermCodes.Trade.Refund.MENU, code = PermCodes.Action.MANAGE)
    @Operation(summary = "发起退款")
    @PostMapping("/refund")
    public Result<RefundOrderResult> refund(@Valid @RequestBody RefundParam param) {
        return Res.ok(mchRefundOrderService.refund(param));
    }
}
