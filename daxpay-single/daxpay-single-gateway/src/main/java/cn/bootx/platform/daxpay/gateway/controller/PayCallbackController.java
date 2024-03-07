package cn.bootx.platform.daxpay.gateway.controller;

import cn.bootx.platform.common.core.annotation.IgnoreAuth;
import cn.bootx.platform.daxpay.service.core.channel.alipay.service.AliPayCallbackService;
import cn.bootx.platform.daxpay.service.core.channel.union.service.UnionPayCallbackService;
import cn.bootx.platform.daxpay.service.core.channel.wechat.service.WeChatPayCallbackService;
import com.ijpay.alipay.AliPayApi;
import com.ijpay.core.kit.HttpKit;
import com.ijpay.core.kit.WxPayKit;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 包括支付成功/退款/转账等一系列类型的回调处理
 * @author xxm
 * @since 2021/2/27
 */
@IgnoreAuth
@Slf4j
@Tag(name = "支付通道信息回调")
@RestController
@RequestMapping("/callback/pay")
@AllArgsConstructor
public class PayCallbackController {

    private final AliPayCallbackService aliPayCallbackService;

    private final WeChatPayCallbackService weChatPayCallbackService;

    private final UnionPayCallbackService unionPayCallbackService;

    @SneakyThrows
    @Operation(summary = "支付宝信息回调")
    @PostMapping("/alipay")
    public String aliPayNotify(HttpServletRequest request) {
        Map<String, String> stringStringMap = AliPayApi.toMap(request);
        return aliPayCallbackService.callback(stringStringMap);
    }

    @SneakyThrows
    @Operation(summary = "微信支付信息回调")
    @PostMapping("/wechat")
    public String wechatPayNotify(HttpServletRequest request) {
        String xmlMsg = HttpKit.readData(request);
        Map<String, String> params = WxPayKit.xmlToMap(xmlMsg);
        return weChatPayCallbackService.callback(params);
    }

    @SneakyThrows
    @Operation(summary = "云闪付支付信息回调")
    @PostMapping("/union")
    public String unionPayNotify(HttpServletRequest request) {
        String xmlMsg = HttpKit.readData(request);
        Map<String, String> params = WxPayKit.xmlToMap(xmlMsg);
        return unionPayCallbackService.callback(params);
    }
}
