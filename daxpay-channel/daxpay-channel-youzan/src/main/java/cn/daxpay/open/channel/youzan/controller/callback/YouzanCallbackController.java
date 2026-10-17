package cn.daxpay.open.channel.youzan.controller.callback;

import cn.daxpay.open.channel.youzan.service.callback.YouzanPayCallbackService;
import cn.daxpay.open.channel.youzan.service.callback.YouzanRefundCallbackService;
import cn.daxpay.open.payment.common.context.MerchantContextLoader;
import cn.daxpay.open.platform.core.annotation.IgnoreAuth;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/// # 有赞支付回调通知控制器
///
/// 有赞(高汇通)异步通知入口(支付/退款, JSON body + 请求头 youzan-sign/youzan-timestamp/youzan-noncestr 验签材料),
/// 不走 Sa-Token 认证(由 @IgnoreAuth 注解放行)。
/// URL 中的 channelMchNo 用于回调时组装凭证(验签需有赞平台公钥)。
/// 应答为 `{"success":true}` JSON 规约串(对齐参考实现)。
@Slf4j
@Tag(name = "有赞支付回调通知控制器")
@RestController
@RequiredArgsConstructor
@RequestMapping("/unipay/callback/{mchNo}/{channelMchNo}/youzan")
@IgnoreAuth
public class YouzanCallbackController {

    private final MerchantContextLoader merchantContextLoader;
    private final YouzanPayCallbackService youzanPayCallbackService;
    private final YouzanRefundCallbackService youzanRefundCallbackService;

    /// 有赞支付回调
    @Operation(summary = "有赞支付回调")
    @PostMapping("/pay")
    public String payNotify(@PathVariable("mchNo") String mchNo,
                            @PathVariable("channelMchNo") String channelMchNo,
                            @RequestHeader(value = "youzan-sign", required = false) String sign,
                            @RequestHeader(value = "youzan-timestamp", required = false) String timestamp,
                            @RequestHeader(value = "youzan-noncestr", required = false) String noncestr,
                            @RequestBody String body) {
        merchantContextLoader.bindMchNoForCallback(mchNo);
        return youzanPayCallbackService.payHandle(mchNo, channelMchNo, body, sign, timestamp, noncestr);
    }

    /// 有赞退款回调
    @Operation(summary = "有赞退款回调")
    @PostMapping("/refund")
    public String refundNotify(@PathVariable("mchNo") String mchNo,
                               @PathVariable("channelMchNo") String channelMchNo,
                               @RequestHeader(value = "youzan-sign", required = false) String sign,
                               @RequestHeader(value = "youzan-timestamp", required = false) String timestamp,
                               @RequestHeader(value = "youzan-noncestr", required = false) String noncestr,
                               @RequestBody String body) {
        merchantContextLoader.bindMchNoForCallback(mchNo);
        return youzanRefundCallbackService.refundHandle(mchNo, channelMchNo, body, sign, timestamp, noncestr);
    }
}
