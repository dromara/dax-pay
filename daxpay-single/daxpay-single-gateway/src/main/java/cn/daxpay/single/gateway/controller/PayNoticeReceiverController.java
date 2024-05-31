package cn.daxpay.single.gateway.controller;

import cn.bootx.platform.common.core.annotation.IgnoreAuth;
import cn.daxpay.single.service.core.channel.alipay.service.AliPayNoticeReceiverService;
import com.ijpay.alipay.AliPayApi;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 支付通道消息通知
 * @author xxm
 * @since 2024/4/16
 */
@IgnoreAuth
@Tag(name = "执法通道网关消息通知")
@RestController
@RequestMapping("/callback/notice")
@RequiredArgsConstructor
public class PayNoticeReceiverController {

    private final AliPayNoticeReceiverService aliPayNoticeReceiverService;

    @Operation(summary = "支付宝消息通知")
    @PostMapping("/alipay")
    public String aliPayNotice(HttpServletRequest request) {
        Map<String, String> map = AliPayApi.toMap(request);
        return aliPayNoticeReceiverService.noticeReceiver(map);
    }

    @Operation(summary = "微信消息通知")
    @PostMapping("/wechat")
    public Map<String, Objects> wechatPayNotice(HttpServletRequest request) {
        return new HashMap<>();
    }
}
