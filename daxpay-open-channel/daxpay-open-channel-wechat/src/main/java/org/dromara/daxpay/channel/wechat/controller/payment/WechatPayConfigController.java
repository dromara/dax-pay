package org.dromara.daxpay.channel.wechat.controller.payment;

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
import org.dromara.daxpay.channel.wechat.service.payment.config.WechatPayConfigService;
import org.dromara.daxpay.service.common.code.DaxPayCode;
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
@ClientCode({DaxPayCode.Client.ADMIN, DaxPayCode.Client.MERCHANT})
@Validated
@Tag(name = "微信支付配置")
@RestController
@RequestMapping("/wechat/pay/config")
@RequestGroup(groupCode = "WechatPayConfig", groupName = "微信支付配置", moduleCode = "wechatPay")
@AllArgsConstructor
public class WechatPayConfigController {

    private final WechatPayConfigService wechatPayConfigService;

    @RequestPath("获取配置")
    @Operation(summary = "获取配置")
    @GetMapping("/findById")
    public Result<WechatPayConfigResult> findById(@NotNull(message = "ID不可为空") Long id) {
        return Res.ok(wechatPayConfigService.findById(id));
    }

    @RequestPath("获取特约商户配置")
    @Operation(summary = "获取特约商户配置")
    @GetMapping("/findSubById")
    public Result<WechatPaySubConfigResult> findSubById(@NotNull(message = "ID不可为空") Long id) {
        return Res.ok(wechatPayConfigService.findSubById(id));
    }

    @RequestPath("新增或更新商户配置")
    @Operation(summary = "新增或更新商户配置")
    @PostMapping("/saveOrUpdate")
    public Result<Void> saveOrUpdate(@RequestBody @Validated() WechatPayConfigParam param) {
        ValidationUtil.validateParam(param);
        wechatPayConfigService.saveOrUpdate(param);
        return Res.ok();
    }

    @RequestPath("新增或更新特约商户配置")
    @Operation(summary = "新增或更新特约商户配置")
    @PostMapping("/saveOrUpdateSub")
    @OperateLog(title = "新增或更新微信特约商户支付配置", businessType = OperateLog.BusinessType.UPDATE, saveParam = true)
    public Result<Void> saveOrUpdateSub(@RequestBody @Validated WechatPaySubConfigParam param) {
        ValidationUtil.validateParam(param);
        wechatPayConfigService.saveOrUpdateSub(param);
        return Res.ok();
    }

}
