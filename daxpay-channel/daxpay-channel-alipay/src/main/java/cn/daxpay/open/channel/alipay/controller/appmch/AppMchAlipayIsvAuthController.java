package cn.daxpay.open.channel.alipay.controller.appmch;

import cn.daxpay.open.channel.alipay.dao.isv.AlipayIsvChannelMerchantManager;
import cn.daxpay.open.channel.alipay.entity.isv.AlipayIsvChannelMerchant;
import cn.daxpay.open.channel.alipay.param.isv.AlipayIsvAppAuthTokenUpdateParam;
import cn.daxpay.open.channel.alipay.param.isv.AlipayIsvAuthParam;
import cn.daxpay.open.channel.alipay.result.isv.AlipayIsvAuthUrlResult;
import cn.daxpay.open.channel.alipay.result.isv.MchAlipayIsvAuthResult;
import cn.daxpay.open.channel.alipay.service.isv.AlipayIsvAuthService;
import cn.daxpay.open.channel.alipay.service.isv.AlipayIsvChannelMerchantService;
import cn.daxpay.open.payment.common.context.PaymentContext;
import cn.daxpay.open.platform.core.annotation.PermCode;
import cn.daxpay.open.platform.core.code.CommonCode;
import cn.daxpay.open.platform.core.code.PermCodes;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

/// # 支付宝服务商代运营授权（商户移动端）
///
/// 对照商户 Web 版 [MchAlipayIsvAuthController]，路径前缀 `/app-mch/alipay/isv-auth`。
/// 商户号一律取自 [PaymentContext]，忽略请求中的 mchNo，防越权。
/// 查询结果脱敏，不返回 appAuthToken 完整值（仅掩码尾4位与是否已授权）；
/// 复用运营端 [AlipayIsvChannelMerchantService#updateAppAuthToken] 的手动更新令牌能力。
/// 移动端无回调页场景, 授权回调地址端点不提供。
@PermCode(menuCode = PermCodes.Merchant.AlipayIsvAuth.MENU)
@Validated
@Tag(name = "支付宝服务商代运营授权(商户移动端)")
@RestController
@RequestMapping("/app-mch/alipay/isv-auth")
@RequiredArgsConstructor
public class AppMchAlipayIsvAuthController {

    private final AlipayIsvAuthService alipayIsvAuthService;
    private final AlipayIsvChannelMerchantService alipayIsvChannelMerchantService;
    private final AlipayIsvChannelMerchantManager alipayIsvChannelMerchantManager;
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

    /// 校验通道商户号归属当前商户并返回实体（TenantLine 兜底之外的显式防越权）
    private AlipayIsvChannelMerchant requireOwned(String channelMchNo) {
        AlipayIsvChannelMerchant entity = alipayIsvChannelMerchantManager
                .lambdaQuery()
                .eq(AlipayIsvChannelMerchant::getChannelMchNo, channelMchNo)
                .oneOpt()
                .orElseThrow(() -> new DataNotExistException("error.payment.channel.channelMerchantNotExist"));
        if (!Objects.equals(entity.getMchNo(), requireMchNo())) {
            // 通道商户不属于当前商户（复用通用归属校验文案）
            throw new BizInfoException(CommonCode.FAIL_CODE, "error.payment.merchant.storeNoMatch");
        }
        return entity;
    }

    /// 查询单个通道商户的授权状态（脱敏, 掩码尾4位）
    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "查询单个通道商户授权状态")
    @GetMapping("/find")
    public Result<MchAlipayIsvAuthResult> findByChannelMchNo(
            @RequestParam("channelMchNo") String channelMchNo) {
        return Res.ok(toResult(requireOwned(channelMchNo)));
    }

    /// 手动粘贴更新授权令牌（先校验归属, 再复用运营端 Service）
    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "手动更新应用授权令牌")
    @PostMapping("/update-app-auth-token")
    public Result<Void> updateAppAuthToken(@RequestBody @Validated AlipayIsvAppAuthTokenUpdateParam param) {
        // 显式防越权：channelMchNo 必须归属当前商户
        requireOwned(param.getChannelMchNo());
        alipayIsvChannelMerchantService.updateAppAuthToken(param);
        return Res.ok();
    }

    /// 生成代运营授权链接（子商户管理员在浏览器确认后令牌自动回传更新）
    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "生成代运营授权链接")
    @PostMapping("/gen-auth-url")
    public Result<AlipayIsvAuthUrlResult> genAuthUrl(@RequestBody @Validated AlipayIsvAuthParam param) {
        // 显式防越权：channelMchNo 必须归属当前商户
        requireOwned(param.getChannelMchNo());
        return Res.ok(alipayIsvAuthService.genAuthUrl(param));
    }

    /// 实体转脱敏结果（不下发 appAuthToken 完整值, 仅掩码尾4位）
    private MchAlipayIsvAuthResult toResult(AlipayIsvChannelMerchant entity) {
        var result = new MchAlipayIsvAuthResult();
        result.setChannelMchNo(entity.getChannelMchNo());
        result.setProduct(entity.getProduct());
        // 实体 isvAppId 为 Long, Result 以字符串展示
        result.setIsvAppId(Objects.toString(entity.getIsvAppId(), null));
        result.setAlipayUserId(entity.getAlipayUserId());
        String token = entity.getAppAuthToken();
        result.setAuthorized(token != null && !token.isBlank());
        if (Boolean.TRUE.equals(result.getAuthorized())) {
            // 掩码仅保留尾4位
            result.setMaskedToken("****" + token.substring(token.length() - 4));
        }
        return result;
    }
}
