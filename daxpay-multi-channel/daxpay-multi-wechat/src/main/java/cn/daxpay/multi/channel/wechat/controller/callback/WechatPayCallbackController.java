package cn.daxpay.multi.channel.wechat.controller.callback;

import cn.bootx.platform.core.annotation.IgnoreAuth;
import cn.daxpay.multi.channel.wechat.service.callback.WechatPayTransferCallbackService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author xxm
 * @since 2024/7/29
 */
@IgnoreAuth
@Tag(name = "微信回调通知控制器")
@RestController
@RequestMapping("/unipay/callback/{mchNo}/{AppId}")
@RequiredArgsConstructor
public class WechatPayCallbackController {
    private final WechatPayTransferCallbackService wechatPayTransferCallbackService;


    @SneakyThrows
    @Operation(summary = "微信转账回调")
    @PostMapping("/transfer/wechat")
    public String wechatRefundNotify(@PathVariable("mchNo") String mchNo, @PathVariable("AppId") String appId, HttpServletRequest request) {
        return "";
    }
}
