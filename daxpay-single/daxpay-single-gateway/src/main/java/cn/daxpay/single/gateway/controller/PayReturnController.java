package cn.daxpay.single.gateway.controller;

import cn.bootx.platform.common.core.annotation.IgnoreAuth;
import cn.daxpay.single.service.core.payment.notice.service.PayReturnService;
import cn.daxpay.single.service.param.channel.alipay.AliPayReturnParam;
import cn.daxpay.single.service.param.channel.union.UnionPayReturnParam;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * 同步通知跳转控制器
 * @author xxm
 * @since 2024/1/13
 */
@IgnoreAuth
@Tag(name = "同步通知跳转控制器")
@RestController
@RequestMapping("/unipay/return")
@RequiredArgsConstructor
public class PayReturnController {
    private final PayReturnService payReturnService;

    @Operation(summary = "支付宝同步跳转通知")
    @GetMapping("/alipay")
    public ModelAndView alipay(AliPayReturnParam param){
        String url = payReturnService.alipay(param);
        return new ModelAndView("redirect:" + url);
    }

    @Operation(summary = "微信同步跳转通知")
    @GetMapping("/wechat")
    public ModelAndView wechat(){
        return null;
    }

    @Operation(summary = "云闪付同步跳转通知")
    @PostMapping("/union")
    public ModelAndView union(UnionPayReturnParam param){
        String url = payReturnService.union(param);
        return new ModelAndView("redirect:" + url);
    }
}
