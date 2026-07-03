package cn.daxpay.open.payment.web.admin.controller.order;

import cn.daxpay.open.platform.core.annotation.PermCode;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import cn.daxpay.open.platform.core.rest.result.Result;
import cn.daxpay.open.payment.core.trade.param.NormalPayOrderQuery;
import cn.daxpay.open.payment.core.trade.result.NormalPayOrderResult;
import cn.daxpay.open.payment.core.trade.service.admin.NormalPayOrderAdminService;
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

/// # 普通支付业务单(管理)
///
/// 面向运营/商户后台的业务订单(容器)管理: 分页查询、详情、状态同步、关闭/撤销
@PermCode(menuCode = "payment:order")
@Validated
@Tag(name = "普通支付业务单(管理)")
@RestController
@RequestMapping("/admin/order/normal-pay")
@RequiredArgsConstructor
public class NormalPayOrderAdminController {

    private final NormalPayOrderAdminService normalPayOrderAdminService;

    @PermCode(code = "view", nameCn = "订单查看", nameEn = "Order View")
    @Operation(summary = "普通支付业务单分页")
    @GetMapping("/page")
    public Result<PageResult<NormalPayOrderResult>> page(PageParam pageParam, NormalPayOrderQuery query) {
        return Res.ok(normalPayOrderAdminService.page(pageParam, query));
    }

    @PermCode(code = "view", nameCn = "订单查看", nameEn = "Order View")
    @Operation(summary = "根据ID查询普通支付业务单详情")
    @GetMapping("/get-by-id")
    public Result<NormalPayOrderResult> findById(
            @NotNull(message = "{validation.field.id.notNull}") Long id) {
        return Res.ok(normalPayOrderAdminService.findById(id));
    }

    @PermCode(code = "manage", nameCn = "订单管理", nameEn = "Order Manage")
    @Operation(summary = "同步支付状态")
    @PostMapping("/sync")
    public Result<NormalPaySyncResult> sync(
            @NotNull(message = "{validation.field.id.notNull}") Long id) {
        return Res.ok(normalPayOrderAdminService.sync(id));
    }

    @PermCode(code = "manage", nameCn = "订单管理", nameEn = "Order Manage")
    @Operation(summary = "关闭/撤销订单")
    @PostMapping("/close")
    public Result<Void> close(
            @NotNull(message = "{validation.field.id.notNull}") Long id,
            @RequestParam(defaultValue = "false") boolean useCancel) {
        normalPayOrderAdminService.close(id, useCancel);
        return Res.ok();
    }
}
