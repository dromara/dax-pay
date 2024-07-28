package cn.daxpay.multi.channel.alipay.strategy;

import cn.daxpay.multi.channel.alipay.entity.config.AliPayConfig;
import cn.daxpay.multi.channel.alipay.service.config.AliPayConfigService;
import cn.daxpay.multi.channel.alipay.service.sync.AliPayRefundSyncService;
import cn.daxpay.multi.core.enums.ChannelEnum;
import cn.daxpay.multi.core.exception.OperationFailException;
import cn.daxpay.multi.service.bo.sync.RefundSyncResultBo;
import cn.daxpay.multi.service.entity.order.refund.RefundOrder;
import cn.daxpay.multi.service.enums.PaySyncResultEnum;
import cn.daxpay.multi.service.strategy.AbsSyncRefundOrderStrategy;
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
public class AliPaySyncRefundStrategy extends AbsSyncRefundOrderStrategy {


    private final AliPayConfigService alipayConfigService;

    private final AliPayRefundSyncService alipaySyncService;

    /**
     * 策略标识, 可以自行进行扩展
     *
     * @see ChannelEnum
     */
    @Override
    public String getChannel() {
        return ChannelEnum.ALI.getCode();
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
     * @see PaySyncResultEnum
     */
    @Override
    public RefundSyncResultBo doSync() {
        AliPayConfig config = alipayConfigService.getAliPayConfig();
        return alipaySyncService.syncRefundStatus(getRefundOrder(), config);
    }

}
