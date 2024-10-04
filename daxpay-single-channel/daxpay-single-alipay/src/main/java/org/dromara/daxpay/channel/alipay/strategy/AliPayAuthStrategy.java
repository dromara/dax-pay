package org.dromara.daxpay.channel.alipay.strategy;

import org.dromara.daxpay.channel.alipay.service.extra.AliPayAuthService;
import org.dromara.daxpay.core.enums.ChannelEnum;
import org.dromara.daxpay.core.param.assist.AuthCodeParam;
import org.dromara.daxpay.core.param.assist.GenerateAuthUrlParam;
import org.dromara.daxpay.core.result.assist.AuthResult;
import org.dromara.daxpay.core.result.assist.AuthUrlResult;
import org.dromara.daxpay.service.strategy.AbsChannelAuthStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 支付宝授权策略
 * @author xxm
 * @since 2024/9/24
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AliPayAuthStrategy extends AbsChannelAuthStrategy {
    private final AliPayAuthService aliPayAuthService;

    /**
     * 策略标识, 可以自行进行扩展
     *
     * @see ChannelEnum
     */
    @Override
    public String getChannel() {
        return ChannelEnum.ALI.getCode();
    }

    /**
     * 获取授权链接
     */
    @Override
    public AuthUrlResult generateAuthUrl(GenerateAuthUrlParam param) {
        return aliPayAuthService.generateAuthUrl(param);
    }

    /**
     * 通过AuthCode兑换认证结果
     */
    @Override
    public AuthResult doAuth(AuthCodeParam param) {
        return aliPayAuthService.getOpenIdOrUserId(param.getAuthCode());
    }

}
