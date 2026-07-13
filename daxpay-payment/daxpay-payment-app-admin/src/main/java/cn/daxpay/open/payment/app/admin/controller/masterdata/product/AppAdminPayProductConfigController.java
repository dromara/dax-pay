package cn.daxpay.open.payment.app.admin.controller.masterdata.product;

import cn.daxpay.open.payment.app.admin.service.masterdata.product.AppAdminPayProductConfigService;
import cn.daxpay.open.payment.masterdata.constants.product.param.PayProductConfigParam;
import cn.daxpay.open.payment.masterdata.constants.product.result.PayProductConfigResult;
import cn.daxpay.open.platform.core.annotation.PermCode;
import cn.daxpay.open.platform.core.code.PermCodes;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/// 运营移动端-支付产品配置管理
@PermCode(menuCode = PermCodes.Payment.ProductConfig.MENU)
@Validated
@Tag(name = "运营移动端-支付产品配置管理")
@RestController
@RequestMapping("/app-admin/product-config")
@RequiredArgsConstructor
public class AppAdminPayProductConfigController {

    private final AppAdminPayProductConfigService payProductConfigService;

    @PermCode(code = PermCodes.Action.VIEW, nameCn = "产品配置查看", nameEn = "Product Config View")
    @Operation(summary = "全量查询产品配置列表")
    @GetMapping("/list-all")
    public Result<List<PayProductConfigResult>> listAll() {
        return Res.ok(payProductConfigService.listAll());
    }

    @PermCode(code = PermCodes.Action.MANAGE, nameCn = "产品配置管理", nameEn = "Product Config Manage")
    @Operation(summary = "切换产品生效环境")
    @PostMapping("/switch-env")
    public Result<Void> switchEnv(
            @NotBlank(message = "{validation.field.product.notBlank}") String product,
            @NotNull(message = "{validation.field.sandbox.notNull}") Boolean sandbox) {
        payProductConfigService.switchEnv(product, sandbox);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.MANAGE, nameCn = "产品配置管理", nameEn = "Product Config Manage")
    @Operation(summary = "保存产品配置")
    @PostMapping("/save")
    public Result<Void> save(@RequestBody @Validated PayProductConfigParam param) {
        payProductConfigService.saveOrUpdate(param);
        return Res.ok();
    }
}
