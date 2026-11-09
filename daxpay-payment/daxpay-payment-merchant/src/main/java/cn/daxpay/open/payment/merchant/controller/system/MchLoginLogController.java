package cn.daxpay.open.payment.merchant.controller.system;

import cn.daxpay.open.platform.capability.audit.log.param.LoginLogQuery;
import cn.daxpay.open.platform.capability.audit.log.result.LoginLogResult;
import cn.daxpay.open.platform.core.annotation.PermCode;
import cn.daxpay.open.platform.core.code.PermCodes;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import cn.daxpay.open.platform.core.rest.result.Result;
import cn.daxpay.open.payment.merchant.service.system.MchLoginLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/// # 登录日志(商户端)
///
/// 商户查看本商户用户登录活动的只读查询入口; 强制白名单过滤, 不开放清理。
@PermCode(menuCode = PermCodes.Merchant.Log.Login.MENU)
@Validated
@Tag(name = "登录日志(商户端)")
@RestController
@RequestMapping("/mch/log/login")
@RequiredArgsConstructor
public class MchLoginLogController {

    private final MchLoginLogService mchLoginLogService;

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "登录日志分页")
    @GetMapping("/page")
    public Result<PageResult<LoginLogResult>> page(PageParam pageParam, LoginLogQuery query) {
        return Res.ok(mchLoginLogService.page(pageParam, query));
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "登录日志详情")
    @GetMapping("/get")
    public Result<LoginLogResult> findById(
            @NotNull(message = "{validation.field.id.notNull}") Long id) {
        return Res.ok(mchLoginLogService.findById(id));
    }
}
