package cn.daxpay.open.payment.app.admin.controller.merchant.channel;

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

/// 分账接收方扫码授权(小程序管理端镜像)
///
/// 对应 admin 版 [AllocReceiverScanAuthController], 复用同一 Service 与权限码。
/// 移动端无渲染二维码场景, 生成授权链接后复制/转发给接收方, queryCode 轮询取结果。
@PermCode(menuCode = PermCodes.Channel.Merchant.MENU)
@Validated
@Tag(name = "小程序管理端-分账接收方扫码授权服务")
@RestController
@RequestMapping("/app-admin/channel/merchant/alloc-scan-auth")
@RequiredArgsConstructor
public class AppAdminAllocReceiverScanAuthController {

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
