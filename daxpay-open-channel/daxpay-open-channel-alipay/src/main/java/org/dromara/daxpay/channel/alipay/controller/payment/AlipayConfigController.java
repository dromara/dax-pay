package org.dromara.daxpay.channel.alipay.controller.payment;

import cn.bootx.platform.core.annotation.ClientCode;
import cn.bootx.platform.core.annotation.OperateLog;
import cn.bootx.platform.core.annotation.RequestGroup;
import cn.bootx.platform.core.annotation.RequestPath;
import cn.bootx.platform.core.rest.Res;
import cn.bootx.platform.core.rest.result.Result;
import org.dromara.daxpay.channel.alipay.param.config.AlipayConfigParam;
import org.dromara.daxpay.channel.alipay.result.config.AlipayConfigResult;
import org.dromara.daxpay.channel.alipay.result.config.AlipaySubConfigResult;
import org.dromara.daxpay.channel.alipay.service.payment.config.AlipayConfigService;
import org.dromara.daxpay.service.common.code.DaxPayCode;
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
    @GetMapping("/findById")
    public Result<AlipayConfigResult> findById(@NotNull(message = "ID不可为空") Long id) {
        return Res.ok(alipayConfigService.findById(id));
    }
    @RequestPath("新增或更新商户配置")
    @Operation(summary = "新增或更新商户配置")
    @PostMapping("/saveOrUpdate")
    @OperateLog(title = "新增或更新支付宝商户配置", businessType = OperateLog.BusinessType.UPDATE, saveParam = true)
    public Result<Void> saveOrUpdate(@RequestBody @Validated AlipayConfigParam param) {
        alipayConfigService.saveOrUpdate(param);
        return Res.ok();
    }



}
