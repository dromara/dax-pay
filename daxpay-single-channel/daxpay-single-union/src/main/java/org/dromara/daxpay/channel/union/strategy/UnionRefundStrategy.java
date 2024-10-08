package org.dromara.daxpay.channel.union.strategy;

import lombok.RequiredArgsConstructor;
import org.dromara.daxpay.channel.union.entity.config.UnionPayConfig;
import org.dromara.daxpay.channel.union.sdk.api.UnionPayKit;
import org.dromara.daxpay.channel.union.service.config.UnionPayConfigService;
import org.dromara.daxpay.channel.union.service.refund.UnionPayRefundService;
import org.dromara.daxpay.core.enums.ChannelEnum;
import org.dromara.daxpay.service.bo.trade.RefundResultBo;
import org.dromara.daxpay.service.strategy.AbsRefundStrategy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

/**
 * 云闪付支付退款
 * @author xxm
 * @since 2023/7/5
 */
@Scope(SCOPE_PROTOTYPE)
@Component
@RequiredArgsConstructor
public class UnionRefundStrategy extends AbsRefundStrategy {

    private final UnionPayRefundService unionPayRefundService;

    private final UnionPayConfigService unionPayConfigService;

    private UnionPayConfig unionPayConfig;

    /**
     * 策略标识
     *
     * @see ChannelEnum
     */
    @Override
    public String getChannel() {
        return ChannelEnum.UNION_PAY.getCode();
    }

    /**
     * 退款前对处理, 初始化微信支付配置
     */
    @Override
    public void doBeforeRefundHandler() {
        this.unionPayConfig = unionPayConfigService.getAndCheckConfig();
    }


    /**
     * 退款操作
     */
    @Override
    public RefundResultBo doRefundHandler() {
        UnionPayKit unionPayKit = unionPayConfigService.initPayService(unionPayConfig);
        return unionPayRefundService.refund(this.getRefundOrder(), unionPayKit);
    }
}
