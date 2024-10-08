package cn.daxpay.multi.channel.alipay.strategy;

import cn.daxpay.multi.channel.alipay.service.config.AliPayConfigService;
import cn.daxpay.multi.channel.alipay.service.refund.AliPayRefundService;
import cn.daxpay.multi.core.enums.ChannelEnum;
import cn.daxpay.multi.service.bo.trade.RefundResultBo;
import cn.daxpay.multi.service.strategy.AbsRefundStrategy;
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
public class AliPayRefundStrategy extends AbsRefundStrategy {

    private final AliPayRefundService aliRefundService;

    /**
     * 策略标识
     * @see ChannelEnum
     */
     @Override
    public String getChannel() {
        return ChannelEnum.ALI.getCode();
    }


    /**
     * 退款
     */
    @Override
    public RefundResultBo doRefundHandler() {
        return aliRefundService.refund(this.getRefundOrder());
    }
}
