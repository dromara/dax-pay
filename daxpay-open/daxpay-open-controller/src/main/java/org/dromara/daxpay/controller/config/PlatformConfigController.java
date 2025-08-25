package org.dromara.daxpay.controller.config;

import cn.bootx.platform.core.annotation.ClientCode;
import cn.bootx.platform.core.annotation.IgnoreAuth;
import cn.bootx.platform.core.annotation.RequestGroup;
import cn.bootx.platform.core.annotation.RequestPath;
import cn.bootx.platform.core.rest.Res;
import cn.bootx.platform.core.rest.result.Result;
import org.dromara.daxpay.service.common.code.DaxPayCode;
import org.dromara.daxpay.service.common.param.config.PlatformBasicConfigParam;
import org.dromara.daxpay.service.common.param.config.PlatformCashoutsConfigParam;
import org.dromara.daxpay.service.common.param.config.PlatformUrlConfigParam;
import org.dromara.daxpay.service.common.param.config.PlatformWebsiteConfigPram;
import org.dromara.daxpay.service.common.result.config.PlatformBasicConfigResult;
import org.dromara.daxpay.service.common.result.config.PlatformUrlConfigResult;
import org.dromara.daxpay.service.common.result.config.PlatformWebsiteConfigResult;
import org.dromara.daxpay.service.common.service.config.PlatformConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 平台配置
 * @author xxm
 * @since 2024/9/20
 */
@Validated
@ClientCode(DaxPayCode.Client.ADMIN)
@Tag(name = "平台配置")
@RestController
@RequestMapping("/platform/config")
@RequestGroup(groupCode = "PlatformBasicConfig", groupName = "支付平台配置", moduleCode = "DaxPayPlatform", moduleName = "(DaxPay)支付平台")
@RequiredArgsConstructor
public class PlatformConfigController {
    private final PlatformConfigService platformConfigService;

    @IgnoreAuth
    @Operation(summary = "获取平台基础配置")
    @GetMapping("/basic/get")
    public Result<PlatformBasicConfigResult> getBasicConfig() {
        return Res.ok(platformConfigService.findBasicConfig());
    }

    @RequestPath("更新平台基础配置")
    @Operation(summary = "更新平台基础配置")
    @PostMapping("/basic/update")
    public Result<Void> updateBasic(@RequestBody @Validated PlatformBasicConfigParam param) {
        platformConfigService.updateBasic(param);
        return Res.ok();
    }

    @IgnoreAuth
    @Operation(summary = "获取平台访问地址配置")
    @GetMapping("/url/get")
    public Result<PlatformUrlConfigResult> getUrlConfig() {
        return Res.ok(platformConfigService.findUrlConfig());
    }

    @RequestPath("更新平台访问地址配置")
    @Operation(summary = "更新平台访问地址配置")
    @PostMapping("/url/update")
    public Result<Void> updateUrl(@RequestBody @Validated PlatformUrlConfigParam param) {
        platformConfigService.updateUrl(param);
        return Res.ok();
    }

    @IgnoreAuth
    @Operation(summary = "获取平台站点配置")
    @GetMapping("/website/get")
    public Result<PlatformWebsiteConfigResult> getWebsiteConfig() {
        return Res.ok(platformConfigService.findWebsiteConfig());
    }

    @RequestPath("更新平台站点配置")
    @Operation(summary = "更新平台站点配置")
    @PostMapping("/website/update")
    public Result<Void> updateWebsite(@RequestBody @Validated PlatformWebsiteConfigPram param) {
        platformConfigService.updateWebsite(param);
        return Res.ok();
    }
}
