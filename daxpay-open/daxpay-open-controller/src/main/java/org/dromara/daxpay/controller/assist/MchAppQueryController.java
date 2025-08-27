package org.dromara.daxpay.controller.assist;

import cn.bootx.platform.core.annotation.ClientCode;
import cn.bootx.platform.core.annotation.RequestGroup;
import cn.bootx.platform.core.annotation.RequestPath;
import cn.bootx.platform.core.rest.Res;
import cn.bootx.platform.core.rest.dto.LabelValue;
import cn.bootx.platform.core.rest.result.Result;
import org.dromara.daxpay.service.common.code.DaxPayCode;
import org.dromara.daxpay.service.merchant.service.app.MchAppService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 商户应用信息查询
 * @author xxm
 * @since 2025/7/7
 */
@Validated
@Tag(name = "商户应用信息查询")
@RestController
@RequestMapping("/mch/app/query")
@ClientCode({DaxPayCode.Client.ADMIN, DaxPayCode.Client.MERCHANT})
@RequestGroup(groupCode = "MchAppQuery", groupName = "商户应用信息查询", moduleCode = "paymentAssist")
@RequiredArgsConstructor
public class MchAppQueryController {
    private final MchAppService mchAppService;

    @RequestPath("查询应用下拉列表")
    @Operation(summary = "查询启用的商户应用下拉列表")
    @GetMapping("/dropdown")
    public Result<List<LabelValue>> dropdown(){
        return Res.ok(mchAppService.dropdown(null));
    }

    @RequestPath("查询启应用下拉列表")
    @Operation(summary = "查询商户应用下拉列表")
    @GetMapping("/dropdownByEnable")
    public Result<List<LabelValue>> dropdownByEnable(){
        return Res.ok(mchAppService.dropdownByEnable(null));
    }

    @RequestPath("根据商户号查询应用下拉列表")
    @Operation(summary = "根据商户号查询启用的商户应用下拉列表")
    @GetMapping("/dropdownByMchNo")
    public Result<List<LabelValue>> dropdownByMchNo(@NotNull(message = "商户号不可为空")String mchNo){
        return Res.ok(mchAppService.dropdown(mchNo));
    }

    @RequestPath("根据商户号查询启应用下拉列表")
    @Operation(summary = "根据商户号查询商户应用下拉列表")
    @GetMapping("/dropdownEnableByMchNo")
    public Result<List<LabelValue>> dropdownEnableByMchNo(@NotNull(message = "商户号不可为空")String mchNo){
        return Res.ok(mchAppService.dropdownByEnable(mchNo));
    }
}
