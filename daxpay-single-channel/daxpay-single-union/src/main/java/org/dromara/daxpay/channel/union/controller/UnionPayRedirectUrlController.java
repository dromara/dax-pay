package org.dromara.daxpay.channel.union.controller;

import cn.bootx.platform.core.annotation.IgnoreAuth;
import org.dromara.daxpay.service.service.assist.PaymentAssistService;
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
 * 银联
 * @author xxm
 * @since 2024/9/6
 */
@IgnoreAuth
@Tag(name = "同步通知跳转控制器")
@RestController
@RequestMapping("/unipay/return/{AppId}")
@RequiredArgsConstructor
public class UnionPayRedirectUrlController {
    private final PaymentAssistService paymentAssistService;

    @Operation(summary = "银联同步跳转通知")
    @GetMapping("/union")
    public ModelAndView alipay(@PathVariable("AppId") String appId, HttpServletRequest request){
        paymentAssistService.initMchApp(appId);
        String redirect = "";
        return new ModelAndView("redirect:"+redirect);
    }
}
