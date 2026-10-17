package cn.daxpay.open.channel.sand.controller.callback;

import cn.daxpay.open.channel.sand.service.callback.SandPayCallbackService;
import cn.daxpay.open.channel.sand.service.callback.SandRefundCallbackService;
import cn.daxpay.open.payment.common.context.MerchantContextLoader;
import cn.daxpay.open.platform.core.annotation.IgnoreAuth;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/// # 杉德支付回调通知控制器
///
/// 杉德异步通知入口(支付/退款, 表单 bizData + sign), 不走 Sa-Token 认证(由 @IgnoreAuth 注解放行)。
/// URL 中的 channelMchNo 用于回调时组装凭证(验签需杉德公钥证书)。
/// 应答为杉德规约纯文本(`respCode=000000` 成功 / `respCode=020002` 失败)。
@Tag(name = "杉德支付回调通知控制器")
@RestController
@RequiredArgsConstructor
@RequestMapping("/unipay/callback/{mchNo}/{channelMchNo}/sand")
@IgnoreAuth
public class SandCallbackController {

    private final MerchantContextLoader merchantContextLoader;
    private final SandPayCallbackService sandPayCallbackService;
    private final SandRefundCallbackService sandRefundCallbackService;

    /// 杉德支付回调
    @Operation(summary = "杉德支付回调")
    @PostMapping("/pay")
    public String payNotify(@PathVariable("mchNo") String mchNo,
                            @PathVariable("channelMchNo") String channelMchNo,
                            @RequestParam("bizData") String bizData,
                            @RequestParam("sign") String sign) {
        merchantContextLoader.bindMchNoForCallback(mchNo);
        return sandPayCallbackService.payHandle(mchNo, channelMchNo, bizData, sign);
    }

    /// 杉德退款回调
    @Operation(summary = "杉德退款回调")
    @PostMapping("/refund")
    public String refundNotify(@PathVariable("mchNo") String mchNo,
                               @PathVariable("channelMchNo") String channelMchNo,
                               @RequestParam("bizData") String bizData,
                               @RequestParam("sign") String sign) {
        merchantContextLoader.bindMchNoForCallback(mchNo);
        return sandRefundCallbackService.refundHandle(mchNo, channelMchNo, bizData, sign);
    }
}
