package cn.daxpay.open.channel.yisheng.controller.callback;

import cn.daxpay.open.channel.yisheng.service.callback.YishengPayCallbackService;
import cn.daxpay.open.payment.common.context.MerchantContextLoader;
import cn.daxpay.open.platform.core.annotation.IgnoreAuth;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/// # 易生支付回调通知控制器
///
/// 易生异步通知入口(支付, form 提交 biz_content + sign), 不走 Sa-Token 认证(由 @IgnoreAuth 注解放行)。
/// URL 中的 channelMchNo 用于回调时组装凭证(验签需易生平台公钥)。
/// 应答为纯文本 SUCCESS / FAIL(对齐参考实现)。
///
/// 易生退款为同步终态无退款异步通知, 仅支付回调入口。
@Slf4j
@Tag(name = "易生支付回调通知控制器")
@RestController
@RequiredArgsConstructor
@RequestMapping("/unipay/callback/{mchNo}/{channelMchNo}/yisheng")
@IgnoreAuth
public class YishengCallbackController {

    private final MerchantContextLoader merchantContextLoader;
    private final YishengPayCallbackService yishengPayCallbackService;

    /// 易生支付回调
    @Operation(summary = "易生支付回调")
    @PostMapping("/pay")
    public String payNotify(@PathVariable("mchNo") String mchNo,
                            @PathVariable("channelMchNo") String channelMchNo,
                            HttpServletRequest request) {
        merchantContextLoader.bindMchNoForCallback(mchNo);
        // 易生回调为 form 提交: biz_content(业务 JSON 原文) + sign(对原文的平台签名)
        String bizContent = request.getParameter("biz_content");
        String sign = request.getParameter("sign");
        return yishengPayCallbackService.payHandle(mchNo, channelMchNo, bizContent, sign);
    }
}
