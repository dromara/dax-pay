package cn.daxpay.multi.channel.alipay.strategy;

import cn.daxpay.multi.channel.alipay.service.callback.AliPayCallbackService;
import cn.daxpay.multi.core.enums.ChannelEnum;
import cn.daxpay.multi.service.strategy.AbsCallbackStrategy;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

/**
 * 支付宝回调处理
 * @author xxm
 * @since 2024/7/22
 */
@Slf4j
@Scope(SCOPE_PROTOTYPE)
@Service
@RequiredArgsConstructor
public class AliPayCallbackStrategy extends AbsCallbackStrategy {

    private final AliPayCallbackService aliPayCallbackService;

    @Override
    public String getChannel() {
        return ChannelEnum.WECHAT.getCode();
    }

    /**
     * 支付回调处理
     */
    @Override
    public String doPayCallbackHandler(HttpServletRequest request) {
        return aliPayCallbackService.pay(request);
    }
}
