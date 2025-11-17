package org.dromara.daxpay.channel.alipay.controller.config;

import cn.bootx.platform.core.annotation.ClientCode;
import cn.bootx.platform.core.annotation.OperateLog;
import cn.bootx.platform.core.annotation.RequestGroup;
import cn.bootx.platform.core.annotation.RequestPath;
import cn.bootx.platform.core.rest.Res;
import cn.bootx.platform.core.rest.result.Result;
import org.dromara.daxpay.channel.alipay.param.config.AlipayIsvConfigParam;
import org.dromara.daxpay.channel.alipay.result.config.AlipayIsvConfigResult;
import org.dromara.daxpay.channel.alipay.service.config.AlipayIsvConfigService;
import org.dromara.daxpay.payment.common.code.DaxPayCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 支付宝服务商配置
 * @author xxm
 * @since 2024/10/31
 */
@ClientCode({DaxPayCode.Client.ADMIN})
@Validated
@Tag(name = "支付宝服务商配置控制器")
@RestController
@RequestMapping("/alipay/config/isv")
@RequiredArgsConstructor
@RequestGroup(groupCode = "AlipayIsvConfig", groupName = "支付宝服务商配置", moduleCode = "alipay")
public class AlipayIsvConfigController {
    private final AlipayIsvConfigService alipayConfigService;

    @RequestPath("获取配置")
    @Operation(summary = "获取配置")
    @GetMapping("/findByIsvNo")
    public Result<AlipayIsvConfigResult> findByIsvNo(@NotNull(message = "服务商号不可为空") String isvNo) {
        return Res.ok(alipayConfigService.findByIsvNo(isvNo).toResult());
    }

    @RequestPath("更新")
    @Operation(summary = "更新")
    @PostMapping("/update")
    @OperateLog(title = "更新支付宝服务商配置", businessType = OperateLog.BusinessType.UPDATE, saveParam = true)
    public Result<Void> update(@RequestBody @Validated AlipayIsvConfigParam param) {
        alipayConfigService.update(param);
        return Res.ok();
    }

}

