package cn.daxpay.open.payment.merchant.controller.trade;

import cn.daxpay.open.payment.merchant.service.trade.MchPayCallbackRecordService;
import cn.daxpay.open.payment.trade.record.param.PayCallbackRecordQuery;
import cn.daxpay.open.payment.trade.record.result.PayCallbackRecordResult;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/// # 通道入站回调记录(商户端)
///
/// 业务编排委托 [MchPayCallbackRecordService]；强制当前商户过滤。
@PermCode(menuCode = PermCodes.Trade.CallbackRecord.MENU)
@Validated
@Tag(name = "通道入站回调记录(商户端)")
@RestController
@RequestMapping("/mch/callback-record")
@RequiredArgsConstructor
public class MchPayCallbackRecordController {

    private final MchPayCallbackRecordService mchPayCallbackRecordService;

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "回调记录分页")
    @GetMapping("/page")
    public Result<PageResult<PayCallbackRecordResult>> page(PageParam pageParam, PayCallbackRecordQuery query) {
        return Res.ok(mchPayCallbackRecordService.page(pageParam, query));
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "回调记录详情")
    @GetMapping("/get-by-id")
    public Result<PayCallbackRecordResult> getById(
            @NotNull(message = "{validation.field.id.notNull}") Long id) {
        return Res.ok(mchPayCallbackRecordService.findById(id));
    }
}
