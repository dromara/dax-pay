package cn.daxpay.open.channel.ysep.controller.callback;

import cn.daxpay.open.channel.ysep.service.callback.YsepPayCallbackService;
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

/// # 银盛支付回调通知控制器
///
/// 银盛异步通知入口(支付, form-urlencoded 报文, 两产品共用), 不走 Sa-Token 认证(由 @IgnoreAuth 注解放行)。
/// 验签证书按通道商户号从 ysep_key_config 读取; path 带 channelMchNo 定位密钥,
/// 子应用按报文形态自动分流开放平台(ysep_pay)/e企通(yseqt_pay)验签协议。
/// 银盛两产品退款无独立异步通知, 仅支付回调端点。
/// 应答串: 成功 `success` / 失败 `fail`(原文应答, 失败重发最多 10 次)。
@Tag(name = "银盛支付回调通知控制器")
@RestController
@RequestMapping("/unipay/callback/{mchNo}/{channelMchNo}/ysep")
@RequiredArgsConstructor
@IgnoreAuth
public class YsepCallbackController {

    private final MerchantContextLoader merchantContextLoader;
    private final YsepPayCallbackService ysepPayCallbackService;

    /// 银盛支付回调(两产品共用)
    @Operation(summary = "银盛支付回调")
    @PostMapping("/pay")
    public String payNotify(@PathVariable("mchNo") String mchNo,
                            @PathVariable("channelMchNo") String channelMchNo,
                            HttpServletRequest request) {
        merchantContextLoader.bindMchNoForCallback(mchNo);
        return ysepPayCallbackService.payHandle(channelMchNo, request);
    }
}
