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
import org.dromara.daxpay.core.param.cashier.CashierCodePayParam;
import org.dromara.daxpay.core.param.trade.pay.PayParam;
import org.dromara.daxpay.core.result.assist.AuthResult;
import org.dromara.daxpay.service.strategy.AbsCashierCodeStrategy;
import org.springframework.stereotype.Component;

/**
 * 微信收银码牌支付
 * @author xxm
 * @since 2024/9/29
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class WechatCashierCodeStrategy extends AbsCashierCodeStrategy {
    private final WechatAuthService wechatAuthService;

    /**
     * 生成授权链接, 主要是微信类通道使用, 用于获取OpenId
     */
    @Override
    public String generateAuthUrl(String cashierCode) {
        String redirectUrl = StrUtil.format("/cashier/wechat/{}", cashierCode);
        return wechatAuthService.generateInnerAuthUrl(redirectUrl);
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
    public void handlePayParam(CashierCodePayParam cashierPayParam, PayParam payParam) {
        WechatPayParam wechatPayParam = new WechatPayParam();
        wechatPayParam.setOpenId(cashierPayParam.getOpenId());
        payParam.setExtraParam(JSONUtil.toJsonStr(wechatPayParam));
    }

    /**
     * 策略标识, 可以自行进行扩展
     */
    @Override
    public String getChannel() {
        return ChannelEnum.WECHAT.getCode();
    }
}
