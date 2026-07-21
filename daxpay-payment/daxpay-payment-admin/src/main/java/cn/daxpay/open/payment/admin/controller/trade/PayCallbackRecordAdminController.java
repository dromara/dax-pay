package cn.daxpay.open.payment.admin.controller.trade;

import cn.daxpay.open.payment.admin.service.trade.PayCallbackRecordAdminService;
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

/// # 通道入站回调记录(管理)
///
@PermCode(menuCode = PermCodes.Trade.CallbackRecord.MENU)
@Validated
@Tag(name = "通道入站回调记录(管理)")
@RestController
@RequestMapping("/admin/callback-record")
@RequiredArgsConstructor
public class PayCallbackRecordAdminController {

    private final PayCallbackRecordAdminService payCallbackRecordAdminService;

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "回调记录分页")
    @GetMapping("/page")
    public Result<PageResult<PayCallbackRecordResult>> page(PageParam pageParam, PayCallbackRecordQuery query) {
        return Res.ok(payCallbackRecordAdminService.page(pageParam, query));
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "回调记录详情")
    @GetMapping("/get-by-id")
    public Result<PayCallbackRecordResult> getById(
            @NotNull(message = "{validation.field.id.notNull}") Long id) {
        return Res.ok(payCallbackRecordAdminService.findById(id));
    }
}
