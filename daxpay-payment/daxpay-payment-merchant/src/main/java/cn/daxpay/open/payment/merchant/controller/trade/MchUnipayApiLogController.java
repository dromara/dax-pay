package cn.daxpay.open.payment.merchant.controller.trade;

import cn.daxpay.open.platform.capability.audit.log.param.UnipayApiLogQuery;
import cn.daxpay.open.platform.capability.audit.log.result.UnipayApiLogResult;
import cn.daxpay.open.payment.merchant.service.trade.MchUnipayApiLogService;
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

/// # 统一支付接口调用日志(商户端)
///
/// 商户排查 unipay 对接问题的只读查询入口；强制按当前商户过滤，不开放清理。
@PermCode(menuCode = PermCodes.Trade.UnipayLog.MENU)
@Validated
@Tag(name = "支付接口日志(商户端)")
@RestController
@RequestMapping("/mch/log/unipay")
@RequiredArgsConstructor
public class MchUnipayApiLogController {

    private final MchUnipayApiLogService mchUnipayApiLogService;

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "调用日志分页")
    @GetMapping("/page")
    public Result<PageResult<UnipayApiLogResult>> page(PageParam pageParam, UnipayApiLogQuery query) {
        return Res.ok(mchUnipayApiLogService.page(pageParam, query));
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "调用日志详情")
    @GetMapping("/get")
    public Result<UnipayApiLogResult> findById(
            @NotNull(message = "{validation.field.id.notNull}") Long id) {
        return Res.ok(mchUnipayApiLogService.findById(id));
    }
}
