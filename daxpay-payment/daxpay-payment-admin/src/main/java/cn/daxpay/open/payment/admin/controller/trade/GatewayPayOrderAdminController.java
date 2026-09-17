package cn.daxpay.open.payment.admin.controller.trade;

import cn.daxpay.open.payment.trade.order.param.GatewayPayOrderQuery;
import cn.daxpay.open.payment.trade.order.result.GatewayPayOrderResult;
import cn.daxpay.open.payment.trade.order.result.export.GatewayOrderExportResult;
import cn.daxpay.open.payment.trade.order.convert.export.GatewayOrderExportConvert;
import cn.daxpay.open.payment.admin.service.trade.GatewayPayOrderAdminService;
import cn.daxpay.open.payment.unipay.result.trade.pay.NormalPaySyncResult;
import cn.daxpay.open.platform.common.excel.ExcelUtils;
import cn.daxpay.open.platform.core.annotation.OperateLog;
import cn.daxpay.open.platform.core.annotation.PermCode;
import cn.daxpay.open.platform.core.code.PermCodes;
import cn.daxpay.open.platform.core.enums.common.OperateLogType;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import cn.daxpay.open.platform.core.rest.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/// # 网关支付业务单(管理)
@PermCode(menuCode = PermCodes.Trade.GatewayOrder.MENU)
@Validated
@Tag(name = "网关支付业务单(管理)")
@RestController
@RequestMapping("/admin/order/gateway-pay")
@RequiredArgsConstructor
public class GatewayPayOrderAdminController {

    private final GatewayPayOrderAdminService gatewayPayOrderAdminService;

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "网关支付业务单分页")
    @GetMapping("/page")
    public Result<PageResult<GatewayPayOrderResult>> page(PageParam pageParam, GatewayPayOrderQuery query) {
        return Res.ok(gatewayPayOrderAdminService.page(pageParam, query));
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "根据ID查询详情")
    @GetMapping("/get-by-id")
    public Result<GatewayPayOrderResult> findById(
            @NotNull(message = "{validation.field.id.notNull}") Long id) {
        return Res.ok(gatewayPayOrderAdminService.findById(id));
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "同步支付状态")
    @PostMapping("/sync")
    public Result<NormalPaySyncResult> sync(
            @NotNull(message = "{validation.field.id.notNull}") Long id) {
        return Res.ok(gatewayPayOrderAdminService.sync(id));
    }

    @PermCode(code = PermCodes.Action.EXPORT)
    @Operation(summary = "导出网关支付业务单")
    @OperateLog(title = "导出网关支付业务单", businessType = OperateLogType.EXPORT, saveParam = false)
    @PostMapping("/export")
    public void export(HttpServletResponse response, GatewayPayOrderQuery query) {
        // 导出时间范围必填, 跨度上限 90 天
        ExcelUtils.validateExportTimeRange(query.getCreateTimeStart(), query.getCreateTimeEnd());
        ExcelUtils.exportPaged(GatewayOrderExportResult.class, "export.gateway_order.fileName", response,
                (pageNo, size) -> gatewayPayOrderAdminService.page(new PageParam(pageNo, size), query),
                GatewayOrderExportConvert.CONVERT::toExportResultList);
    }
}
