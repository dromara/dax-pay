package cn.daxpay.open.payment.app.merchant.controller.log;

import cn.daxpay.open.payment.merchant.service.system.MchOperateLogService;
import cn.daxpay.open.platform.capability.audit.log.param.OperateLogQuery;
import cn.daxpay.open.platform.capability.audit.log.result.OperateLogResult;
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

/// # 操作日志(商户移动端)
///
/// 对应 Web 商户端 [cn.daxpay.open.payment.merchant.controller.system.MchOperateLogController]
/// (路径 `/mch/log/operate`), 镜像其全部端点(该控制器无导出端点), 复用同一 [MchOperateLogService]
/// 与菜单/权限码, 仅 URL 前缀不同(`/app-mch`)。
/// 商户查看本商户用户操作记录的只读查询入口; 强制白名单过滤, 不开放清理。
@PermCode(menuCode = PermCodes.Merchant.Log.Operate.MENU)
@Validated
@Tag(name = "小程序商户端-操作日志")
@RestController
@RequestMapping("/app-mch/log/operate")
@RequiredArgsConstructor
public class AppMerchantOperateLogController {

    private final MchOperateLogService mchOperateLogService;

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "操作日志分页")
    @GetMapping("/page")
    public Result<PageResult<OperateLogResult>> page(PageParam pageParam, OperateLogQuery query) {
        return Res.ok(mchOperateLogService.page(pageParam, query));
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "操作日志详情")
    @GetMapping("/get")
    public Result<OperateLogResult> findById(
            @NotNull(message = "{validation.field.id.notNull}") Long id) {
        return Res.ok(mchOperateLogService.findById(id));
    }
}
