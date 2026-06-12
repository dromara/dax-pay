package org.dromara.daxpay.payment.common.controller.assist;

import org.dromara.daxpay.platform.core.rest.Res;
import org.dromara.daxpay.platform.core.rest.dto.LabelValue;
import org.dromara.daxpay.platform.core.rest.result.Result;
import org.dromara.daxpay.payment.common.service.MchAppInfoAssistQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/// # 商户应用信息查询
///
@Validated
@Tag(name = "商户应用信息查询")
@RestController
@RequestMapping("/mch/app-info/query")
@RequiredArgsConstructor
public class MchAppInfoQueryController {
    private final MchAppInfoAssistQueryService mchAppInfoAssistQueryService;

    @Operation(summary = "查询启用的商户应用下拉列表")
    @GetMapping("/dropdown")
    public Result<List<LabelValue>> dropdown(){
        return Res.ok(mchAppInfoAssistQueryService.dropdown(null));
    }

    @Operation(summary = "查询商户应用下拉列表")
    @GetMapping("/dropdown-by-enable")
    public Result<List<LabelValue>> dropdownByEnable(){
        return Res.ok(mchAppInfoAssistQueryService.dropdownByEnable(null));
    }

    @Operation(summary = "根据商户号查询启用的商户应用下拉列表")
    @GetMapping("/dropdown-by-mch-no")
    public Result<List<LabelValue>> dropdownByMchNo(@NotNull(message = "{validation.field.mchNo.notBlank}")String mchNo){
        return Res.ok(mchAppInfoAssistQueryService.dropdown(mchNo));
    }

    @Operation(summary = "根据商户号查询商户应用下拉列表")
    @GetMapping("/dropdown-enable-by-mch-no")
    public Result<List<LabelValue>> dropdownEnableByMchNo(@NotNull(message = "{validation.field.mchNo.notBlank}")String mchNo){
        return Res.ok(mchAppInfoAssistQueryService.dropdownByEnable(mchNo));
    }
}

