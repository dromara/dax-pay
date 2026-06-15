package org.dromara.daxpay.payment.admin.controller.masterdata.product;

import org.dromara.daxpay.payment.pay.param.masterdata.product.PayProductQuery;
import org.dromara.daxpay.payment.pay.result.masterdata.product.PayProductResult;
import org.dromara.daxpay.payment.pay.service.masterdata.product.PayProductService;
import org.dromara.daxpay.platform.core.annotation.PermCode;
import org.dromara.daxpay.platform.core.rest.Res;
import org.dromara.daxpay.platform.core.rest.dto.LabelValue;
import org.dromara.daxpay.platform.core.rest.param.PageParam;
import org.dromara.daxpay.platform.core.rest.result.PageResult;
import org.dromara.daxpay.platform.core.rest.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/// # 支付产品管理
///
@PermCode(menuCode = "payment:product")
@Validated
@Tag(name = "支付产品管理")
@RestController
@RequestMapping("/admin/product")
@RequiredArgsConstructor
public class PayProductController {

    private final PayProductService payProductService;

    @PermCode(code = "view", nameCn = "产品查看", nameEn = "Product View")
    @Operation(summary = "分页查询")
    @GetMapping("/page")
    public Result<PageResult<PayProductResult>> page(PageParam pageParam, PayProductQuery query, String name) {
        return Res.ok(payProductService.page(pageParam, query, name));
    }

    @PermCode(code = "view", nameCn = "产品查看", nameEn = "Product View")
    @Operation(summary = "根据编码查询详情")
    @GetMapping("/get")
    public Result<PayProductResult> findByCode(@NotBlank(message = "{validation.field.code.notBlank}") String code) {
        return Res.ok(payProductService.findByCode(code));
    }

    @PermCode(code = "view", nameCn = "产品查看", nameEn = "Product View")
    @Operation(summary = "启用产品下拉列表")
    @GetMapping("/dropdown")
    public Result<List<LabelValue>> dropdown() {
        return Res.ok(payProductService.dropdown());
    }

    /// 全量查询支付产品列表（卡片式管理页使用）
    @PermCode(code = "view", nameCn = "产品查看", nameEn = "Product View")
    @Operation(summary = "全量查询支付产品")
    @GetMapping("/list-all")
    public Result<List<PayProductResult>> listAll() {
        return Res.ok(payProductService.listAll());
    }

}
