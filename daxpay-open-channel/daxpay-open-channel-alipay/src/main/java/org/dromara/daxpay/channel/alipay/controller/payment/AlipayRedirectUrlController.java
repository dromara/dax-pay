package org.dromara.daxpay.channel.alipay.controller.payment;

import cn.bootx.platform.core.annotation.IgnoreAuth;
import org.dromara.daxpay.channel.alipay.service.payment.redirect.AlipayRedirectUrlService;
import org.dromara.daxpay.service.service.assist.PaymentAssistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * 支付宝同步跳转通知控制器
 * @author xxm
 * @since 2024/6/4
 */
@Validated
@IgnoreAuth
@Tag(name = "支付宝同步通知")
@RestController
@RequestMapping("/unipay/return/{mchNo}/{AppId}")
@RequiredArgsConstructor
public class AlipayRedirectUrlController {
    private final AlipayRedirectUrlService redirectUrlService;
    private final PaymentAssistService paymentAssistService;

    @Operation(summary = "支付宝同步跳转通知")
    @GetMapping("/alipay")
    public ModelAndView alipay(@PathVariable("mchNo") String mchNo, @PathVariable("AppId") String appId, HttpServletRequest request){
        paymentAssistService.initMchAndApp(mchNo, appId);
        String redirect = redirectUrlService.redirect(request,false);
        return new ModelAndView("redirect:"+redirect);
    }
    @Operation(summary = "支付宝同步跳转通知")
    @GetMapping("/alipay/isv")
    public ModelAndView alipayIsv(@PathVariable("mchNo") String mchNo, @PathVariable("AppId") String appId, HttpServletRequest request){
        paymentAssistService.initMchAndApp(mchNo, appId);
        String redirect = redirectUrlService.redirect(request, true);
        return new ModelAndView("redirect:"+redirect);
    }
}
