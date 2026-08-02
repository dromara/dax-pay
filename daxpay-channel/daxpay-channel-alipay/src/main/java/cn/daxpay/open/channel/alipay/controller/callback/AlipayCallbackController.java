package cn.daxpay.open.channel.alipay.controller.callback;

import cn.daxpay.open.channel.alipay.service.callback.AlipayCallbackService;
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

/// # 支付宝支付回调通知控制器
///
/// 支付宝异步通知入口(支付/退款共用同一端点, 无 /pay 后缀, 见 AlipayPayService.buildNotifyUrl),
/// 不走 Sa-Token 认证(由 @IgnoreAuth 注解放行)。
/// 路径 `{mchNo}/{channelMchNo}`: mchNo 装载租户上下文; channelMchNo 供直连凭证组装(亦可凭 out_trade_no 反查)。
@Tag(name = "支付宝支付回调通知控制器")
@RestController
@RequestMapping("/unipay/callback/{mchNo}/{channelMchNo}/alipay")
@RequiredArgsConstructor
@IgnoreAuth
public class AlipayCallbackController {

    private final MerchantContextLoader merchantContextLoader;
    private final AlipayCallbackService alipayCallbackService;

    /// 支付宝回调(支付/退款统一入口, 由服务层按表单参数区分)
    @Operation(summary = "支付宝支付/退款回调")
    @PostMapping
    public String notify(@PathVariable String mchNo,
                         @PathVariable String channelMchNo,
                         HttpServletRequest request) {
        // 显式装载商户租户上下文(不校验启用, 保证禁用商户历史单可回调)
        merchantContextLoader.bindMchNoForCallback(mchNo);
        return alipayCallbackService.handle(channelMchNo, request);
    }
}
