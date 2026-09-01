package cn.daxpay.open.plugin.risk.controller.appadmin;

import cn.daxpay.open.platform.core.annotation.PermCode;
import cn.daxpay.open.platform.core.code.PermCodes;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import cn.daxpay.open.platform.core.rest.result.Result;
import cn.daxpay.open.plugin.risk.param.PayRiskHitQuery;
import cn.daxpay.open.plugin.risk.result.PayRiskHitResult;
import cn.daxpay.open.plugin.risk.service.PayRiskHitService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/// # 支付风险命中记录(运营移动端)
///
/// 小程序管理端的镜像接口, 与 [PayRiskHitAdminController] 同权限码同 Service, 仅路径前缀不同。
@PermCode(menuCode = PermCodes.Payment.Risk.Hit.MENU)
@Validated
@Tag(name = "支付风险命中(运营移动端)")
@RestController
@RequestMapping("/app-admin/pay/risk-hit")
@RequiredArgsConstructor
public class AppAdminPayRiskHitController {

    private final PayRiskHitService payRiskHitService;

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "分页查询")
    @GetMapping("/page")
    public Result<PageResult<PayRiskHitResult>> page(PageParam pageParam, PayRiskHitQuery query) {
        return Res.ok(payRiskHitService.page(pageParam, query));
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "详情")
    @GetMapping("/get")
    public Result<PayRiskHitResult> get(@NotNull(message = "{validation.field.id.notNull}") Long id) {
        return Res.ok(payRiskHitService.findById(id));
    }
}
