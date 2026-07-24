package cn.daxpay.open.channel.alipay.controller.isv;

import cn.daxpay.open.channel.alipay.dao.isv.AlipayIsvChannelMerchantManager;
import cn.daxpay.open.channel.alipay.entity.isv.AlipayIsvChannelMerchant;
import cn.daxpay.open.channel.alipay.param.isv.AlipayIsvAuthParam;
import cn.daxpay.open.channel.alipay.result.isv.AlipayIsvAuthUrlResult;
import cn.daxpay.open.channel.alipay.result.isv.MchAlipayIsvAuthResult;
import cn.daxpay.open.channel.alipay.service.isv.AlipayIsvAuthService;
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
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

/// # 支付宝服务商代运营授权（商户端）
///
/// 对照运营端 [AlipayIsvChannelMerchantController]，路径前缀 `/mch/alipay/isv-auth`。
/// 商户号一律取自 [PaymentContext]，忽略请求中的 mchNo，防越权。
/// 复用 [AlipayIsvAuthService] 的授权链接生成能力；查询结果脱敏，不返回 appAuthToken。
/// 供通道商户详情页「授权操作」抽屉（[findByChannelMchNo]）与列表视图（[list]）共用。
@PermCode(menuCode = PermCodes.Merchant.AlipayIsvAuth.MENU)
@Validated
@Tag(name = "支付宝服务商代运营授权(商户端)")
@RestController
@RequestMapping("/mch/alipay/isv-auth")
@RequiredArgsConstructor
public class MchAlipayIsvAuthController {

    private final AlipayIsvAuthService alipayIsvAuthService;
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

    /// 列表：当前商户名下所有支付宝服务商通道商户绑定（脱敏，仅返回是否已授权）
    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "当前商户的支付宝服务商通道商户列表")
    @GetMapping("/list")
    public Result<List<MchAlipayIsvAuthResult>> list() {
        String mchNo = requireMchNo();
        List<AlipayIsvChannelMerchant> entities = alipayIsvChannelMerchantManager
                .lambdaQuery()
                .eq(AlipayIsvChannelMerchant::getMchNo, mchNo)
                .orderByAsc(AlipayIsvChannelMerchant::getCreateTime)
                .list();
        List<MchAlipayIsvAuthResult> list = entities.stream()
                .map(this::toResult)
                .toList();
        return Res.ok(list);
    }

    /// 查询单个通道商户的授权状态（供通道商户详情页抽屉展示，脱敏）
    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "查询单个通道商户授权状态")
    @GetMapping("/find")
    public Result<MchAlipayIsvAuthResult> findByChannelMchNo(
            @RequestParam("channelMchNo") String channelMchNo) {
        return Res.ok(toResult(requireOwned(channelMchNo)));
    }

    /// 生成代运营授权链接（先校验归属当前商户，再复用核心 Service）
    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "生成代运营授权链接")
    @PostMapping("/gen-auth-url")
    public Result<AlipayIsvAuthUrlResult> genAuthUrl(@RequestBody @Validated AlipayIsvAuthParam param) {
        // 显式防越权：channelMchNo 必须归属当前商户
        requireOwned(param.getChannelMchNo());
        return Res.ok(alipayIsvAuthService.genAuthUrl(param));
    }

    /// 获取代运营授权回调地址（用于支付宝开放平台配置参考）
    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "获取代运营授权回调地址")
    @GetMapping("/auth-callback-url")
    public Result<String> getAuthCallbackUrl() {
        return Res.ok(alipayIsvAuthService.getAuthCallbackUrl());
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

    /// 实体转脱敏结果（不下发 appAuthToken）
    private MchAlipayIsvAuthResult toResult(AlipayIsvChannelMerchant entity) {
        var result = new MchAlipayIsvAuthResult();
        result.setChannelMchNo(entity.getChannelMchNo());
        result.setProduct(entity.getProduct());
        result.setAlipayUserId(entity.getAlipayUserId());
        result.setAuthorized(entity.getAppAuthToken() != null && !entity.getAppAuthToken().isBlank());
        return result;
    }
}
