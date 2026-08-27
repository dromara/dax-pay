package cn.daxpay.open.channel.alipay.controller.direct;

import cn.daxpay.open.channel.alipay.param.direct.AlipayDirectAppCapabilityBatchParam;
import cn.daxpay.open.channel.alipay.result.direct.AlipayDirectAppCapabilityResult;
import cn.daxpay.open.channel.alipay.result.direct.AlipayDirectCapabilityOption;
import cn.daxpay.open.channel.alipay.service.direct.AlipayDirectAppCapabilityService;
import cn.daxpay.open.payment.common.context.PaymentContext;
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

import java.util.List;

/// # 支付宝直连商户应用支付能力关联管理（商户端）
///
/// 对照运营端 [AlipayDirectAppCapabilityController]，路径前缀 `/mch/alipay/direct-app/capability`。
/// 商户号一律取自 [PaymentContext]，忽略请求中的 mchNo，防越权。
@PermCode(menuCode = PermCodes.Channel.App.MENU)
@Validated
@Tag(name = "支付宝直连商户应用支付能力关联管理(商户端)")
@RestController
@RequestMapping("/mch/alipay/direct-app/capability")
@RequiredArgsConstructor
public class MchAlipayDirectAppCapabilityController {

    private final AlipayDirectAppCapabilityService alipayDirectAppCapabilityService;
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
    @Operation(summary = "查询通道商户的能力应用关联列表")
    @GetMapping("/list-by-channel-mch-no")
    public Result<List<AlipayDirectAppCapabilityResult>> listByChannelMchNo(
            @NotBlank(message = "{validation.field.channelMerchantNo.notBlank}") String channelMchNo) {
        return Res.ok(alipayDirectAppCapabilityService.listByChannelMchNo(requireMchNo(), channelMchNo));
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "全量保存能力应用关联")
    @PostMapping("/save-batch")
    public Result<Void> saveBatch(@RequestBody @Validated AlipayDirectAppCapabilityBatchParam param) {
        // 强制当前商户号，忽略客户端传入（防越权）
        param.setMchNo(requireMchNo());
        alipayDirectAppCapabilityService.saveBatch(param);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "查询支付宝直连支持的支付能力候选")
    @GetMapping("/list-supported-capabilities")
    public Result<List<AlipayDirectCapabilityOption>> listSupportedCapabilities() {
        return Res.ok(alipayDirectAppCapabilityService.listSupportedCapabilities());
    }
}
