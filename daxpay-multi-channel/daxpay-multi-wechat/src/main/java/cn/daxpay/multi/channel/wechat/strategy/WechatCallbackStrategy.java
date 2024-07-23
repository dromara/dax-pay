package cn.daxpay.multi.channel.wechat.strategy;

import cn.daxpay.multi.channel.wechat.service.callback.WechatPayCallbackService;
import cn.daxpay.multi.channel.wechat.service.callback.WechatRefundCallbackService;
import cn.daxpay.multi.core.enums.ChannelEnum;
import cn.daxpay.multi.service.strategy.AbsCallbackStrategy;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

/**
 * 微信支付回调处理
 * @author xxm
 * @since 2024/7/22
 */
@Slf4j
@Scope(SCOPE_PROTOTYPE)
@Service
@RequiredArgsConstructor
public class WechatCallbackStrategy extends AbsCallbackStrategy {

    private final WechatPayCallbackService payCallbackService;

    private final WechatRefundCallbackService refundCallbackService;

    @Override
    public String getChannel() {
        return ChannelEnum.WECHAT.getCode();
    }

    /**
     * 支付回调处理
     */
    @Override
    public String doPayCallbackHandler(HttpServletRequest request) {
        return payCallbackService.pay(request);
    }

    /**
     * 退款回调处理
     */
    @Override
    public String doRefundCallbackHandler(HttpServletRequest request) {
        return refundCallbackService.refund(request);
    }
}
