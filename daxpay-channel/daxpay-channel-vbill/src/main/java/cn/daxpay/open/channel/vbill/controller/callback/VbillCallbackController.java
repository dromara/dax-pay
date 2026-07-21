package cn.daxpay.open.channel.vbill.controller.callback;

import cn.daxpay.open.channel.vbill.service.callback.VbillPayCallbackService;
import cn.daxpay.open.channel.vbill.service.callback.VbillRefundCallbackService;
import cn.daxpay.open.payment.common.context.MerchantContextLoader;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/// # 随行付支付回调通知控制器
///
/// 随行付(天阙科技)异步通知入口(支付), 不走 Sa-Token 认证(由安全配置放行 `/unipay/callback/**`)。
/// 验签只需全局服务商公钥; path 仍带 channelMchNo 统一约定, 凭 ordNo 反查 PayTrade。
@Tag(name = "随行付支付回调通知控制器")
@RestController
@RequestMapping("/unipay/callback/{mchNo}/{channelMchNo}/vbill")
@RequiredArgsConstructor
public class VbillCallbackController {

    private final MerchantContextLoader merchantContextLoader;
    private final VbillPayCallbackService vbillPayCallbackService;
    private final VbillRefundCallbackService vbillRefundCallbackService;

    /// 随行付支付回调
    ///
    /// 响应格式: `{"code":"success","msg":"成功"}` 表示已收到, 其他视为失败(随行付会重试)
    @Operation(summary = "随行付支付回调")
    @PostMapping("/pay")
    public Map<String, String> payNotify(@PathVariable("mchNo") String mchNo,
                                         @PathVariable("channelMchNo") String channelMchNo,
                                         HttpServletRequest request) {
        merchantContextLoader.bindMchNoForCallback(mchNo);
        return vbillPayCallbackService.payHandle(channelMchNo, request);
    }

    /// 随行付退款回调
    @Operation(summary = "随行付退款回调")
    @PostMapping("/refund")
    public Map<String, String> refundNotify(@PathVariable("mchNo") String mchNo,
                                            @PathVariable("channelMchNo") String channelMchNo,
                                            HttpServletRequest request) {
        merchantContextLoader.bindMchNoForCallback(mchNo);
        return vbillRefundCallbackService.refundHandle(channelMchNo, request);
    }
}
