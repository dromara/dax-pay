package org.dromara.daxpay.channel.wechat.strategy.sub;

import org.dromara.daxpay.channel.wechat.enums.WechatAuthTypeEnum;
import org.dromara.daxpay.channel.wechat.service.config.WechatPayConfigService;
import org.dromara.daxpay.payment.pay.enums.ChannelEnum;
import org.dromara.daxpay.payment.unipay.param.assist.AuthCodeParam;
import org.dromara.daxpay.payment.unipay.param.assist.GenerateAuthUrlParam;
import org.dromara.daxpay.payment.unipay.result.assist.AuthResult;
import org.dromara.daxpay.payment.unipay.result.assist.AuthUrlResult;
import org.dromara.daxpay.payment.pay.service.assist.WechatOpenAuthService;
import org.dromara.daxpay.payment.pay.strategy.AbsChannelAuthStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
        if (param.getAuthType().equals(WechatAuthTypeEnum.SUB.getCode())) {
            // 二级商户获取
            return wechatAuthService.generateInnerAuthUrl(param.getAuthPath(), config.getAuthUrl(), this.getChannel(), param.getAppId(), config.getSubAppId(), config.getAppSecret());
        } else {
            return wechatAuthService.generateInnerAuthUrl(param.getAuthPath(), config.getAuthUrl(), this.getChannel(), param.getAppId(), config.getWxAppId(), config.getAppSecret());
        }
    }

    /**
     * 通过AuthCode兑换认证结果
     */
    @Override
    public AuthResult doAuth(AuthCodeParam param) {
        var config = wechatPayConfigService.getAndCheckConfig(true);
        if (param.getAuthType().equals(WechatAuthTypeEnum.SUB.getCode())){
            // 二级商户获取
            return wechatAuthService.getTokenAndOpenId(param.getAuthCode(), config.getSubAppId(), config.getAppSecret());
        } else {
            return wechatAuthService.getTokenAndOpenId(param.getAuthCode(), config.getWxAppId(), config.getAppSecret());
        }
    }
}
