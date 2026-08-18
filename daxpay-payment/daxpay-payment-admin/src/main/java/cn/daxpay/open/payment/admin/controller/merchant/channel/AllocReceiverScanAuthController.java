package cn.daxpay.open.payment.admin.controller.merchant.channel;

import cn.daxpay.open.payment.auth.develop.AllocReceiverScanAuthParam;
import cn.daxpay.open.payment.auth.develop.AllocReceiverScanAuthService;
import cn.daxpay.open.payment.unipay.result.assist.AuthResult;
import cn.daxpay.open.payment.unipay.result.assist.AuthUrlResult;
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

/// 分账接收方扫码授权(管理)
///
/// 接收方报备表单扫码获取 openId/userId: 生成 OAuth 授权链接(前端渲染二维码) +
/// queryCode 轮询取结果, 复用认证域统一会话机制(与转账扫码/认证调试一致)。
/// 权限与分账接收方报备同域(通道商户菜单), 不依赖开发调试菜单权限。
@PermCode(menuCode = PermCodes.Channel.Merchant.MENU)
@Validated
@Tag(name = "分账接收方扫码授权服务")
@RestController
@RequestMapping("/admin/channel/merchant/alloc-scan-auth")
@RequiredArgsConstructor
public class AllocReceiverScanAuthController {

    private final AllocReceiverScanAuthService allocReceiverScanAuthService;

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "生成接收方扫码授权链接")
    @PostMapping("/generate-url")
    public Result<AuthUrlResult> generateUrl(@Validated @RequestBody AllocReceiverScanAuthParam param) {
        return Res.ok(allocReceiverScanAuthService.generateScanAuthUrl(param));
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "通过查询码获取授权结果")
    @GetMapping("/query-result")
    public Result<AuthResult> queryResult(
            @NotBlank(message = "{validation.field.queryCode.notBlank}") String queryCode) {
        return Res.ok(allocReceiverScanAuthService.queryAuthResult(queryCode));
    }
}
