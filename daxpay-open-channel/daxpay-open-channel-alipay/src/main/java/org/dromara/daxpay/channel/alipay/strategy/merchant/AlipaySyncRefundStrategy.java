package org.dromara.daxpay.channel.alipay.strategy.merchant;

import org.dromara.daxpay.channel.alipay.entity.config.AliPayConfig;
import org.dromara.daxpay.channel.alipay.service.payment.config.AlipayConfigService;
import org.dromara.daxpay.channel.alipay.service.payment.sync.AlipayRefundSyncService;
import org.dromara.daxpay.core.enums.ChannelEnum;
import org.dromara.daxpay.core.exception.OperationFailException;
import org.dromara.daxpay.service.pay.bo.sync.RefundSyncResultBo;
import org.dromara.daxpay.service.pay.entity.order.refund.RefundOrder;
import org.dromara.daxpay.service.pay.strategy.AbsSyncRefundOrderStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

/**
 * 支付宝退款订单查询
 * @author xxm
 * @since 2024/7/25
 */
@Scope(SCOPE_PROTOTYPE)
@Component
@RequiredArgsConstructor
public class AlipaySyncRefundStrategy extends AbsSyncRefundOrderStrategy {


    private final AlipayRefundSyncService alipaySyncService;
    private final AlipayConfigService aliPayConfigService;

    /**
     * 策略标识, 可以自行进行扩展
     *
     * @see ChannelEnum
     */
    @Override
    public String getChannel() {
        return ChannelEnum.ALIPAY.getCode();
    }


    /**
     * 间隔10秒以上再发起退款查询请求。小于10s可能会导致查询到的状态不正确, 因为支付宝没有转账失败的状态
     */
    @Override
    public void doBeforeHandler() {
        RefundOrder refundOrder = this.getRefundOrder();
        if (refundOrder.getReqTime().plusSeconds(10).isAfter(LocalDateTime.now())) {
            throw new OperationFailException("间隔10秒以上再发起退款查询请求");
        }
    }

    /**
     * 异步支付单与支付网关进行状态比对后的结果
     *
     */
    @Override
    public RefundSyncResultBo doSync() {
        AliPayConfig aliPayConfig = aliPayConfigService.getAliPayConfig(false);
        return alipaySyncService.syncRefundStatus(this.getRefundOrder(), aliPayConfig);
    }

}
