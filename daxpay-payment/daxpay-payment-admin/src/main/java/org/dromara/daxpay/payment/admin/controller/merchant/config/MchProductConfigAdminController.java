package org.dromara.daxpay.payment.admin.controller.merchant.config;

import org.dromara.daxpay.platform.core.annotation.PermCode;
import org.dromara.daxpay.platform.core.rest.Res;
import org.dromara.daxpay.platform.core.rest.result.Result;
import org.dromara.daxpay.payment.merchant.param.config.MchProductConfigBatchParam;
import org.dromara.daxpay.payment.merchant.param.config.MchProductConfigEnableParam;
import org.dromara.daxpay.payment.merchant.result.config.MchProductConfigResult;
import org.dromara.daxpay.payment.merchant.service.config.MchProductConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/// # 商户产品配置管理控制器
///
@PermCode(menuCode = "payment:merchant")
@Validated
@Tag(name = "商户产品配置管理")
@RestController
@RequestMapping("/admin/merchant/product/config")
@RequiredArgsConstructor
public class MchProductConfigAdminController {

    private final MchProductConfigService service;

    @PermCode(code = "view", nameCn = "商户查看", nameEn = "Merchant View")
    @Operation(summary = "根据商户号查询产品配置列表")
    @GetMapping("/all-by-mch-no")
    public Result<List<MchProductConfigResult>> findAllByMchNo(@NotBlank(message = "{validation.field.mchNo.notBlank}") String mchNo) {
        return Res.ok(service.findAllByMchNo(mchNo));
    }

    @PermCode(code = "product_config_update", nameCn = "产品配置更新", nameEn = "Product Config Update")
    @Operation(summary = "更新启用状态，如果配置不存在则自动创建")
    @PostMapping("/update-enable")
    public Result<Void> updateEnable(@RequestBody @Validated MchProductConfigEnableParam param) {
        service.updateEnable(param);
        return Res.ok();
    }

    @PermCode(code = "product_config_update", nameCn = "产品配置更新", nameEn = "Product Config Update")
    @Operation(summary = "批量保存商户产品配置")
    @PostMapping("/save-batch")
    public Result<Void> saveBatch(@RequestBody @Validated MchProductConfigBatchParam param) {
        service.saveBatch(param);
        return Res.ok();
    }
}
