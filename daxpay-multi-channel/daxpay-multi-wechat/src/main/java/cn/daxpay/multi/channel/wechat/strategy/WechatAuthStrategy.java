package cn.daxpay.multi.channel.wechat.strategy;

import cn.daxpay.multi.channel.wechat.service.assist.WechatAuthService;
import cn.daxpay.multi.core.enums.ChannelEnum;
import cn.daxpay.multi.core.param.assist.AuthCodeParam;
import cn.daxpay.multi.core.param.assist.GenerateAuthUrlParam;
import cn.daxpay.multi.core.result.assist.AuthResult;
import cn.daxpay.multi.core.result.assist.AuthUrlResult;
import cn.daxpay.multi.service.strategy.AbsChannelAuthStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

/**
 * 微信认证
 * @author xxm
 * @since 2024/9/24
 */
@Slf4j
@Scope(SCOPE_PROTOTYPE)
@Service
@RequiredArgsConstructor
public class WechatAuthStrategy extends AbsChannelAuthStrategy {
    private final WechatAuthService wechatAuthService;

    @Override
    public String getChannel() {
        return ChannelEnum.WECHAT.getCode();
    }

    /**
     * 获取授权链接
     */
    @Override
    public AuthUrlResult generateAuthUrl(GenerateAuthUrlParam param) {
        return wechatAuthService.generateAuthUrl(param);
    }

    /**
     * 通过AuthCode兑换认证结果
     */
    @Override
    public AuthResult doAuth(AuthCodeParam param) {
        return wechatAuthService.getTokenAndOpenId(param.getAuthCode());
    }
}
