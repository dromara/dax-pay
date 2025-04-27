package org.dromara.daxpay.controller.config;

import cn.bootx.platform.core.annotation.RequestGroup;
import cn.bootx.platform.core.annotation.RequestPath;
import cn.bootx.platform.core.rest.Res;
import cn.bootx.platform.core.rest.result.Result;
import org.dromara.daxpay.service.param.config.NotifySubscribeParam;
import org.dromara.daxpay.service.result.config.MerchantNotifyConfigResult;
import org.dromara.daxpay.service.service.config.MerchantNotifyConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 商户通知配置控制器
 * @author xxm
 * @since 2024/8/2
 */
@Validated
@RequestGroup(groupCode = "MerchantNotifyConfig", groupName = "商户通知配置", moduleCode = "PayConfig")
@Tag(name = "商户订阅通知配置")
@RestController
@RequestMapping("/merchant/notify/config")
@RequiredArgsConstructor
public class MerchantNotifyConfigController {
    private final MerchantNotifyConfigService service;

    @RequestPath("查询列表")
    @Operation(summary = "查询列表")
    @GetMapping("/findAllByAppId")
    public Result<List<MerchantNotifyConfigResult>> findAllByAppId(@NotNull(message = "应用AppId不可为空") String appId) {
        return Res.ok(service.findAllByAppId(appId));
    }

    @RequestPath("商户消息订阅配置")
    @Operation(summary = "商户消息订阅配置")
    @PostMapping("/subscribe")
    public Result<Void> subscribe(@RequestBody @Validated NotifySubscribeParam param) {
        service.subscribe(param.getAppId(), param.getNotifyType(), param.isSubscribe());
        return Res.ok();
    }
}
