package cn.bootx.platform.daxpay.service.core.payment.refund.strategy;

import cn.bootx.platform.daxpay.code.PayChannelEnum;
import cn.bootx.platform.daxpay.code.PayRefundStatusEnum;
import cn.bootx.platform.daxpay.service.common.local.PaymentContextLocal;
import cn.bootx.platform.daxpay.service.core.channel.alipay.entity.AliPayConfig;
import cn.bootx.platform.daxpay.service.core.channel.alipay.service.AliPayConfigService;
import cn.bootx.platform.daxpay.service.core.channel.alipay.service.AliPayRefundService;
import cn.bootx.platform.daxpay.service.core.order.pay.service.PayChannelOrderService;
import cn.bootx.platform.daxpay.service.func.AbsRefundStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

/**
 * 支付宝退款
 * @author xxm
 * @since 2023/7/4
 */
@Scope(SCOPE_PROTOTYPE)
@Component
@RequiredArgsConstructor
public class AliPayRefundStrategy extends AbsRefundStrategy {

    private final AliPayConfigService alipayConfigService;
    private final AliPayRefundService aliRefundService;
    private final PayChannelOrderService payChannelOrderService;
    /**
     * 策略标识
     *
     * @see PayChannelEnum
     */
    @Override
    public PayChannelEnum getChannel() {
        return PayChannelEnum.ALI;
    }


    /**
     * 退款前前操作
     */
    @Override
    public void doBeforeRefundHandler() {
        AliPayConfig config = alipayConfigService.getConfig();
        alipayConfigService.initConfig(config);
    }

    /**
     * 退款
     */
    @Override
    public void doRefundHandler() {
        aliRefundService.refund(this.getPayOrder(), this.getRefundChannelParam().getAmount());
    }

    /**
     * 退款发起成功操作
     */
    @Override
    public void doSuccessHandler() {
        // 更新退款订单数据状态
        PayRefundStatusEnum refundStatusEnum = PaymentContextLocal.get()
                .getRefundInfo()
                .getStatus();
        this.getRefundChannelOrder().setStatus(refundStatusEnum.getCode());

        // 更新支付通道订单中的属性
        payChannelOrderService.updateAsyncPayRefund(this.getPayChannelOrder(), this.getRefundChannelOrder());
    }
}
