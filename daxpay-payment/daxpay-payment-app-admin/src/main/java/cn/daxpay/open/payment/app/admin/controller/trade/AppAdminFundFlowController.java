package cn.daxpay.open.payment.app.admin.controller.trade;

import cn.daxpay.open.payment.admin.service.trade.FundFlowAdminService;
import cn.daxpay.open.payment.app.admin.service.trade.AppAdminFundFlowService;
import cn.daxpay.open.payment.trade.flow.param.FundFlowQuery;
import cn.daxpay.open.payment.trade.flow.result.FundFlowResult;
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

/// # 资金流水(运营移动端)
///
/// 收款/退款成功流水查询, 只读。业务编排委托 [AppAdminFundFlowService]（转发 [FundFlowAdminService]）。
@PermCode(menuCode = PermCodes.Trade.FundFlow.MENU)
@Validated
@Tag(name = "资金流水(运营移动端)")
@RestController
@RequestMapping("/app-admin/order/fund-flow")
@RequiredArgsConstructor
public class AppAdminFundFlowController {

    private final AppAdminFundFlowService fundFlowService;

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "资金流水分页")
    @GetMapping("/page")
    public Result<PageResult<FundFlowResult>> page(PageParam pageParam, FundFlowQuery query) {
        return Res.ok(fundFlowService.page(pageParam, query));
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "根据ID查询资金流水详情")
    @GetMapping("/get-by-id")
    public Result<FundFlowResult> findById(
            @NotNull(message = "{validation.field.id.notNull}") Long id) {
        return Res.ok(fundFlowService.findById(id));
    }
}
