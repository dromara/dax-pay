package cn.daxpay.multi.gateway.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

/**
 * 同步跳转通知控制器
 * @author xxm
 * @since 2024/6/4
 */
@Tag(name = "同步通知跳转控制器")
@RestController
@RequestMapping("/unipay/return/{mchNo}/{AppId}")
@RequiredArgsConstructor
public class RedirectUrlController {

    @Operation(summary = "支付宝同步跳转通知")
    @GetMapping("/alipay")
    public ModelAndView alipay(@PathVariable("mchNo") String mchNo, @PathVariable("AppId") String appId){
        return new ModelAndView("redirect:");
    }

    @Operation(summary = "微信同步跳转通知")
    @GetMapping("/wechat")
    public ModelAndView wechat(@PathVariable("mchNo") String mchNo, @PathVariable("AppId") String appId){
        return new ModelAndView("redirect:");
    }

    @Operation(summary = "云闪付同步跳转通知")
    @PostMapping("/union")
    public ModelAndView union(@PathVariable("mchNo") String mchNo, @PathVariable("AppId") String appId){
        return new ModelAndView("redirect:");
    }
}
