package cn.daxpay.multi.channel.alipay.controller;

import cn.bootx.platform.core.annotation.IgnoreAuth;
import cn.daxpay.multi.channel.alipay.service.redirect.AliPayRedirectUrlService;
import cn.daxpay.multi.service.service.assist.PaymentAssistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * 同步跳转通知控制器
 * @author xxm
 * @since 2024/6/4
 */
@IgnoreAuth
@Tag(name = "同步通知跳转控制器")
@RestController
@RequestMapping("/unipay/return/{mchNo}/{AppId}")
@RequiredArgsConstructor
public class AliPayRedirectUrlController {
    private final AliPayRedirectUrlService redirectUrlService;
    private final PaymentAssistService paymentAssistService;

    @Operation(summary = "支付宝同步跳转通知")
    @GetMapping("/alipay")
    public ModelAndView alipay(@PathVariable("mchNo") String mchNo, @PathVariable("AppId") String appId, HttpServletRequest request){
        paymentAssistService.initMchAndApp(mchNo, appId);
        String redirect = redirectUrlService.redirect(request);
        return new ModelAndView("redirect:"+redirect);
    }
}
