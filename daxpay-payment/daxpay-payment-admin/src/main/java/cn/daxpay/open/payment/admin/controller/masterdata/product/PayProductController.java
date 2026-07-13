package cn.daxpay.open.payment.admin.controller.masterdata.product;


import cn.daxpay.open.platform.core.code.PermCodes;
import cn.daxpay.open.payment.masterdata.constants.product.param.PayProductQuery;
import cn.daxpay.open.payment.masterdata.constants.product.result.PayProductResult;
import cn.daxpay.open.payment.masterdata.constants.product.service.PayProductService;
import cn.daxpay.open.platform.core.annotation.PermCode;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.dto.LabelValue;
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

/// # 支付产品管理
///
@PermCode(menuCode = PermCodes.Payment.Platform.Product.MENU)
@Validated
@Tag(name = "支付产品管理")
@RestController
@RequestMapping("/admin/product")
@RequiredArgsConstructor
public class PayProductController {

    private final PayProductService payProductService;

    @PermCode(code = PermCodes.Action.VIEW, nameCn = "产品查看", nameEn = "Product View")
    @Operation(summary = "分页查询")
    @GetMapping("/page")
    public Result<PageResult<PayProductResult>> page(PageParam pageParam, PayProductQuery query, String name) {
        return Res.ok(payProductService.page(pageParam, query, name));
    }

    @PermCode(code = PermCodes.Action.VIEW, nameCn = "产品查看", nameEn = "Product View")
    @Operation(summary = "根据编码查询详情")
    @GetMapping("/get")
    public Result<PayProductResult> findByCode(@NotBlank(message = "{validation.field.code.notBlank}") String code) {
        return Res.ok(payProductService.findByCode(code));
    }

    @PermCode(code = PermCodes.Action.VIEW, nameCn = "产品查看", nameEn = "Product View")
    @Operation(summary = "启用产品下拉列表")
    @GetMapping("/dropdown")
    public Result<List<LabelValue>> dropdown() {
        return Res.ok(payProductService.dropdown());
    }

    @PermCode(code = PermCodes.Action.MANAGE, nameCn = "产品管理", nameEn = "Product Manage")
    @Operation(summary = "切换支付产品启停")
    @PostMapping("/switch-enabled")
    public Result<Void> switchEnabled(
            @NotBlank(message = "{validation.field.product.notBlank}") String product,
            @NotNull(message = "{validation.field.enabled.notNull}") Boolean enabled) {
        payProductService.switchEnabled(product, enabled);
        return Res.ok();
    }

    /// 全量查询支付产品列表（卡片式管理页使用）
    @PermCode(code = PermCodes.Action.VIEW, nameCn = "产品查看", nameEn = "Product View")
    @Operation(summary = "全量查询支付产品")
    @GetMapping("/list-all")
    public Result<List<PayProductResult>> listAll() {
        return Res.ok(payProductService.listAll());
    }

}
