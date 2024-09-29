package cn.daxpay.multi.channel.wechat.strategy;

import cn.daxpay.multi.channel.wechat.service.pay.WechatPayCashierService;
import cn.daxpay.multi.core.enums.ChannelEnum;
import cn.daxpay.multi.core.param.cashier.CashierAuthCodeParam;
import cn.daxpay.multi.core.param.cashier.CashierPayParam;
import cn.daxpay.multi.core.param.trade.pay.PayParam;
import cn.daxpay.multi.service.strategy.AbsChannelCashierStrategy;
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
    public String generateAuthUrl(CashierAuthCodeParam param) {
        return wechatPayCashierService.generateAuthUrl(param);
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
