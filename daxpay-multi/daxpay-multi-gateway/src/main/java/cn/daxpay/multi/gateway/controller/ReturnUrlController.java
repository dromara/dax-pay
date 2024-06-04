package cn.daxpay.multi.gateway.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * 同步跳转通知控制器
 * @author xxm
 * @since 2024/6/4
 */
@Tag(name = "同步跳转通知控制器")
@RestController
@RequestMapping("/unipay/return")
@RequiredArgsConstructor
public class ReturnUrlController {

    @Operation(summary = "支付宝同步跳转通知")
    @GetMapping("/pay/alipay")
    public ModelAndView alipay(){
        return new ModelAndView("redirect:");
    }

    @Operation(summary = "微信同步跳转通知")
    @GetMapping("/pay/wechat")
    public ModelAndView wechat(){
        return new ModelAndView("redirect:");
    }

    @Operation(summary = "云闪付同步跳转通知")
    @PostMapping("/pay/union")
    public ModelAndView union(){
        return new ModelAndView("redirect:");
    }
}
