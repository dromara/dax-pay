package cn.daxpay.open.payment.app.admin.controller.trade;

import cn.daxpay.open.payment.admin.service.trade.AllocAdminService;
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

/// # 分账订单(小程序管理端)
///
/// 小程序管理端分账订单镜像, 对应 admin 版 [AllocAdminController]。跨商户查询(传 mchNo 过滤),
/// 不做发起分账, 业务编排委托 [AllocAdminService]。
@PermCode(menuCode = PermCodes.Trade.Alloc.MENU)
@Validated
@Tag(name = "分账订单(小程序管理端)")
@RestController
@RequestMapping("/app-admin/alloc")
@RequiredArgsConstructor
public class AppAdminAllocController {

    private final AllocAdminService allocAdminService;

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "小程序管理端-分账订单分页")
    @GetMapping("/page")
    public Result<PageResult<AllocOrderResult>> page(PageParam pageParam, AllocOrderQuery query) {
        return Res.ok(allocAdminService.page(pageParam, query));
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "小程序管理端-分账订单详情")
    @GetMapping("/get-by-id")
    public Result<AllocOrderResult> findById(
            @NotNull(message = "{validation.field.id.notNull}") Long id) {
        return Res.ok(allocAdminService.findById(id));
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "小程序管理端-分账明细列表")
    @GetMapping("/detail")
    public Result<java.util.List<AllocDetailResult>> details(
            @NotBlank(message = "{validation.field.allocNo.notBlank}") String allocNo) {
        return Res.ok(allocAdminService.details(allocNo));
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "小程序管理端-同步分账状态")
    @PostMapping("/sync")
    public Result<Void> sync(
            @NotBlank(message = "{validation.field.allocNo.notBlank}") String allocNo) {
        allocAdminService.sync(allocNo);
        return Res.ok();
    }
}