package cn.daxpay.open.channel.alipay.controller.appmch;

import cn.daxpay.open.channel.alipay.result.direct.AlipayDirectAppResult;
import cn.daxpay.open.channel.alipay.service.direct.AlipayDirectAppService;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

/// # 支付宝直连应用管理(小程序商户端镜像)
///
/// 对照 admin 版 [cn.daxpay.open.channel.alipay.controller.direct.AlipayDirectAppController],
/// 仅镜像转账配置页所需的只读应用列表端点。
/// 商户号一律取自 [PaymentContext], 忽略客户端传入, 防越权。
@PermCode(menuCode = PermCodes.Channel.Merchant.MENU)
@Validated
@Tag(name = "小程序商户端-支付宝直连应用管理")
@RestController
@RequestMapping("/app-mch/alipay/mch-app")
@RequiredArgsConstructor
public class AppMchAlipayDirectAppController {

    private final AlipayDirectAppService alipayDirectAppService;
    private final PaymentContext paymentContext;

    /// 当前登录商户号(上下文必有;缺则视为会话异常)
    private String requireMchNo() {
        String mchNo = paymentContext.getMchNo();
        if (Objects.isNull(mchNo) || mchNo.isBlank()) {
            // 商户上下文缺失
            throw new BizInfoException(CommonCode.FAIL_CODE, "pay.error.assist.mchContextMissing");
        }
        return mchNo;
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "按通道商户号查询当前商户的支付宝应用列表")
    @GetMapping("/list-by-channel-mch-no")
    public Result<List<AlipayDirectAppResult>> listByChannelMchNo(
            @NotBlank(message = "{validation.field.channelMerchantNo.notBlank}") String channelMchNo) {
        return Res.ok(alipayDirectAppService.listByMchNoAndChannelMchNo(requireMchNo(), channelMchNo));
    }
}
