package cn.daxpay.open.channel.mock.controller.admin;

import cn.daxpay.open.channel.mock.param.credential.MockDirectCredentialParam;
import cn.daxpay.open.channel.mock.result.credential.MockDirectCredentialResult;
import cn.daxpay.open.channel.mock.service.credential.MockDirectCredentialService;
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

/// # Mock 模拟通道直连商户接入密钥管理(商户级拟真凭证)
///
/// 直连产品(mock_pay)的商户级网关接入配置维护入口(每通道商户一行, 拟真真实通道每商户一套密钥):
/// 密钥在模拟网关控制台按商户生成, 运营复制材料粘贴保存; 两段 PEM 留空表示不修改。
/// 复用通道商户通用菜单 + 动作码。
/// 随 [cn.daxpay.open.channel.mock.MockChannelApp] 仅 DEV 装配, PROD 下端点不存在。
@PermCode(menuCode = PermCodes.Channel.Merchant.MENU)
@Tag(name = "Mock模拟通道直连商户接入密钥管理")
@RestController
@RequestMapping("/admin/mock/direct-credential")
@RequiredArgsConstructor
@Validated
public class MockDirectCredentialController {

    private final MockDirectCredentialService mockDirectCredentialService;

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "保存直连商户接入密钥(按通道商户号 upsert, 两段密钥可分别留空表示不修改)")
    @PostMapping("/save")
    public Result<Void> save(@RequestBody @Validated MockDirectCredentialParam param) {
        mockDirectCredentialService.save(param);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "查询直连商户接入密钥(敏感字段脱敏, 未保存过时 configured=false)")
    @GetMapping("/find")
    public Result<MockDirectCredentialResult> find(
            @NotBlank(message = "{validation.field.channelMerchantNo.notBlank}") String channelMchNo) {
        return Res.ok(mockDirectCredentialService.getMasked(channelMchNo));
    }
}
