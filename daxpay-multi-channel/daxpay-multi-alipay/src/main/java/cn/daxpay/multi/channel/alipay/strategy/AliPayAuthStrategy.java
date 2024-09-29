package cn.daxpay.multi.channel.alipay.strategy;

import cn.daxpay.multi.channel.alipay.service.extra.AliPayAuthService;
import cn.daxpay.multi.core.enums.ChannelEnum;
import cn.daxpay.multi.core.param.assist.AuthCodeParam;
import cn.daxpay.multi.core.param.assist.GenerateAuthUrlParam;
import cn.daxpay.multi.core.result.assist.AuthResult;
import cn.daxpay.multi.core.result.assist.AuthUrlResult;
import cn.daxpay.multi.service.strategy.AbsChannelAuthStrategy;
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
