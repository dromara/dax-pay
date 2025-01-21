package org.dromara.daxpay.channel.wechat.strategy.merchant;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.daxpay.channel.wechat.param.pay.WechatPayParam;
import org.dromara.daxpay.channel.wechat.service.assist.WechatAuthService;
import org.dromara.daxpay.core.enums.ChannelAuthStatusEnum;
import org.dromara.daxpay.core.enums.ChannelEnum;
import org.dromara.daxpay.core.param.assist.AuthCodeParam;
import org.dromara.daxpay.core.param.checkout.CheckoutPayParam;
import org.dromara.daxpay.core.param.trade.pay.PayParam;
import org.dromara.daxpay.core.result.assist.AuthResult;
import org.dromara.daxpay.service.strategy.AbsCheckoutStrategy;
import org.springframework.stereotype.Component;

/**
 * 微信收银台支付策略
 * @author xxm
 * @since 2024/12/2
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class WechatCheckoutStrategy extends AbsCheckoutStrategy {
    private final WechatAuthService wechatAuthService;

    /**
     * 策略标识, 可以自行进行扩展
     *
     * @see ChannelEnum
     */
    @Override
    public String getChannel() {
        return ChannelEnum.WECHAT.getCode();
    }

    /**
     * 生成授权链接, 主要是微信类通道使用, 用于获取OpenId
     */
    @Override
    public String generateAuthUrl(String orderNo) {
        String redirectUrl = StrUtil.format("/aggregate/wechat/{}", orderNo);
        return wechatAuthService.generateInnerAuthUrl(redirectUrl);
    }

    /**
     * 检测付款码
     */
    @Override
    public boolean checkBarCode(String barCode){
        String[] wx = { "10", "11", "12", "13", "14", "15" };
        return StrUtil.startWithAny(barCode.substring(0, 2), wx);
    }
    /**
     * 获取认证结果
     */
    @Override
    public AuthResult doAuth(AuthCodeParam param) {
        String openId = wechatAuthService.getTokenAndOpenId(param.getAuthCode()).getOpenId();
        return new AuthResult().setStatus(ChannelAuthStatusEnum.SUCCESS.getCode()).setOpenId(openId);
    }

    /**
     * 支付参数处理
     */
    @Override
    public void handlePayParam(CheckoutPayParam checkoutPayParam, PayParam payParam) {
        WechatPayParam wechatPayParam = new WechatPayParam();
        wechatPayParam.setOpenId(checkoutPayParam.getOpenId());
        wechatPayParam.setAuthCode(checkoutPayParam.getBarCode());
        payParam.setExtraParam(JSONUtil.toJsonStr(wechatPayParam));
    }

}
