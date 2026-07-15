package cn.daxpay.open.plugin.risk.controller.admin;

import cn.daxpay.open.platform.core.annotation.PermCode;
import cn.daxpay.open.platform.core.code.PermCodes;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import cn.daxpay.open.platform.core.rest.result.Result;
import cn.daxpay.open.platform.core.validation.ValidationGroup;
import cn.daxpay.open.plugin.risk.param.PayBlacklistParam;
import cn.daxpay.open.plugin.risk.param.PayBlacklistQuery;
import cn.daxpay.open.plugin.risk.result.PayBlacklistResult;
import cn.daxpay.open.plugin.risk.service.PayBlacklistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/// # 支付黑名单管理（运营端）
///
@PermCode(menuCode = PermCodes.Payment.Risk.Blacklist.MENU)
@Validated
@Tag(name = "支付黑名单")
@RestController
@RequestMapping("/admin/pay/blacklist")
@RequiredArgsConstructor
public class PayBlacklistAdminController {

    private final PayBlacklistService payBlacklistService;

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "分页查询")
    @GetMapping("/page")
    public Result<PageResult<PayBlacklistResult>> page(PageParam pageParam, PayBlacklistQuery query) {
        return Res.ok(payBlacklistService.page(pageParam, query));
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "详情")
    @GetMapping("/get")
    public Result<PayBlacklistResult> get(@NotNull(message = "{validation.field.id.notNull}") Long id) {
        return Res.ok(payBlacklistService.findById(id));
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "新增")
    @PostMapping("/add")
    public Result<PayBlacklistResult> add(@RequestBody @Validated(ValidationGroup.add.class) PayBlacklistParam param) {
        return Res.ok(payBlacklistService.add(param));
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "修改")
    @PostMapping("/update")
    public Result<Void> update(@RequestBody @Validated(ValidationGroup.edit.class) PayBlacklistParam param) {
        payBlacklistService.update(param);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "删除")
    @PostMapping("/delete")
    public Result<Void> delete(@NotNull(message = "{validation.field.id.notNull}") Long id) {
        payBlacklistService.delete(id);
        return Res.ok();
    }
}
