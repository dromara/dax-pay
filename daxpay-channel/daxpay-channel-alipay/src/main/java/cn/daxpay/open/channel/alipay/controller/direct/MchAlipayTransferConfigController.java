package cn.daxpay.open.channel.alipay.controller.direct;

import cn.daxpay.open.channel.alipay.param.direct.AlipayTransferConfigParam;
import cn.daxpay.open.channel.alipay.result.direct.AlipayTransferConfigResult;
import cn.daxpay.open.channel.alipay.service.direct.AlipayTransferConfigService;
import cn.daxpay.open.payment.common.context.PaymentContext;
import cn.daxpay.open.platform.core.code.CommonCode;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/// # 支付宝转账配置管理(商户端)
///
/// 对照运营端 [AlipayTransferConfigController],路径前缀 `/mch/alipay/transfer-config`。
/// 商户号一律取自 [PaymentContext],忽略请求中的 mchNo,防越权。
///
@Validated
@Tag(name = "支付宝转账配置管理(商户端)")
@RestController
@RequestMapping("/mch/alipay/transfer-config")
@RequiredArgsConstructor
public class MchAlipayTransferConfigController {

    private final AlipayTransferConfigService alipayTransferConfigService;
    private final PaymentContext paymentContext;

    /// 当前登录商户号(上下文必有;缺则视为会话异常)
    private String requireMchNo() {
        String mchNo = paymentContext.getMchNo();
        if (mchNo == null || mchNo.isBlank()) {
            // 商户上下文缺失
            throw new BizInfoException(CommonCode.FAIL_CODE, "pay.error.assist.mchContextMissing");
        }
        return mchNo;
    }

    @Operation(summary = "查询通道商户的转账配置")
    @GetMapping("/find-by-channel-mch-no")
    public Result<AlipayTransferConfigResult> findByChannelMchNo(
            @NotBlank(message = "{validation.field.channelMerchantNo.notBlank}") String channelMchNo) {
        return Res.ok(alipayTransferConfigService.findByChannelMchNo(requireMchNo(), channelMchNo));
    }

    @Operation(summary = "保存或更新转账配置(一对一)")
    @PostMapping("/save")
    public Result<Void> save(@RequestBody @Validated AlipayTransferConfigParam param) {
        // 商户号强制取自上下文, 防越权
        param.setMchNo(requireMchNo());
        alipayTransferConfigService.saveOrUpdate(param);
        return Res.ok();
    }
}
