package org.dromara.daxpay.channel.alipay.controller.payment;

import cn.bootx.platform.core.annotation.IgnoreAuth;
import org.dromara.daxpay.channel.alipay.service.payment.notice.AlipayNoticeReceiverService;
import org.dromara.daxpay.service.service.assist.PaymentAssistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 支付宝消息通知
 * @author xxm
 * @since 2024/4/16
 */
@Validated
@IgnoreAuth
@Tag(name = "支付宝消息通知")
@RestController
@RequestMapping("/unipay/notice/{mchNo}/{AppId}")
@RequiredArgsConstructor
public class AlipayNoticeReceiverController {

    private final AlipayNoticeReceiverService aliPayNoticeReceiverService;

    private final PaymentAssistService paymentAssistService;

    @Operation(summary = "支付宝消息通知(普通商户)")
    @PostMapping("/alipay")
    public String aliPay(@PathVariable("mchNo") String mchNo, @PathVariable("AppId") String appId, HttpServletRequest request) {
        paymentAssistService.initMchAndApp(mchNo, appId);
        return aliPayNoticeReceiverService.noticeReceiver(request,false);
    }
    @Operation(summary = "支付宝消息通知(特约商户)")
    @PostMapping("/alipay/isv")
    public String aliPayIsv(@PathVariable("mchNo") String mchNo, @PathVariable("AppId") String appId, HttpServletRequest request) {
        paymentAssistService.initMchAndApp(mchNo, appId);
        return aliPayNoticeReceiverService.noticeReceiver(request,true);
    }
}
