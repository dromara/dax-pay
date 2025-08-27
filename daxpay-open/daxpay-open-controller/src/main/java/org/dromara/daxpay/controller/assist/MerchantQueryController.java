package org.dromara.daxpay.controller.assist;

import cn.bootx.platform.core.annotation.RequestGroup;
import cn.bootx.platform.core.annotation.RequestPath;
import cn.bootx.platform.core.rest.Res;
import cn.bootx.platform.core.rest.dto.LabelValue;
import cn.bootx.platform.core.rest.result.Result;
import org.dromara.daxpay.service.merchant.service.info.MerchantInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 商户查询控制器
 * @author xxm
 * @since 2024/6/25
 */
@Validated
@Tag(name = "商户信息查询控制器")
@RestController
@RequestMapping("/merchant/query")
@RequestGroup(groupCode = "MerchantInfo", groupName = "商户信息查询", moduleCode = "merchant")
@RequiredArgsConstructor
public class MerchantQueryController {
    private final MerchantInfoService merchantInfoService;

    @RequestPath("商户下拉列表")
    @Operation(summary = "商户下拉列表")
    @GetMapping("/dropdown")
    public Result<List<LabelValue>> dropdown(){
        return Res.ok(merchantInfoService.dropdown());
    }

    @RequestPath("启用的商户下拉列表")
    @Operation(summary = "启用的商户下拉列表")
    @GetMapping("/dropdownByEnable")
    public Result<List<LabelValue>> dropdownByEnable(){
        return Res.ok(merchantInfoService.dropdownByEnable());
    }

}
