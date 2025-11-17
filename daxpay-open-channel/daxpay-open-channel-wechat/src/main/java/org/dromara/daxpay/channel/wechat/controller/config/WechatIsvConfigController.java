package org.dromara.daxpay.channel.wechat.controller.config;

import cn.bootx.platform.core.annotation.OperateLog;
import cn.bootx.platform.core.annotation.RequestGroup;
import cn.bootx.platform.core.annotation.RequestPath;
import cn.bootx.platform.core.rest.Res;
import cn.bootx.platform.core.rest.result.Result;
import org.dromara.daxpay.channel.wechat.param.config.WechatIsvConfigParam;
import org.dromara.daxpay.channel.wechat.result.config.WechatIsvConfigResult;
import org.dromara.daxpay.channel.wechat.service.config.WechatIsvConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 微信服务商配置控制器
 * @author xxm
 * @since 2024/12/27
 */
@Validated
@Tag(name = "微信服务商配置")
@RestController
@RequestMapping("/wechat/config/isv")
@RequestGroup(groupCode = "WechatIsvConfig", groupName = "微信服务商配置", moduleCode = "channel")
@RequiredArgsConstructor
public class WechatIsvConfigController {
    private final WechatIsvConfigService wechatIsvConfigService;

    @RequestPath("根据服务商编号获取配置")
    @Operation(summary = "根据服务商编号获取配置")
    @GetMapping("/findByIsvNo")
    public Result<WechatIsvConfigResult> findByIsvNo(String isvNo) {
        return Res.ok(wechatIsvConfigService.findByIsvNo(isvNo).toResult());
    }

    @RequestPath("更新配置")
    @Operation(summary = "更新配置")
    @PostMapping("/update")
    @OperateLog(title = "更新微信服务商配置", businessType = OperateLog.BusinessType.UPDATE, saveParam = true)
    public Result<Void> update(@RequestBody @Validated WechatIsvConfigParam param) {
        wechatIsvConfigService.update(param);
        return Res.ok();
    }
}
