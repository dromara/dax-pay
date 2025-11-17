package org.dromara.daxpay.payment.common.controller.assist;

import cn.bootx.platform.core.annotation.ClientCode;
import cn.bootx.platform.core.annotation.RequestGroup;
import cn.bootx.platform.core.annotation.RequestPath;
import cn.bootx.platform.core.rest.Res;
import cn.bootx.platform.core.rest.dto.LabelValue;
import cn.bootx.platform.core.rest.result.Result;
import org.dromara.daxpay.payment.common.code.DaxPayCode;
import org.dromara.daxpay.payment.isv.service.info.IsvInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Validated
@Tag(name = "服务商信息查询")
@RestController
@RequestMapping("/isv/query")
@ClientCode({DaxPayCode.Client.ADMIN, DaxPayCode.Client.MERCHANT})
@RequestGroup(groupCode = "IsvQuery", groupName = "服务商信息查询", moduleCode = "paymentAssist")
@RequiredArgsConstructor
public class IsvQueryController {
    private final IsvInfoService isvInfoService;

    @RequestPath("查询服务商下拉列表")
    @Operation(summary = "查询服务商下拉列表")
    @GetMapping("/dropdown")
    public Result<List<LabelValue>> dropdown(){
        return Res.ok(isvInfoService.dropdown());
    }

    @RequestPath("查询启用的服务商下拉列表")
    @Operation(summary = "查询启用的服务商下拉列表")
    @GetMapping("/dropdownByEnable")
    public Result<List<LabelValue>> dropdownByEnable(){
        return Res.ok(isvInfoService.dropdownByEnable());
    }

}
