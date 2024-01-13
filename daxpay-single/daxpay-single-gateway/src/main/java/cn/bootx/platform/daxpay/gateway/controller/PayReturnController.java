package cn.bootx.platform.daxpay.gateway.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * 支付同步通知控制器
 * @author xxm
 * @since 2024/1/13
 */
@Tag(name = "支付同步通知控制器")
@RestController
@RequestMapping("/pay/return")
@RequiredArgsConstructor
public class PayReturnController {

    @Operation(summary = "支付宝同步通知")
    @GetMapping("/alipay")
    public ModelAndView alipay(){
        return null;
    }

    @Operation(summary = "微信同步通知")
    @GetMapping("/wechat")
    public ModelAndView wechat(){
        return null;
    }
}
