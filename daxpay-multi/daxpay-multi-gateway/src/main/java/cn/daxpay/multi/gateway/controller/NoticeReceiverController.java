package cn.daxpay.multi.gateway.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 三方支付回调消息通知接收器
 * @author xxm
 * @since 2024/6/4
 */
@Tag(name = "三方支付回调通知处理器")
@RestController
@RequestMapping("/notice/{mchNo}/{AppId}")
@RequiredArgsConstructor
public class NoticeReceiverController {


    @Operation(summary = "支付宝消息通知")
    @PostMapping("/alipay")
    public String aliPayNotice(HttpServletRequest request) {
        return "";
    }

    @Operation(summary = "微信消息通知")
    @PostMapping("/wechat")
    public Map<String, Objects> wechatPayNotice(HttpServletRequest request) {
        return new HashMap<>();
    }
}
