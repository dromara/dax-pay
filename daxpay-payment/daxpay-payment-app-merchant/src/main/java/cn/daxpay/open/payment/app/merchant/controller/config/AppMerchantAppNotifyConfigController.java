package cn.daxpay.open.payment.app.merchant.controller.config;

import cn.daxpay.open.payment.app.merchant.service.config.AppMerchantAppNotifyConfigService;
import cn.daxpay.open.payment.merchant.param.config.MchAppNotifyConfigParam;
import cn.daxpay.open.payment.merchant.result.config.MchAppNotifyConfigResult;
import cn.daxpay.open.platform.core.annotation.PermCode;
import cn.daxpay.open.platform.core.code.PermCodes;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/// # 商户应用事件通知配置(商户移动端)
///
/// 面向商户移动端的应用通知配置管理。业务编排委托 [AppMerchantAppNotifyConfigService]。
@PermCode(menuCode = PermCodes.Merchant.NotifyConfig.MENU)
@Validated
@Tag(name = "商户应用事件通知配置(商户移动端)")
@RestController
@RequestMapping("/app-merchant/merchant/app-notify-config")
@RequiredArgsConstructor
public class AppMerchantAppNotifyConfigController {

    private final AppMerchantAppNotifyConfigService notifyConfigService;

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "根据应用ID查询通知配置")
    @GetMapping("/get-by-app-id")
    public Result<MchAppNotifyConfigResult> findByAppId(
            @NotBlank(message = "{validation.field.appId.notBlank}") String appId) {
        return Res.ok(notifyConfigService.findByAppId(appId));
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "保存或更新通知配置")
    @PostMapping("/save-or-update")
    public Result<Void> saveOrUpdate(@RequestBody @Validated MchAppNotifyConfigParam param) {
        notifyConfigService.saveOrUpdate(param);
        return Res.ok();
    }
}
