package org.dromara.daxpay.channel.wechat.strategy.sub;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.daxpay.channel.wechat.service.payment.config.WechatPayConfigService;
import org.dromara.daxpay.core.enums.ChannelEnum;
import org.dromara.daxpay.core.param.assist.AuthCodeParam;
import org.dromara.daxpay.core.param.assist.GenerateAuthUrlParam;
import org.dromara.daxpay.core.result.assist.AuthResult;
import org.dromara.daxpay.core.result.assist.AuthUrlResult;
import org.dromara.daxpay.service.service.assist.WechatOpenAuthService;
import org.dromara.daxpay.service.strategy.AbsChannelAuthStrategy;
import org.springframework.stereotype.Service;

/**
 * 微信认证
 * @author xxm
 * @since 2024/9/24
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WechatSubAuthStrategy extends AbsChannelAuthStrategy {
    private final WechatOpenAuthService wechatAuthService;

    private final WechatPayConfigService wechatPayConfigService;


    @Override
    public String getChannel() {
        return ChannelEnum.WECHAT_ISV.getCode();
    }

    /**
     * 获取授权链接
     */
    @Override
    public AuthUrlResult generateAuthUrl(GenerateAuthUrlParam param) {
        var config = wechatPayConfigService.getAndCheckConfig(true);
        return wechatAuthService.generateInnerAuthUrl(param.getAuthPath(),config.getAuthUrl(), this.getChannel(), config.getAppId(), config.getWxAppId(),config.getAppSecret());
    }

    /**
     * 通过AuthCode兑换认证结果
     */
    @Override
    public AuthResult doAuth(AuthCodeParam param) {
        var config = wechatPayConfigService.getAndCheckConfig(true);
        return wechatAuthService.getTokenAndOpenId(param.getAuthCode(), config.getWxAppId(), config.getAppSecret());
    }
}
