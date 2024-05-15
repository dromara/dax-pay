package cn.daxpay.single.service.core.payment.refund.strategy;

import cn.daxpay.single.code.PayChannelEnum;
import cn.daxpay.single.service.core.channel.wechat.entity.WeChatPayConfig;
import cn.daxpay.single.service.core.channel.wechat.service.WeChatPayConfigService;
import cn.daxpay.single.service.core.channel.wechat.service.WechatPayRefundService;
import cn.daxpay.single.service.func.AbsRefundStrategy;
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
public class WeChatRefundStrategy extends AbsRefundStrategy {

    private final WeChatPayConfigService weChatPayConfigService;

    private final WechatPayRefundService wechatPayRefundService;

    private WeChatPayConfig weChatPayConfig;

    /**
     * 策略标识
     *
     * @see PayChannelEnum
     */
    @Override
    public PayChannelEnum getChannel() {
        return PayChannelEnum.WECHAT;
    }

    /**
     * 退款前对处理, 初始化微信支付配置
     */
    @Override
    public void doBeforeRefundHandler() {
        this.weChatPayConfig = weChatPayConfigService.getAndCheckConfig();
    }


    /**
     * 退款操作
     */
    @Override
    public void doRefundHandler() {
        wechatPayRefundService.refund(this.getRefundOrder(), this.weChatPayConfig);
    }

}
