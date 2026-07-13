package cn.daxpay.open.payment.admin.controller.trade;

import cn.daxpay.open.platform.core.annotation.PermCode;
import cn.daxpay.open.platform.core.code.PermCodes;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import cn.daxpay.open.platform.core.rest.result.Result;
import cn.daxpay.open.payment.core.trade.order.param.PayTradeQuery;
import cn.daxpay.open.payment.core.trade.order.result.PayTradeResult;
import cn.daxpay.open.payment.admin.service.trade.PayTradeAdminService;
import cn.daxpay.open.payment.unipay.result.trade.pay.NormalPaySyncResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/// # 资金交易凭证(管理)
///
/// 面向运营/商户后台的资金交易(凭证)管理: 分页查询、详情、状态同步、关闭/撤销
@PermCode(menuCode = PermCodes.Payment.Trade.MENU)
@Validated
@Tag(name = "资金交易凭证(管理)")
@RestController
@RequestMapping("/admin/order/pay-trade")
@RequiredArgsConstructor
public class PayTradeAdminController {

    private final PayTradeAdminService payTradeAdminService;

    @PermCode(code = PermCodes.Action.VIEW, nameCn = "交易查看", nameEn = "Trade View")
    @Operation(summary = "资金交易凭证分页")
    @GetMapping("/page")
    public Result<PageResult<PayTradeResult>> page(PageParam pageParam, PayTradeQuery query) {
        return Res.ok(payTradeAdminService.page(pageParam, query));
    }

    @PermCode(code = PermCodes.Action.VIEW, nameCn = "交易查看", nameEn = "Trade View")
    @Operation(summary = "根据ID查询资金交易凭证详情")
    @GetMapping("/get-by-id")
    public Result<PayTradeResult> findById(
            @NotNull(message = "{validation.field.id.notNull}") Long id) {
        return Res.ok(payTradeAdminService.findById(id));
    }

    @PermCode(code = PermCodes.Action.MANAGE, nameCn = "交易管理", nameEn = "Trade Manage")
    @Operation(summary = "同步支付状态")
    @PostMapping("/sync")
    public Result<NormalPaySyncResult> sync(
            @NotNull(message = "{validation.field.id.notNull}") Long id) {
        return Res.ok(payTradeAdminService.sync(id));
    }

    @PermCode(code = PermCodes.Action.MANAGE, nameCn = "交易管理", nameEn = "Trade Manage")
    @Operation(summary = "关闭/撤销订单")
    @PostMapping("/close")
    public Result<Void> close(
            @NotNull(message = "{validation.field.id.notNull}") Long id,
            @RequestParam(defaultValue = "false") boolean useCancel) {
        payTradeAdminService.close(id, useCancel);
        return Res.ok();
    }
}
