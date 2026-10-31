package cn.daxpay.open.channel.mock.controller.admin;

import cn.daxpay.open.channel.mock.param.credential.MockIsvCredentialParam;
import cn.daxpay.open.channel.mock.result.credential.MockIsvCredentialResult;
import cn.daxpay.open.channel.mock.service.credential.MockIsvCredentialService;
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

/// # Mock 模拟服务商接入密钥管理(服务商级独立密钥, save/get 配置范式)
///
/// 服务商产品(mock_isv)的网关接入配置维护入口(每服务商一行, 拟真服务商级独立密钥范式):
/// 密钥在模拟网关控制台生成, 运营复制材料粘贴保存; 两段 PEM 留空表示不修改。
/// 保存/查询语义与 [MockDirectCredentialController] 同款(复用通道商户通用菜单 + 动作码)。
/// 随 [cn.daxpay.open.channel.mock.MockChannelApp] 仅 DEV 装配, PROD 下端点不存在。
@PermCode(menuCode = PermCodes.Channel.Merchant.MENU)
@Tag(name = "Mock模拟服务商接入密钥管理")
@RestController
@RequestMapping("/admin/mock/isv-credential")
@RequiredArgsConstructor
@Validated
public class MockIsvCredentialController {

    private final MockIsvCredentialService mockIsvCredentialService;

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "保存服务商接入密钥(按服务商编号 upsert, 两段密钥留空表示不修改)")
    @PostMapping("/save")
    public Result<Void> save(@RequestBody @Validated MockIsvCredentialParam param) {
        mockIsvCredentialService.save(param);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "查询服务商接入密钥(敏感字段脱敏, 未保存过时 configured=false)")
    @GetMapping("/get")
    public Result<MockIsvCredentialResult> get(
            @NotBlank(message = "{validation.field.isvNo.notBlank}") String isvNo) {
        return Res.ok(mockIsvCredentialService.getMasked(isvNo));
    }
}
