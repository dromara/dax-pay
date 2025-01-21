package org.dromara.daxpay.channel.wechat.controller;

import cn.bootx.platform.core.annotation.RequestGroup;
import cn.bootx.platform.core.annotation.RequestPath;
import cn.bootx.platform.core.rest.Res;
import cn.bootx.platform.core.rest.result.Result;
import cn.bootx.platform.core.util.ValidationUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.dromara.daxpay.channel.wechat.param.config.WechatPayConfigParam;
import org.dromara.daxpay.channel.wechat.result.config.WechatPayConfigResult;
import org.dromara.daxpay.channel.wechat.service.config.WechatPayConfigService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author xxm
 * @since 2021/3/19
 */
@Validated
@Tag(name = "微信支付配置")
@RestController
@RequestMapping("/wechat/pay/config")
@RequestGroup(groupCode = "WechatPayConfig", groupName = "微信支付配置", moduleCode = "wechatPay", moduleName = "微信支付")
@AllArgsConstructor
public class WechatPayConfigController {

    private final WechatPayConfigService wechatPayConfigService;

    @RequestPath("获取配置")
    @Operation(summary = "获取配置")
    @GetMapping("/findById")
    public Result<WechatPayConfigResult> findById(@NotNull(message = "ID不可为空") Long id) {
        return Res.ok(wechatPayConfigService.findById(id));
    }

    @RequestPath("新增或更新")
    @Operation(summary = "新增或更新")
    @PostMapping("/saveOrUpdate")
    public Result<Void> saveOrUpdate(@RequestBody WechatPayConfigParam param) {
        ValidationUtil.validateParam(param);
        wechatPayConfigService.saveOrUpdate(param);
        return Res.ok();
    }

}
