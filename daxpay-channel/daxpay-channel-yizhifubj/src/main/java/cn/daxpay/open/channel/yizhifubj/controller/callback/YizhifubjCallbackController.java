package cn.daxpay.open.channel.yizhifubj.controller.callback;

import cn.daxpay.open.channel.yizhifubj.service.callback.YizhifubjPayCallbackService;
import cn.daxpay.open.channel.yizhifubj.service.callback.YizhifubjRefundCallbackService;
import cn.daxpay.open.payment.common.context.MerchantContextLoader;
import cn.daxpay.open.platform.core.annotation.IgnoreAuth;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/// # 一直付(北京)支付回调通知控制器
///
/// 一直付异步通知入口(支付/退款, body 密文 + encryptKey 请求头), 不走 Sa-Token 认证(由 @IgnoreAuth 注解放行)。
/// URL 中的 channelMchNo 用于回调时组装凭证(解密需商户 pfx 证书)。
/// 应答为明文串 SUCCESS / Fail(按一直付规约, 无需加密应答)。
@Tag(name = "一直付支付回调通知控制器")
@RestController
@RequiredArgsConstructor
@RequestMapping("/unipay/callback/{mchNo}/{channelMchNo}/yizhifubj")
@IgnoreAuth
public class YizhifubjCallbackController {

    private final MerchantContextLoader merchantContextLoader;
    private final YizhifubjPayCallbackService yizhifubjPayCallbackService;
    private final YizhifubjRefundCallbackService yizhifubjRefundCallbackService;

    /// 一直付支付回调
    @Operation(summary = "一直付支付回调")
    @PostMapping("/pay")
    public String payNotify(@PathVariable("mchNo") String mchNo,
                            @PathVariable("channelMchNo") String channelMchNo,
                            @RequestHeader("encryptKey") String encryptKey,
                            @RequestBody String body) {
        merchantContextLoader.bindMchNoForCallback(mchNo);
        return yizhifubjPayCallbackService.payHandle(mchNo, channelMchNo, body, encryptKey);
    }

    /// 一直付退款回调
    @Operation(summary = "一直付退款回调")
    @PostMapping("/refund")
    public String refundNotify(@PathVariable("mchNo") String mchNo,
                               @PathVariable("channelMchNo") String channelMchNo,
                               @RequestHeader("encryptKey") String encryptKey,
                               @RequestBody String body) {
        merchantContextLoader.bindMchNoForCallback(mchNo);
        return yizhifubjRefundCallbackService.refundHandle(mchNo, channelMchNo, body, encryptKey);
    }
}
