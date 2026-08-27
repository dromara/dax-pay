package cn.daxpay.open.payment.admin.controller.masterdata.product;

import cn.daxpay.open.platform.common.config.properties.PlatformConfigProperties;
import cn.daxpay.open.platform.core.annotation.IgnoreAuth;
import cn.daxpay.open.platform.core.annotation.PermCode;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/// # 支付环境全局配置查询
///
/// 沙箱环境开关由配置文件控制(daxpay.platform.config.sandbox-enabled), 运行时只读。
@Tag(name = "支付环境全局配置查询")
@IgnoreAuth(login = true)
@RestController
@RequestMapping("/admin/pay-env")
@RequiredArgsConstructor
public class PayEnvConfigController {

    private final PlatformConfigProperties platformConfigProperties;

    @Operation(summary = "查询沙箱环境开关状态")
    @GetMapping("/sandbox-enabled")
    public Result<Boolean> getSandboxEnabled() {
        return Res.ok(platformConfigProperties.isSandboxEnabled());
    }
}
