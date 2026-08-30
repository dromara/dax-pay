package cn.daxpay.open.plugin.easypay.controller.appadmin;

import cn.daxpay.open.platform.core.annotation.PermCode;
import cn.daxpay.open.platform.core.code.PermCodes;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.result.Result;
import cn.daxpay.open.plugin.easypay.param.config.EasyPayCredentialParam;
import cn.daxpay.open.plugin.easypay.result.config.EasyPayCredentialResult;
import cn.daxpay.open.plugin.easypay.service.config.EasyPayCredentialService;
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

/// 小程序管理端-易支付凭证配置
///
/// 镜像自 `EasyPayCredentialController`(admin/mch 双路径), 保留小程序端易支付配置页
/// 用到的查询与更新端点; 同权限码同 Service。
@PermCode(menuCode = PermCodes.Merchant.EasyPay.MENU)
@Validated
@Tag(name = "小程序管理端-易支付凭证配置")
@RestController
@RequestMapping("/app-admin/easypay/credential")
@RequiredArgsConstructor
public class AppAdminEasyPayCredentialController {

    private final EasyPayCredentialService easyPayCredentialService;

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "按应用号查询凭证")
    @GetMapping("/get-by-app-id")
    public Result<EasyPayCredentialResult> getByAppId(
            @NotBlank(message = "{validation.field.appId.notBlank}") String appId) {
        return Res.ok(easyPayCredentialService.findResultByAppId(appId));
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "更新凭证")
    @PostMapping("/update")
    public Result<Void> update(@RequestBody @Validated EasyPayCredentialParam param) {
        easyPayCredentialService.update(param);
        return Res.ok();
    }
}
