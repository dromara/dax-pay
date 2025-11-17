package org.dromara.daxpay.payment.merchant.controller.gateway;

import cn.bootx.platform.core.annotation.ClientCode;
import cn.bootx.platform.core.annotation.RequestGroup;
import cn.bootx.platform.core.annotation.RequestPath;
import cn.bootx.platform.core.rest.Res;
import cn.bootx.platform.core.rest.result.Result;
import org.dromara.daxpay.payment.common.code.DaxPayCode;
import org.dromara.daxpay.payment.merchant.entity.gateway.MiniQuicklyConfig;
import org.dromara.daxpay.payment.merchant.param.gateway.MiniQuicklyConfigParam;
import org.dromara.daxpay.payment.merchant.service.gateway.MiniQuicklyConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 小程序快捷支付配置
 * @author xxm
 * @since 2024/11/20
 */
@Validated
@ClientCode({DaxPayCode.Client.ADMIN, DaxPayCode.Client.MERCHANT})
@Tag(name = "小程序快捷支付配置")
@RestController
@RequestMapping("/mch/mini/quickly/config")
@RequestGroup(groupCode = "MiniQuicklyConfig", groupName = "小程序快捷支付配置", moduleCode = "gateway")
@RequiredArgsConstructor
public class MiniQuicklyConfigController {
    private final MiniQuicklyConfigService miniQuicklyConfigService;

    @RequestPath("根据应用ID查询")
    @Operation(summary = "根据应用ID查询")
    @GetMapping("/findByAppId")
    public Result<MiniQuicklyConfig> findByAppId(@NotBlank(message = "应用ID不可为空") String appId){
        return Res.ok(miniQuicklyConfigService.findConfigByAppId(appId));
    }

    @RequestPath("更新")
    @Operation(summary = "更新")
    @PostMapping("/update")
    public Result<Void> update(@Validated @RequestBody MiniQuicklyConfigParam param){
        miniQuicklyConfigService.updateConfig(param);
        return Res.ok();
    }
}
