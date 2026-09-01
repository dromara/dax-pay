package cn.daxpay.open.payment.app.admin.controller.trade;

import cn.daxpay.open.payment.admin.service.trade.AbnormalOrderAdminService;
import cn.daxpay.open.payment.app.admin.service.trade.AppAdminAbnormalOrderService;
import cn.daxpay.open.payment.trade.abnormal.param.AbnormalOrderQuery;
import cn.daxpay.open.payment.trade.abnormal.result.AbnormalOrderResult;
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

/// # 异常订单(运营移动端)
///
/// 终态订单收到通道收款证据的人工处置入口。业务编排委托 [AppAdminAbnormalOrderService]（转发 [AbnormalOrderAdminService]）。
@PermCode(menuCode = PermCodes.Trade.AbnormalOrder.MENU)
@Validated
@Tag(name = "异常订单(运营移动端)")
@RestController
@RequestMapping("/app-admin/order/abnormal-order")
@RequiredArgsConstructor
public class AppAdminAbnormalOrderController {

    private final AppAdminAbnormalOrderService abnormalOrderService;

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "异常订单分页")
    @GetMapping("/page")
    public Result<PageResult<AbnormalOrderResult>> page(PageParam pageParam, AbnormalOrderQuery query) {
        return Res.ok(abnormalOrderService.page(pageParam, query));
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "根据ID查询异常订单详情")
    @GetMapping("/get-by-id")
    public Result<AbnormalOrderResult> findById(
            @NotNull(message = "{validation.field.id.notNull}") Long id) {
        return Res.ok(abnormalOrderService.findById(id));
    }

    /// 确认成功前建议先在订单列表触发同步, 核实通道侧真实状态
    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "确认成功(订单翻转为支付成功并补发通知)")
    @PostMapping("/confirm-success")
    public Result<Void> confirmSuccess(
            @NotNull(message = "{validation.field.id.notNull}") Long id, String remark) {
        abnormalOrderService.confirmSuccess(id, remark);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "忽略(核实无需入账)")
    @PostMapping("/ignore")
    public Result<Void> ignore(
            @NotNull(message = "{validation.field.id.notNull}") Long id, String remark) {
        abnormalOrderService.ignore(id, remark);
        return Res.ok();
    }
}
