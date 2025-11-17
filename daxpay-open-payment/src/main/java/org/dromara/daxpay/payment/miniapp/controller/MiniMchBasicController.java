package org.dromara.daxpay.payment.miniapp.controller;

import cn.bootx.platform.core.annotation.ClientCode;
import cn.bootx.platform.core.annotation.RequestGroup;
import cn.bootx.platform.core.annotation.RequestPath;
import cn.bootx.platform.core.rest.Res;
import cn.bootx.platform.core.rest.dto.LabelValue;
import cn.bootx.platform.core.rest.result.Result;
import org.dromara.daxpay.payment.common.code.DaxPayCode;
import org.dromara.daxpay.payment.merchant.result.app.MchAppResult;
import org.dromara.daxpay.payment.merchant.result.config.ChannelConfigResult;
import org.dromara.daxpay.payment.merchant.result.info.MerchantResult;
import org.dromara.daxpay.payment.miniapp.service.MiniMchBasicService;
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
 * 小程序基础接口
 * @author xxm
 * @since 2025/4/22
 */
@Validated
@ClientCode(DaxPayCode.Client.MERCHANT)
@Tag(name = "小程序基础接口")
@RestController
@RequestMapping("/mini/mch")
@RequestGroup(groupCode = "MiniMchBasic", groupName = "小程序基础接口", moduleCode = "MiniMch", moduleName = "(DaxPay)商户端微信小程序")
@RequiredArgsConstructor
public class MiniMchBasicController {
    private final MiniMchBasicService miniAppBasicService;

    @RequestPath("查询商户信息")
    @Operation(summary = "查询商户信息")
    @GetMapping("/findMchInfo")
    public Result<MerchantResult> findMchInfo(){
        return Res.ok(miniAppBasicService.findMchInfo());
    }

    @RequestPath("所有商户应用下拉列表")
    @Operation(summary = "所有商户应用下拉列表")
    @GetMapping("/dropdownMchApp")
    public Result<List<LabelValue>> dropdownMchApp(){
        return Res.ok(miniAppBasicService.dropdownMchApp());
    }

    @RequestPath("查询默认应用")
    @Operation(summary = "查询默认应用")
    @GetMapping("/findDefaultMchApp")
    public Result<MchAppResult> findDefaultMchApp(){
        return Res.ok(miniAppBasicService.findDefaultMchApp());
    }

    @RequestPath("根据应用ID查询通道配置列表")
    @Operation(summary = "根据应用ID查询通道配置列表")
    @GetMapping("/findAllConfigByAppId")
    public Result<List<ChannelConfigResult>> findAllConfigByAppId(@NotNull(message = "应用AppId不可为空")  String appId){
        return Res.ok(miniAppBasicService.findAllConfigByAppId(appId));
    }
}
