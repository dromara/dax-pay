package cn.daxpay.open.channel.epaylinks.controller.callback;

import cn.daxpay.open.channel.epaylinks.service.callback.EpaylinksPayCallbackService;
import cn.daxpay.open.payment.common.context.MerchantContextLoader;
import cn.daxpay.open.platform.core.annotation.IgnoreAuth;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/// # 易票联支付回调通知控制器
///
/// 易票联异步通知入口(支付, JSON body + x-efps-sign 头签名), 不走 Sa-Token 认证(由 @IgnoreAuth 注解放行)。
/// URL 中的 channelMchNo 用于回调时组装凭证(验签需平台公钥证书)。
/// 应答为易票联规约 JSON(returnCode=0000/0001)。
///
/// 易票联退款为同步受理即终态语义, 无退款异步通知, 不提供退款回调端点。
@Tag(name = "易票联支付回调通知控制器")
@RestController
@RequiredArgsConstructor
@RequestMapping("/unipay/callback/{mchNo}/{channelMchNo}/epaylinks")
@IgnoreAuth
public class EpaylinksCallbackController {

    private final MerchantContextLoader merchantContextLoader;
    private final EpaylinksPayCallbackService epaylinksPayCallbackService;

    /// 易票联支付回调
    @Operation(summary = "易票联支付回调")
    @PostMapping(value = "/pay", produces = MediaType.APPLICATION_JSON_VALUE)
    public String payNotify(@PathVariable("mchNo") String mchNo,
                            @PathVariable("channelMchNo") String channelMchNo,
                            @RequestHeader(value = "x-efps-sign", required = false) String sign,
                            @RequestBody String body) {
        merchantContextLoader.bindMchNoForCallback(mchNo);
        return epaylinksPayCallbackService.payHandle(mchNo, channelMchNo, body, sign);
    }
}
