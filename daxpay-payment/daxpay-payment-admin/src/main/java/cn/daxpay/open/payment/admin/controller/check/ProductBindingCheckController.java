package cn.daxpay.open.payment.admin.controller.check;

import cn.daxpay.open.payment.admin.check.service.ProductBindingCheckService;
import cn.daxpay.open.payment.common.check.model.ProductBindingCheckResult;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/// # 产品绑定检查(运营端)
///
/// 服务商产品配置页的数据源, 展示该产品各项关键配置的绑定状态。
/// 不挂菜单权限码: 与配置检查(运营端)一致, 任何已认证运营用户均可查看产品绑定完成度。
@Tag(name = "产品绑定检查(运营端)")
@Validated
@RestController
@RequestMapping("/admin/product-binding-check")
@RequiredArgsConstructor
public class ProductBindingCheckController {

    private final ProductBindingCheckService productBindingCheckService;

    /// 检查指定支付产品的配置绑定完整性
    @Operation(summary = "检查产品绑定完整性")
    @GetMapping("/check")
    public Result<ProductBindingCheckResult> check(
            @NotBlank(message = "{validation.field.product.notBlank}") String product) {
        return Res.ok(productBindingCheckService.check(product));
    }
}
