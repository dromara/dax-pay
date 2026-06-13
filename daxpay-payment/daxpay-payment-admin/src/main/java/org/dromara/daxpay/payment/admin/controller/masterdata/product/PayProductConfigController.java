package org.dromara.daxpay.payment.admin.controller.masterdata.product;

import org.dromara.daxpay.payment.pay.param.masterdata.product.PayProductConfigParam;
import org.dromara.daxpay.payment.pay.result.masterdata.product.PayProductConfigResult;
import org.dromara.daxpay.payment.pay.service.masterdata.product.PayProductConfigService;
import org.dromara.daxpay.platform.core.annotation.PermCode;
import org.dromara.daxpay.platform.core.rest.Res;
import org.dromara.daxpay.platform.core.rest.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/// # 支付产品配置管理
///
@PermCode(menuCode = "payment:product:config")
@Validated
@Tag(name = "支付产品配置管理")
@RestController
@RequestMapping("/admin/product-config")
@RequiredArgsConstructor
public class PayProductConfigController {

    private final PayProductConfigService payProductConfigService;

    @PermCode(code = "view", nameCn = "产品配置查看", nameEn = "Product Config View")
    @Operation(summary = "全量查询产品配置列表（卡片页使用）")
    @GetMapping("/list-all")
    public Result<List<PayProductConfigResult>> listAll() {
        return Res.ok(payProductConfigService.listAll());
    }

    @PermCode(code = "edit", nameCn = "产品配置编辑", nameEn = "Product Config Edit")
    @Operation(summary = "切换产品生效环境")
    @PostMapping("/switch-env")
    public Result<Void> switchEnv(
            @NotBlank(message = "{validation.field.product.notBlank}") String product,
            @NotNull(message = "{validation.field.sandbox.notNull}") Boolean sandbox) {
        payProductConfigService.switchEnv(product, sandbox);
        return Res.ok();
    }

    @PermCode(code = "edit", nameCn = "产品配置编辑", nameEn = "Product Config Edit")
    @Operation(summary = "保存产品配置")
    @PostMapping("/save")
    public Result<Void> save(@RequestBody @Validated PayProductConfigParam param) {
        payProductConfigService.saveOrUpdate(param);
        return Res.ok();
    }
}
