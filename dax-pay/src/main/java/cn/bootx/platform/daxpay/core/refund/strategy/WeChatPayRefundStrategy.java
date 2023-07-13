package cn.bootx.platform.daxpay.core.refund.strategy;

import cn.bootx.platform.daxpay.code.pay.PayChannelEnum;
import cn.bootx.platform.daxpay.core.channel.wechat.dao.WeChatPayConfigManager;
import cn.bootx.platform.daxpay.core.channel.wechat.dao.WeChatPaymentManager;
import cn.bootx.platform.daxpay.core.channel.wechat.entity.WeChatPayConfig;
import cn.bootx.platform.daxpay.core.channel.wechat.service.WeChatPayCancelService;
import cn.bootx.platform.daxpay.core.channel.wechat.service.WeChatPaymentService;
import cn.bootx.platform.daxpay.core.payment.service.PaymentService;
import cn.bootx.platform.daxpay.core.refund.func.AbsPayRefundStrategy;
import cn.bootx.platform.daxpay.exception.payment.PayFailureException;
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

    private final WeChatPaymentManager weChatPaymentManager;
    private final WeChatPayConfigManager weChatPayConfigManager;

    private final WeChatPayCancelService weChatPayCancelService;
    private final WeChatPaymentService weChatPaymentService;
    private final PaymentService paymentService;


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
        this.weChatPayConfig = weChatPayConfigManager.findByMchAppCode(this.getPayment().getMchAppCode())
                        .orElseThrow(() -> new PayFailureException("支付配置不存在"));
    }

    /**
     * 退款
     */
    @Override
    public void doRefundHandler() {
        weChatPayCancelService.refund(this.getPayment(), this.getRefundModeParam().getAmount(), this.weChatPayConfig);
        weChatPaymentService.updatePayRefund(this.getPayment().getId(), this.getRefundModeParam().getAmount());
        paymentService.updateRefundSuccess(this.getPayment(), this.getRefundModeParam().getAmount(), PayChannelEnum.WECHAT);

    }

}
