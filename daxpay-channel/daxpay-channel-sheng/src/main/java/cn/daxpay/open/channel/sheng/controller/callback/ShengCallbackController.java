package cn.daxpay.open.channel.sheng.controller.callback;

import cn.daxpay.open.channel.sheng.service.callback.ShengPayCallbackService;
import cn.daxpay.open.channel.sheng.service.callback.ShengRefundCallbackService;
import cn.daxpay.open.payment.common.context.MerchantContextLoader;
import cn.daxpay.open.platform.core.annotation.IgnoreAuth;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/// # 盛付通支付回调通知控制器
///
/// 盛付通异步通知入口(支付/退款, JSON 报文), 不走 Sa-Token 认证(由 @IgnoreAuth 注解放行)。
/// 验签公钥按通道商户号从 sheng_key_config 读取; path 带 channelMchNo 定位密钥,
/// 凭 out_trade_no 反查 PayTrade。
@Tag(name = "盛付通支付回调通知控制器")
@RestController
@RequestMapping("/unipay/callback/{mchNo}/{channelMchNo}/sheng")
@RequiredArgsConstructor
@IgnoreAuth
public class ShengCallbackController {

    private final MerchantContextLoader merchantContextLoader;
    private final ShengPayCallbackService shengPayCallbackService;
    private final ShengRefundCallbackService shengRefundCallbackService;

    /// 盛付通支付回调
    @Operation(summary = "盛付通支付回调")
    @PostMapping("/pay")
    public String payNotify(@PathVariable("mchNo") String mchNo,
                            @PathVariable("channelMchNo") String channelMchNo,
                            HttpServletRequest request) {
        merchantContextLoader.bindMchNoForCallback(mchNo);
        return shengPayCallbackService.payHandle(channelMchNo, request);
    }

    /// 盛付通退款回调
    @Operation(summary = "盛付通退款回调")
    @PostMapping("/refund")
    public String refundNotify(@PathVariable("mchNo") String mchNo,
                               @PathVariable("channelMchNo") String channelMchNo,
                               HttpServletRequest request) {
        merchantContextLoader.bindMchNoForCallback(mchNo);
        return shengRefundCallbackService.refundHandle(channelMchNo, request);
    }
}
