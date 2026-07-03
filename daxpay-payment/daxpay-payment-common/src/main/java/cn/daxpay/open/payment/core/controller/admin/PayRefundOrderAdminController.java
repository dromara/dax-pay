package cn.daxpay.open.payment.core.controller.admin;

import cn.daxpay.open.platform.core.annotation.PermCode;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import cn.daxpay.open.platform.core.rest.result.Result;
import cn.daxpay.open.payment.core.trade.param.PayRefundOrderQuery;
import cn.daxpay.open.payment.core.trade.param.PayRefundParam;
import cn.daxpay.open.payment.core.trade.result.PayRefundOrderResult;
import cn.daxpay.open.payment.core.trade.service.admin.PayRefundOrderAdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/// # 退款订单(管理)
///
/// 面向运营/商户后台的退款订单管理: 分页查询、详情、发起退款、退款状态同步
@PermCode(menuCode = "payment:refund")
@Validated
@Tag(name = "退款订单(管理)")
@RestController
@RequestMapping("/admin/order/refund")
@RequiredArgsConstructor
public class PayRefundOrderAdminController {

    private final PayRefundOrderAdminService payRefundOrderAdminService;

    @PermCode(code = "view", nameCn = "退款查看", nameEn = "Refund View")
    @Operation(summary = "退款订单分页")
    @GetMapping("/page")
    public Result<PageResult<PayRefundOrderResult>> page(PageParam pageParam, PayRefundOrderQuery query) {
        return Res.ok(payRefundOrderAdminService.page(pageParam, query));
    }

    @PermCode(code = "view", nameCn = "退款查看", nameEn = "Refund View")
    @Operation(summary = "根据ID查询退款订单详情")
    @GetMapping("/get-by-id")
    public Result<PayRefundOrderResult> findById(
            @NotNull(message = "{validation.field.id.notNull}") Long id) {
        return Res.ok(payRefundOrderAdminService.findById(id));
    }

    @PermCode(code = "manage", nameCn = "退款管理", nameEn = "Refund Manage")
    @Operation(summary = "发起退款")
    @PostMapping("/refund")
    public Result<PayRefundOrderResult> refund(@Valid @RequestBody PayRefundParam param) {
        return Res.ok(payRefundOrderAdminService.refund(param));
    }

    @PermCode(code = "manage", nameCn = "退款管理", nameEn = "Refund Manage")
    @Operation(summary = "同步退款状态")
    @PostMapping("/sync")
    public Result<PayRefundOrderResult> sync(
            @NotNull(message = "{validation.field.id.notNull}") Long id) {
        return Res.ok(payRefundOrderAdminService.sync(id));
    }
}
