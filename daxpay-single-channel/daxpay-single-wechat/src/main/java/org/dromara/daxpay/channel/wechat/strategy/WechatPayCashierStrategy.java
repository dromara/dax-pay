package org.dromara.daxpay.channel.wechat.strategy;

import org.dromara.daxpay.channel.wechat.service.pay.WechatPayCashierService;
import org.dromara.daxpay.core.enums.ChannelAuthStatusEnum;
import org.dromara.daxpay.core.enums.ChannelEnum;
import org.dromara.daxpay.core.param.cashier.CashierAuthCodeParam;
import org.dromara.daxpay.core.param.cashier.CashierAuthUrlParam;
import org.dromara.daxpay.core.param.cashier.CashierPayParam;
import org.dromara.daxpay.core.param.trade.pay.PayParam;
import org.dromara.daxpay.core.result.assist.AuthResult;
import org.dromara.daxpay.service.strategy.AbsChannelCashierStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 微信收银支付
 * @author xxm
 * @since 2024/9/29
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class WechatPayCashierStrategy extends AbsChannelCashierStrategy {
    private final WechatPayCashierService wechatPayCashierService;

    /**
     * 生成授权链接, 主要是微信类通道使用, 用于获取OpenId
     */
    @Override
    public String generateAuthUrl(CashierAuthUrlParam param) {
        return wechatPayCashierService.generateAuthUrl(param);
    }

    /**
     * 获取认证结果
     */
    @Override
    public AuthResult doAuth(CashierAuthCodeParam param) {
        String openId = wechatPayCashierService.getOpenId(param);
        return new AuthResult().setStatus(ChannelAuthStatusEnum.SUCCESS.getCode()).setOpenId(openId);
    }

    /**
     * 支付参数处理
     */
    @Override
    public void handlePayParam(CashierPayParam cashierPayParam, PayParam payParam) {
        wechatPayCashierService.handlePayParam(cashierPayParam,payParam);
    }

    /**
     * 策略标识, 可以自行进行扩展
     */
    @Override
    public String getChannel() {
        return ChannelEnum.WECHAT.getCode();
    }
}
