package cn.daxpay.multi.gateway.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 支付通道网关消息通知接收器
 * @author xxm
 * @since 2024/6/4
 */
@Tag(name = "支付通道网关消息通知")
@RestController
@RequestMapping("/unipay/notice/{mchNo}/{AppId}")
@RequiredArgsConstructor
public class NoticeReceiverController {

    @Operation(summary = "支付宝消息通知")
    @PostMapping("/alipay")
    public String aliPayNotice(@PathVariable("mchNo") String mchNo, @PathVariable("AppId") String appId, HttpServletRequest request) {
        return "";
    }

    @Operation(summary = "微信消息通知")
    @PostMapping("/wechat")
    public Map<String, Objects> wechatPayNotice(@PathVariable("mchNo") String mchNo, @PathVariable("AppId") String appId, HttpServletRequest request) {
        return new HashMap<>();
    }
}
