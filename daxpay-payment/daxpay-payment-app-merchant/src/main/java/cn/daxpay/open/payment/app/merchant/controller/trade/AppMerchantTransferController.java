package cn.daxpay.open.payment.app.merchant.controller.trade;

import cn.daxpay.open.payment.app.merchant.service.trade.AppMerchantTransferService;
import cn.daxpay.open.payment.trade.transfer.param.TransferTradeQuery;
import cn.daxpay.open.payment.trade.transfer.result.TransferTradeResult;
import cn.daxpay.open.platform.core.annotation.PermCode;
import cn.daxpay.open.platform.core.code.PermCodes;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import cn.daxpay.open.platform.core.rest.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/// # 转账单(商户移动端)
///
/// 商户移动端转账单镜像, 对应商户版 [cn.daxpay.open.payment.merchant.controller.trade.MchTransferController]。
/// 仅镜像转账记录查询与同步/关闭操作, 不做发起转账。业务编排委托 [AppMerchantTransferService]。
@PermCode(menuCode = PermCodes.Trade.Transfer.MENU)
@Validated
@Tag(name = "转账单(商户移动端)")
@RestController
@RequestMapping("/app-mch/transfer")
@RequiredArgsConstructor
public class AppMerchantTransferController {

    private final AppMerchantTransferService transferService;

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "转账记录分页(跨通道)")
    @GetMapping("/trade/page")
    public Result<PageResult<TransferTradeResult>> tradePage(PageParam pageParam, TransferTradeQuery query) {
        return Res.ok(transferService.tradePage(pageParam, query));
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "转账记录详情")
    @GetMapping("/trade/get-by-id")
    public Result<TransferTradeResult> tradeFindById(
            @NotNull(message = "{validation.field.id.notNull}") Long id) {
        return Res.ok(transferService.tradeFindById(id));
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "同步转账状态")
    @PostMapping("/sync")
    public Result<Void> sync(
            @NotNull(message = "{validation.field.channel.notNull}") String channel,
            @NotNull(message = "{validation.field.id.notNull}") Long id) {
        transferService.sync(channel, id);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "关闭转账(仅通道支持场景有效)")
    @PostMapping("/close")
    public Result<Void> close(
            @NotNull(message = "{validation.field.channel.notNull}") String channel,
            @NotNull(message = "{validation.field.id.notNull}") Long id) {
        transferService.close(channel, id);
        return Res.ok();
    }
}
