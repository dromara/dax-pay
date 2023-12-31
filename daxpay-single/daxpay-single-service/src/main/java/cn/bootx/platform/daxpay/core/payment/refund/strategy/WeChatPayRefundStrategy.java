package cn.bootx.platform.daxpay.core.payment.refund.strategy;

import cn.bootx.platform.daxpay.code.PayChannelEnum;
import cn.bootx.platform.daxpay.core.channel.wechat.entity.WeChatPayConfig;
import cn.bootx.platform.daxpay.core.channel.wechat.service.WeChatPayConfigService;
import cn.bootx.platform.daxpay.core.channel.wechat.service.WeChatPayOrderService;
import cn.bootx.platform.daxpay.core.channel.wechat.service.WechatRefundService;
import cn.bootx.platform.daxpay.core.order.pay.service.PayOrderService;
import cn.bootx.platform.daxpay.func.AbsPayRefundStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

/**
 * 微信支付退款
 * @author xxm
 * @since 2023/7/5
 */
@Scope(SCOPE_PROTOTYPE)
@Component
@RequiredArgsConstructor
public class WeChatPayRefundStrategy extends AbsPayRefundStrategy {

    private final WeChatPayConfigService weChatPayConfigService;

    private final WechatRefundService wechatRefundService;
    private final WeChatPayOrderService weChatPayOrderService;
    private final PayOrderService payOrderService;

    private WeChatPayConfig weChatPayConfig;

    /**
     * 策略标识
     *
     * @see PayChannelEnum
     */
    @Override
    public PayChannelEnum getType() {
        return PayChannelEnum.WECHAT;
    }

    /**
     * 退款前对处理, 初始化微信支付配置
     */
    @Override
    public void doBeforeRefundHandler() {
        this.weChatPayConfig = weChatPayConfigService.getConfig();
    }

    /**
     * 退款
     */
    @Override
    public void doRefundHandler() {
        wechatRefundService.refund(this.getOrder(), this.getChannelParam().getAmount(), this.weChatPayConfig);
        weChatPayOrderService.updateRefund(this.getOrder().getId(), this.getChannelParam().getAmount());
        payOrderService.updateRefundSuccess(this.getOrder(), this.getChannelParam().getAmount(), PayChannelEnum.WECHAT);

    }

}
