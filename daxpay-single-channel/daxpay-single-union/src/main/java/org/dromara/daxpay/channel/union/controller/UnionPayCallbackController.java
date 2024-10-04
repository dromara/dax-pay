package org.dromara.daxpay.channel.union.controller;

import cn.bootx.platform.core.annotation.IgnoreAuth;
import org.dromara.daxpay.service.service.assist.PaymentAssistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 银联回调通知
 * @author xxm
 * @since 2024/9/6
 */
@IgnoreAuth
@Tag(name = "银联回调通知")
@RestController
@RequestMapping("/unipay/callback/{AppId}")
@RequiredArgsConstructor
public class UnionPayCallbackController {


    private final PaymentAssistService paymentAssistService;

    @Operation(summary = "银联回调")
    @PostMapping("/union")
    public String UnionPayNotify(@PathVariable("AppId") String appId, HttpServletRequest request) {
        paymentAssistService.initMchApp(appId);
        return "";
    }
}
