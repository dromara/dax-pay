package org.dromara.daxpay.channel.wechat.strategy.isv;

import org.dromara.daxpay.channel.wechat.service.assist.WechatSubAuthService;
import org.dromara.daxpay.core.enums.ChannelEnum;
import org.dromara.daxpay.core.param.assist.AuthCodeParam;
import org.dromara.daxpay.core.param.assist.GenerateAuthUrlParam;
import org.dromara.daxpay.core.result.assist.AuthResult;
import org.dromara.daxpay.core.result.assist.AuthUrlResult;
import org.dromara.daxpay.service.strategy.AbsChannelAuthStrategy;
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
public class WechatIsvAuthStrategy extends AbsChannelAuthStrategy {
    private final WechatSubAuthService wechatAuthService;

    @Override
    public String getChannel() {
        return ChannelEnum.WECHAT_ISV.getCode();
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
