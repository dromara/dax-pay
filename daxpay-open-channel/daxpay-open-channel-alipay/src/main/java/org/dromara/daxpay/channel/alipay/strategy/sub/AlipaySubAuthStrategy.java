package org.dromara.daxpay.channel.alipay.strategy.sub;

import org.dromara.daxpay.channel.alipay.entity.config.AliPayConfig;
import org.dromara.daxpay.channel.alipay.service.payment.config.AlipayConfigService;
import org.dromara.daxpay.channel.alipay.service.payment.extra.AlipayAuthService;
import org.dromara.daxpay.core.enums.ChannelEnum;
import org.dromara.daxpay.core.param.assist.AuthCodeParam;
import org.dromara.daxpay.core.param.assist.GenerateAuthUrlParam;
import org.dromara.daxpay.core.result.assist.AuthResult;
import org.dromara.daxpay.core.result.assist.AuthUrlResult;
import org.dromara.daxpay.service.pay.strategy.AbsChannelAuthStrategy;
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
public class AlipaySubAuthStrategy extends AbsChannelAuthStrategy {
    private final AlipayAuthService aliPayAuthService;
    private final AlipayConfigService aliPayConfigService;

    /**
     * 策略标识, 可以自行进行扩展
     *
     * @see ChannelEnum
     */
    @Override
    public String getChannel() {
        return ChannelEnum.ALIPAY_ISV.getCode();
    }

    /**
     * 获取授权链接
     */
    @Override
    public AuthUrlResult generateAuthUrl(GenerateAuthUrlParam param) {
        AliPayConfig aliPayConfig = aliPayConfigService.getAliPayConfig(true);
        return aliPayAuthService.generateAuthUrl(param.getAuthPath(), getChannel(), aliPayConfig.getAppId(), aliPayConfig.getAliAppId());
    }

    /**
     * 通过AuthCode兑换认证结果
     */
    @Override
    public AuthResult doAuth(AuthCodeParam param) {
        AliPayConfig aliPayConfig = aliPayConfigService.getAliPayConfig(true);
        return aliPayAuthService.getOpenIdOrUserId(param.getAuthCode(), aliPayConfig);
    }

}
