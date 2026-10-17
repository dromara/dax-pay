package cn.daxpay.open.channel.lakala.controller.callback;

import cn.daxpay.open.channel.lakala.service.callback.LakalaMossPayCallbackService;
import cn.daxpay.open.channel.lakala.service.callback.LakalaMossRefundCallbackService;
import cn.daxpay.open.payment.common.context.MerchantContextLoader;
import cn.daxpay.open.platform.core.annotation.IgnoreAuth;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/// # 拉卡拉 MOSS 支付回调通知控制器
///
/// MOSS(开放平台产品线)异步通知入口(支付/退款), 不走 Sa-Token 认证(由 @IgnoreAuth 注解放行)。
/// 验签只需全局 MOSS 密钥配置; path 仍带 channelMchNo 统一约定, 凭 order_no 反查订单。
///
/// 应答为签名 JSON(`{head:{code:"000000"}, sign}`), 与服务商模式返回裸 success 不同,
/// 由 [LakalaMossPayCallbackService] 委托子应用构建。
@Tag(name = "拉卡拉MOSS支付回调通知控制器")
@RestController
@RequestMapping(value = "/unipay/callback/{mchNo}/{channelMchNo}/lakala/moss",
        produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@IgnoreAuth
public class LakalaMossCallbackController {

    private final MerchantContextLoader merchantContextLoader;
    private final LakalaMossPayCallbackService lakalaMossPayCallbackService;
    private final LakalaMossRefundCallbackService lakalaMossRefundCallbackService;

    /// 拉卡拉MOSS支付回调
    @Operation(summary = "拉卡拉MOSS支付回调")
    @PostMapping("/pay")
    public String payNotify(@PathVariable("mchNo") String mchNo,
                            @PathVariable("channelMchNo") String channelMchNo,
                            HttpServletRequest request) {
        merchantContextLoader.bindMchNoForCallback(mchNo);
        return lakalaMossPayCallbackService.payHandle(channelMchNo, request);
    }

    /// 拉卡拉MOSS退款回调
    @Operation(summary = "拉卡拉MOSS退款回调")
    @PostMapping("/refund")
    public String refundNotify(@PathVariable("mchNo") String mchNo,
                               @PathVariable("channelMchNo") String channelMchNo,
                               HttpServletRequest request) {
        merchantContextLoader.bindMchNoForCallback(mchNo);
        return lakalaMossRefundCallbackService.refundHandle(channelMchNo, request);
    }
}
