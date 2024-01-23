package cn.bootx.platform.daxpay.service.core.payment.refund.strategy;

import cn.bootx.platform.daxpay.code.PayChannelEnum;
import cn.bootx.platform.daxpay.service.common.local.PaymentContextLocal;
import cn.bootx.platform.daxpay.service.core.channel.alipay.entity.AliPayConfig;
import cn.bootx.platform.daxpay.service.core.channel.alipay.service.AliPayConfigService;
import cn.bootx.platform.daxpay.service.core.channel.alipay.service.AliPayOrderService;
import cn.bootx.platform.daxpay.service.core.channel.alipay.service.AliPayRefundService;
import cn.bootx.platform.daxpay.service.core.order.pay.service.PayOrderService;
import cn.bootx.platform.daxpay.service.core.order.refund.entity.PayRefundChannelOrder;
import cn.bootx.platform.daxpay.service.func.AbsPayRefundStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

/**
 * 支付宝退款
 * @author xxm
 * @since 2023/7/4
 */
@Scope(SCOPE_PROTOTYPE)
@Component
@RequiredArgsConstructor
public class AliPayRefundStrategy extends AbsPayRefundStrategy {

    private final AliPayConfigService alipayConfigService;
    private final AliPayOrderService aliPayOrderService;
    private final AliPayRefundService aliRefundService;
    /**
     * 策略标识
     *
     * @see PayChannelEnum
     */
    @Override
    public PayChannelEnum getType() {
        return PayChannelEnum.ALI;
    }

    private final PayOrderService payOrderService;


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
        aliPayOrderService.updateRefund(this.getPayOrder().getId(), this.getRefundChannelParam().getAmount());
        payOrderService.updateRefundSuccess(this.getPayOrder(), this.getRefundChannelParam().getAmount(), PayChannelEnum.ALI);
    }

    /**
     * 生成通道退款订单对象
     */
    @Override
    public PayRefundChannelOrder generateChannelOrder() {
        PayRefundChannelOrder payRefundChannelOrder = super.generateChannelOrder();
        // 追加关联对款请求号
        String refundRequestNo = PaymentContextLocal.get()
                .getRefundInfo()
                .getGatewayRequestNo();
        return payRefundChannelOrder.setGatewayRequestNo(refundRequestNo);
    }
}
