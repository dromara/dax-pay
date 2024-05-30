package cn.daxpay.single.gateway.controller;

import cn.bootx.platform.common.core.annotation.IgnoreAuth;
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
 * 网关消息通知
 * @author xxm
 * @since 2024/4/16
 */
@IgnoreAuth
@Tag(name = "三方支付网关消息通知")
@RestController
@RequestMapping("/gateway/notice")
@RequiredArgsConstructor
public class PayNoticeReceiverController {

    @Operation(summary = "支付宝消息通知")
    @PostMapping("/alipay")
    public String aliPayNotice(HttpServletRequest request) {
        Map<String, String> map = AliPayApi.toMap(request);
        // 通过 msg_method 获取消息类型
        String msgMethod = map.get("msg_method");
        // 通过 biz_content 获取值
        String bizContent = map.get("biz_content");

        return "success";
    }

    @Operation(summary = "微信消息通知")
    @PostMapping("/wechat")
    public Map<String, Objects> wechatPayNotice() {
        return new HashMap<>();
    }

}
