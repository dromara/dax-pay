package cn.daxpay.multi.gateway.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 三方支付回调处理器
 * @author xxm
 * @since 2024/6/4
 */
@Slf4j
@Tag(name = "三方支付回调处理器")
@RestController
@RequestMapping("/callback/{mchNo}/{AppId}")
@AllArgsConstructor
public class CallbackReceiverController {

    @SneakyThrows
    @Operation(summary = "支付宝回调")
    @PostMapping("/alipay")
    public String aliPayNotify(HttpServletRequest request) {
        return "";
    }

    @SneakyThrows
    @Operation(summary = "微信支付回调")
    @PostMapping("/wechat")
    public String wechatPayNotify(HttpServletRequest request) {
        return "";
    }

    @SneakyThrows
    @Operation(summary = "云闪付支付回调")
    @PostMapping("/union")
    public String unionPayNotify(HttpServletRequest request) {
        return "";
    }
}
