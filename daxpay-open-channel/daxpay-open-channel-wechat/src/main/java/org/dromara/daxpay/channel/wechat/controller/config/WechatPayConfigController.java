package org.dromara.daxpay.channel.wechat.controller.config;

import cn.bootx.platform.core.annotation.ClientCode;
import cn.bootx.platform.core.annotation.OperateLog;
import cn.bootx.platform.core.annotation.RequestGroup;
import cn.bootx.platform.core.annotation.RequestPath;
import cn.bootx.platform.core.rest.Res;
import cn.bootx.platform.core.rest.result.Result;
import cn.bootx.platform.core.util.ValidationUtil;
import org.dromara.daxpay.channel.wechat.param.config.WechatPayConfigParam;
import org.dromara.daxpay.channel.wechat.param.config.WechatPaySubConfigParam;
import org.dromara.daxpay.channel.wechat.result.config.WechatPayConfigResult;
import org.dromara.daxpay.channel.wechat.result.config.WechatPaySubConfigResult;
import org.dromara.daxpay.channel.wechat.service.config.WechatPayConfigService;
import org.dromara.daxpay.payment.common.code.DaxPayCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 微信支付配置
 * @author xxm
 * @since 2021/3/19
 */
@ClientCode({DaxPayCode.Client.ADMIN,DaxPayCode.Client.MERCHANT})
@Validated
@Tag(name = "微信支付配置")
@RestController
@RequestMapping("/wechat/pay/config")
@RequestGroup(groupCode = "WechatPayConfigEntity", groupName = "微信支付配置", moduleCode = "wechatPay", moduleName = "(DaxPay通道)微信支付")
@AllArgsConstructor
public class WechatPayConfigController {

    private final WechatPayConfigService wechatPayConfigService;

    @RequestPath("获取支付配置")
    @Operation(summary = "获取获取支付配置配置")
    @GetMapping("/findByAppId")
    public Result<WechatPayConfigResult> findByAppId(@NotNull(message = "应用Id不可为空") String appId) {
        return Res.ok(wechatPayConfigService.findByAppId(appId).toResult());
    }

    @RequestPath("获取特约商户配置")
    @Operation(summary = "获取特约商户配置")
    @GetMapping("/findSubByAppId")
    public Result<WechatPaySubConfigResult> findSubByAppId(@NotNull(message = "应用Id不可为空") String appId) {
        return Res.ok(wechatPayConfigService.findSubByAppId(appId).toResult());
    }

    @RequestPath("更新商户配置")
    @Operation(summary = "更新商户配置")
    @PostMapping("/update")
    @OperateLog(title = "更新微信商户支付配置", businessType = OperateLog.BusinessType.UPDATE, saveParam = true)
    public Result<Void> update(@RequestBody @Validated() WechatPayConfigParam param) {
        ValidationUtil.validateParam(param);
        wechatPayConfigService.update(param);
        return Res.ok();
    }

    @RequestPath("更新特约商户配置")
    @Operation(summary = "更新特约商户配置")
    @PostMapping("/updateSub")
    @OperateLog(title = "更新微信特约商户支付配置", businessType = OperateLog.BusinessType.UPDATE, saveParam = true)
    public Result<Void> updateSub(@RequestBody @Validated WechatPaySubConfigParam param) {
        ValidationUtil.validateParam(param);
        wechatPayConfigService.updateSub(param);
        return Res.ok();
    }

}
