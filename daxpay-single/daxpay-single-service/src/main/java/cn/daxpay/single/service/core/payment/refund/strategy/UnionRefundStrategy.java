package cn.daxpay.single.service.core.payment.refund.strategy;

import cn.daxpay.single.code.PayChannelEnum;
import cn.daxpay.single.service.core.channel.union.entity.UnionPayConfig;
import cn.daxpay.single.service.core.channel.union.service.UnionPayConfigService;
import cn.daxpay.single.service.core.channel.union.service.UnionPayRefundService;
import cn.daxpay.single.service.func.AbsRefundStrategy;
import cn.daxpay.single.service.sdk.union.api.UnionPayKit;
import lombok.RequiredArgsConstructor;
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
     * @see PayChannelEnum
     */
    @Override
    public PayChannelEnum getChannel() {
        return PayChannelEnum.UNION_PAY;
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
    public void doRefundHandler() {
        UnionPayKit unionPayKit = unionPayConfigService.initPayService(unionPayConfig);
        unionPayRefundService.refund(this.getRefundOrder(), unionPayKit);
    }
}
