package cn.daxpay.open.channel.alipay.controller.direct;

import cn.daxpay.open.channel.alipay.result.direct.AlipayDirectChannelMerchantResult;
import cn.daxpay.open.channel.alipay.service.direct.AlipayDirectChannelMerchantService;
import cn.daxpay.open.payment.common.context.PaymentContext;
import cn.daxpay.open.payment.merchant.dao.channel.ChannelMerchantManager;
import cn.daxpay.open.payment.merchant.entity.channel.ChannelMerchant;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

/// # 支付宝直连通道商户配置（商户端）
///
/// 对照运营端 [AlipayDirectChannelMerchantController]，路径前缀 `/mch/alipay/direct-channel-merchant`。
/// 提供支付宝直连通道商户的基础信息查看（含支付宝商家识别码 alipayUserId），商户号一律取自 [PaymentContext]，防越权。
@PermCode(menuCode = PermCodes.Channel.Merchant.MENU)
@Validated
@Tag(name = "支付宝直连通道商户配置(商户端)")
@RestController
@RequestMapping("/mch/alipay/direct-channel-merchant")
@RequiredArgsConstructor
public class MchAlipayDirectChannelMerchantController {

    private final AlipayDirectChannelMerchantService alipayDirectChannelMerchantService;
    private final ChannelMerchantManager channelMerchantManager;
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

    /// 校验通道商户属于当前商户（防越权）
    private void assertChannelMchOwned(String channelMchNo) {
        ChannelMerchant channelMerchant = channelMerchantManager.findByChannelMchNo(channelMchNo)
                // 支付宝: 通道商户不存在或商户号不匹配
                .orElseThrow(() -> new BizInfoException(CommonCode.FAIL_CODE, "error.channel.alipay.mchAppNotFound"));
        if (!Objects.equals(channelMerchant.getMchNo(), this.requireMchNo())) {
            // 支付宝: 通道商户与商户号不匹配
            throw new BizInfoException(CommonCode.FAIL_CODE, "error.channel.alipay.mchAppNotFound");
        }
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "根据通道商户号查询支付宝直连通道商户配置")
    @GetMapping("/find-by-channel-mch-no")
    public Result<AlipayDirectChannelMerchantResult> findByChannelMchNo(
            @NotBlank(message = "{validation.field.channelMerchantNo.notBlank}") String channelMchNo) {
        this.assertChannelMchOwned(channelMchNo);
        return Res.ok(alipayDirectChannelMerchantService.findByChannelMchNo(channelMchNo));
    }
}
