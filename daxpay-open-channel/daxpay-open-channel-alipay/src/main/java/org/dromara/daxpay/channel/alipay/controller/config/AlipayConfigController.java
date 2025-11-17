package org.dromara.daxpay.channel.alipay.controller.config;

import cn.bootx.platform.core.annotation.ClientCode;
import cn.bootx.platform.core.annotation.OperateLog;
import cn.bootx.platform.core.annotation.RequestGroup;
import cn.bootx.platform.core.annotation.RequestPath;
import cn.bootx.platform.core.rest.Res;
import cn.bootx.platform.core.rest.result.Result;
import org.dromara.daxpay.channel.alipay.param.config.AlipayConfigParam;
import org.dromara.daxpay.channel.alipay.param.config.AlipaySubConfigParam;
import org.dromara.daxpay.channel.alipay.result.config.AlipayConfigResult;
import org.dromara.daxpay.channel.alipay.result.config.AlipaySubConfigResult;
import org.dromara.daxpay.channel.alipay.service.config.AlipayConfigService;
import org.dromara.daxpay.payment.common.code.DaxPayCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 支付宝配置控制器
 * @author xxm
 * @since 2024/6/26
 */
@ClientCode({DaxPayCode.Client.ADMIN, DaxPayCode.Client.MERCHANT})
@Validated
@Tag(name = "支付宝支付配置控制器")
@RestController
@RequestMapping("/alipay/config")
@RequiredArgsConstructor
@RequestGroup(groupCode = "AlipayConfig", groupName = "支付宝支付配置", moduleCode = "alipay", moduleName = "(DaxPay通道)支付宝")
public class AlipayConfigController {
    private final AlipayConfigService alipayConfigService;

    @RequestPath("获取商户配置")
    @Operation(summary = "获取商户配置")
    @GetMapping("/findByAppId")
    public Result<AlipayConfigResult> findByAppId(@NotNull(message = "商户应用Id不可为空") String appId) {
        return Res.ok(alipayConfigService.findByAppId(appId).toResult());
    }
    @RequestPath("获取子商户配置")
    @Operation(summary = "获取子商户配置")
    @GetMapping("/findSubByAppId")
    public Result<AlipaySubConfigResult> findSubByAppId(@NotNull(message = "商户应用Id不可为空") String appId) {
        return Res.ok(alipayConfigService.findSubByAppId(appId).toResult());
    }

    @RequestPath("更新商户配置")
    @Operation(summary = "更新商户配置")
    @PostMapping("/update")
    @OperateLog(title = "更新支付宝商户配置", businessType = OperateLog.BusinessType.UPDATE, saveParam = true)
    public Result<Void> update(@RequestBody @Validated AlipayConfigParam param) {
        alipayConfigService.update(param);
        return Res.ok();
    }

    @RequestPath("更新子商户配置")
    @Operation(summary = "更新子商户配置")
    @PostMapping("/updateSub")
    @OperateLog(title = "更新支付宝子商户配置", businessType = OperateLog.BusinessType.UPDATE, saveParam = true)
    public Result<Void> updateSub(@RequestBody @Validated AlipaySubConfigParam param) {
        alipayConfigService.updateSub(param);
        return Res.ok();
    }
}
