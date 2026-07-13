package cn.daxpay.open.payment.admin.controller.merchant.config;

import cn.daxpay.open.platform.core.annotation.PermCode;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.result.Result;
import cn.daxpay.open.payment.merchant.param.config.MchAppNotifyConfigParam;
import cn.daxpay.open.payment.merchant.result.config.MchAppNotifyConfigResult;
import cn.daxpay.open.payment.admin.service.merchant.config.MchAppNotifyConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/// # 商户应用事件通知配置管理控制器
///
@PermCode(menuCode = "merchant:notify_config")
@Validated
@Tag(name = "商户应用事件通知配置管理")
@RestController
@RequestMapping("/admin/merchant/app-notify-config")
@RequiredArgsConstructor
public class MchAppNotifyConfigAdminController {

    private final MchAppNotifyConfigService notifyConfigService;

    @PermCode(code = "view", nameCn = "商户查看", nameEn = "Merchant View")
    @Operation(summary = "根据应用ID查询通知配置")
    @GetMapping("/get-by-app-id")
    public Result<MchAppNotifyConfigResult> findByAppId(
            @NotBlank(message = "{validation.field.appId.notBlank}") String appId) {
        return Res.ok(notifyConfigService.findByAppId(appId));
    }

    @PermCode(code = "notify_config_update", nameCn = "通知配置更新", nameEn = "Notify Config Update")
    @Operation(summary = "保存或更新通知配置")
    @PostMapping("/save-or-update")
    public Result<Void> saveOrUpdate(@RequestBody @Validated MchAppNotifyConfigParam param) {
        notifyConfigService.saveOrUpdate(param);
        return Res.ok();
    }
}
