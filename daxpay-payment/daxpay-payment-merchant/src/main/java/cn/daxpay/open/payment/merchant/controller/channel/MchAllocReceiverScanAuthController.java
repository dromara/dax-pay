package cn.daxpay.open.payment.merchant.controller.channel;

import cn.daxpay.open.payment.auth.develop.AllocReceiverScanAuthParam;
import cn.daxpay.open.payment.auth.develop.AllocReceiverScanAuthService;
import cn.daxpay.open.payment.common.context.PaymentContext;
import cn.daxpay.open.payment.unipay.result.assist.AuthResult;
import cn.daxpay.open.payment.unipay.result.assist.AuthUrlResult;
import cn.daxpay.open.platform.core.annotation.PermCode;
import cn.daxpay.open.platform.core.code.CommonCode;
import cn.daxpay.open.platform.core.code.PermCodes;
import cn.daxpay.open.platform.core.exception.BizInfoException;
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

/// # 分账接收方扫码授权（商户端）
///
/// 对照运营端 [AllocReceiverScanAuthController]，路径前缀 `/mch/channel/merchant/alloc-scan-auth`。
/// 商户号一律取自 [PaymentContext]，忽略请求中的 mchNo，防越权。
/// 权限与分账接收方报备同域(通道商户菜单), 不依赖开发调试菜单权限。
@PermCode(menuCode = PermCodes.Channel.Merchant.MENU)
@Validated
@Tag(name = "分账接收方扫码授权服务(商户端)")
@RestController
@RequestMapping("/mch/channel/merchant/alloc-scan-auth")
@RequiredArgsConstructor
public class MchAllocReceiverScanAuthController {

    private final AllocReceiverScanAuthService allocReceiverScanAuthService;
    private final PaymentContext paymentContext;

    /// 当前登录商户号（上下文必有；缺则视为会话异常）
    private String requireMchNo() {
        String mchNo = paymentContext.getMchNo();
        if (mchNo == null || mchNo.isBlank()) {
            // 商户上下文缺失
            throw new BizInfoException(CommonCode.FAIL_CODE, "pay.error.assist.mchContextMissing");
        }
        return mchNo;
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "生成接收方扫码授权链接")
    @PostMapping("/generate-url")
    public Result<AuthUrlResult> generateUrl(@Validated @RequestBody AllocReceiverScanAuthParam param) {
        // 强制当前商户，忽略客户端传入的 mchNo
        param.setMchNo(requireMchNo());
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
