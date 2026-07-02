package cn.daxpay.open.payment.common.controller.assist;

import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.dto.LabelValue;
import cn.daxpay.open.platform.core.rest.result.Result;
import cn.daxpay.open.payment.merchant.service.info.MerchantInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/// # 商户查询控制器
///
@Validated
@Tag(name = "商户信息查询控制器")
@RestController
@RequestMapping("/merchant/query")
@RequiredArgsConstructor
public class MerchantQueryController {
    private final MerchantInfoService merchantInfoService;

    @Operation(summary = "商户下拉列表")
    @GetMapping("/dropdown")
    public Result<List<LabelValue>> dropdown(){
        return Res.ok(merchantInfoService.dropdown());
    }

    @Operation(summary = "启用的商户下拉列表")
    @GetMapping("/dropdown-by-enable")
    public Result<List<LabelValue>> dropdownByEnable(){
        return Res.ok(merchantInfoService.dropdownByEnable());
    }

}

