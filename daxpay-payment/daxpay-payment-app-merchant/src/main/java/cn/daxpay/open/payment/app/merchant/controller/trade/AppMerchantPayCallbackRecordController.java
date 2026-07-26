package cn.daxpay.open.payment.app.merchant.controller.trade;

import cn.daxpay.open.payment.app.merchant.service.trade.AppMerchantPayCallbackRecordService;
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

/// # 通道入站回调记录(商户移动端)
///
/// 面向商户移动端的通道回调记录查询。业务编排委托 [AppMerchantPayCallbackRecordService]。
@PermCode(menuCode = PermCodes.Trade.CallbackRecord.MENU)
@Validated
@Tag(name = "通道入站回调记录(商户移动端)")
@RestController
@RequestMapping("/app-merchant/callback-record")
@RequiredArgsConstructor
public class AppMerchantPayCallbackRecordController {

    private final AppMerchantPayCallbackRecordService payCallbackRecordService;

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "回调记录分页")
    @GetMapping("/page")
    public Result<PageResult<PayCallbackRecordResult>> page(PageParam pageParam, PayCallbackRecordQuery query) {
        return Res.ok(payCallbackRecordService.page(pageParam, query));
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "回调记录详情")
    @GetMapping("/get-by-id")
    public Result<PayCallbackRecordResult> findById(
            @NotNull(message = "{validation.field.id.notNull}") Long id) {
        return Res.ok(payCallbackRecordService.findById(id));
    }
}
