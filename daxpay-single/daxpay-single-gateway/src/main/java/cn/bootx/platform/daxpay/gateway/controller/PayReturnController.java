package cn.bootx.platform.daxpay.gateway.controller;

import cn.bootx.platform.common.core.annotation.IgnoreAuth;
import cn.bootx.platform.daxpay.service.core.notice.service.PayReturnService;
import cn.bootx.platform.daxpay.service.param.channel.alipay.AliPayReturnParam;
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
@IgnoreAuth
@Tag(name = "支付同步通知")
@RestController
@RequestMapping("/return/pay")
@RequiredArgsConstructor
public class PayReturnController {
    private final PayReturnService payReturnService;

    @Operation(summary = "支付宝同步跳转连接")
    @GetMapping("/alipay")
    public ModelAndView alipay(AliPayReturnParam param){
        String url = payReturnService.alipay(param);
        return new ModelAndView("redirect:" + url);
    }

    @Operation(summary = "微信同步通知")
    @GetMapping("/wechat")
    public ModelAndView wechat(){
        return null;
    }
}
