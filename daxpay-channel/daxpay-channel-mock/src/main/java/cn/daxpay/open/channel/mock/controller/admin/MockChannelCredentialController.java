package cn.daxpay.open.channel.mock.controller.admin;

import cn.daxpay.open.channel.mock.param.credential.MockChannelCredentialParam;
import cn.daxpay.open.channel.mock.result.credential.MockChannelCredentialResult;
import cn.daxpay.open.channel.mock.service.credential.MockChannelCredentialService;
import cn.daxpay.open.platform.core.annotation.PermCode;
import cn.daxpay.open.platform.core.code.PermCodes;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/// # Mock 模拟通道网关签名密钥配置管理(通道级全局单行)
///
/// 模拟通道与模拟网关侧成对一致的网关签名密钥对维护入口(全局单行, id 固定 1):
/// 商户私钥(请求签名) + 平台公钥(响应/回调验签); 运营复制模拟网关控制台材料粘贴保存,
/// 两段 PEM 留空表示不修改([MockChannelCredentialParam] 空串转 null, 更新时空值不覆盖)。
/// 商户级/服务商级接入配置未配置时回落本全局单行(见
/// [cn.daxpay.open.channel.mock.service.credential.MockChannelCredentialService#getRequiredConfigured])。
/// 复用通道商户通用菜单 + 动作码。
/// 随 [cn.daxpay.open.channel.mock.MockChannelApp] 仅 DEV 装配, PROD 下端点不存在。
@PermCode(menuCode = PermCodes.Channel.Merchant.MENU)
@Tag(name = "Mock模拟通道网关签名密钥配置管理")
@RestController
@RequestMapping("/admin/mock/credential")
@RequiredArgsConstructor
@Validated
public class MockChannelCredentialController {

    private final MockChannelCredentialService mockChannelCredentialService;

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "查询网关签名密钥配置(敏感字段脱敏, 空行即未配置)")
    @GetMapping("/get")
    public Result<MockChannelCredentialResult> get() {
        return Res.ok(mockChannelCredentialService.getOrInit().toResult());
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "保存网关签名密钥配置(留空字段表示不修改)")
    @PostMapping("/save")
    public Result<Void> save(@RequestBody @Validated MockChannelCredentialParam param) {
        mockChannelCredentialService.save(param);
        return Res.ok();
    }
}
