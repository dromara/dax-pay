package cn.daxpay.open.payment.app.merchant.controller.trade;

import cn.daxpay.open.payment.app.merchant.service.trade.AppMerchantAllocService;
import cn.daxpay.open.payment.trade.alloc.param.AllocOrderQuery;
import cn.daxpay.open.payment.trade.alloc.result.AllocDetailResult;
import cn.daxpay.open.payment.trade.alloc.result.AllocOrderResult;
import cn.daxpay.open.platform.core.annotation.PermCode;
import cn.daxpay.open.platform.core.code.PermCodes;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import cn.daxpay.open.platform.core.rest.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/// # 分账订单(商户移动端)
///
/// 商户移动端分账订单镜像, 对应商户版 [cn.daxpay.open.payment.merchant.controller.trade.MchAllocController]。
/// 仅查询与同步, 不做发起分账。业务编排委托 [AppMerchantAllocService]。
@PermCode(menuCode = PermCodes.Trade.Alloc.MENU)
@Validated
@Tag(name = "分账订单(商户移动端)")
@RestController
@RequestMapping("/app-mch/alloc")
@RequiredArgsConstructor
public class AppMerchantAllocController {

    private final AppMerchantAllocService allocService;

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "分账订单分页")
    @GetMapping("/page")
    public Result<PageResult<AllocOrderResult>> page(PageParam pageParam, AllocOrderQuery query) {
        return Res.ok(allocService.page(pageParam, query));
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "根据ID查询分账订单详情")
    @GetMapping("/get-by-id")
    public Result<AllocOrderResult> findById(
            @NotNull(message = "{validation.field.id.notNull}") Long id) {
        return Res.ok(allocService.findById(id));
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "分账明细列表")
    @GetMapping("/detail")
    public Result<List<AllocDetailResult>> details(
            @NotBlank(message = "{validation.field.allocNo.notBlank}") String allocNo) {
        return Res.ok(allocService.details(allocNo));
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "同步分账状态")
    @PostMapping("/sync")
    public Result<Void> sync(
            @NotBlank(message = "{validation.field.allocNo.notBlank}") String allocNo) {
        allocService.sync(allocNo);
        return Res.ok();
    }
}
