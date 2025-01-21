package org.dromara.daxpay.service.controller.reconcile;

import cn.bootx.platform.core.annotation.RequestGroup;
import cn.bootx.platform.core.annotation.RequestPath;
import cn.bootx.platform.core.rest.Res;
import cn.bootx.platform.core.rest.result.Result;
import org.dromara.daxpay.service.param.reconcile.ReconcileCreatParam;
import org.dromara.daxpay.service.service.reconcile.ReconcileStatementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 对账服务控制器(管理端)
 * @author xxm
 * @since 2024/8/7
 */
@Validated
@RequestGroup(moduleCode = "reconcile",moduleName = "对账服务", groupCode = "ReconcileStatement", groupName = "对账单")
@Tag(name = "对账服务控制器")
@RestController
@RequestMapping("/reconcile/statement")
@RequiredArgsConstructor
public class ReconcileStatementAdminController {
    private final ReconcileStatementService statementService;

    @RequestPath("手动创建对账订单")
    @Operation(summary = "手动创建对账订单")
    @PostMapping("/create")
    public Result<Void> create(@RequestBody @Validated ReconcileCreatParam param){
        statementService.create(param);
        return Res.ok();
    }
}
