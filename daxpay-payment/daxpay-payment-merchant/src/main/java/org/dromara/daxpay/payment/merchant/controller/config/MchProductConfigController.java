package org.dromara.daxpay.payment.merchant.controller.config;

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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/// # 商户产品配置
///
@Validated
@Tag(name = "商户产品配置")
@RestController
@RequestMapping("/merchant/product/config")
@RequiredArgsConstructor
public class MchProductConfigController {
    private final MchProductConfigService service;

    @Operation(summary = "根据商户号查询产品配置列表")
    @GetMapping("/all-by-mch-no")
    public Result<List<MchProductConfigResult>> findAllByMchNo(@NotBlank(message = "{validation.field.mchNo.notBlank}") String mchNo){
        return Res.ok(service.findAllByMchNo(mchNo));
    }

    @Operation(summary = "更新启用状态，如果配置不存在则自动创建")
    @PostMapping("/update-enable")
    public Result<Void> updateEnable(@RequestBody @Validated MchProductConfigEnableParam param){
        service.updateEnable(param);
        return Res.ok();
    }

    @Operation(summary = "批量保存商户产品配置")
    @PostMapping("/save-batch")
    public Result<Void> saveBatch(@RequestBody @Validated MchProductConfigBatchParam param){
        service.saveBatch(param);
        return Res.ok();
    }
}
