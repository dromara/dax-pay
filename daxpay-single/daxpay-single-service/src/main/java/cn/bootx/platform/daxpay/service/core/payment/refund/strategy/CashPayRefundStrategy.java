package cn.bootx.platform.daxpay.service.core.payment.refund.strategy;

import cn.bootx.platform.daxpay.code.PayChannelEnum;
import cn.bootx.platform.daxpay.code.PayStatusEnum;
import cn.bootx.platform.daxpay.code.RefundStatusEnum;
import cn.bootx.platform.daxpay.service.core.channel.cash.service.CashRecordService;
import cn.bootx.platform.daxpay.service.func.AbsRefundStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

/**
 * 现金支付退款
 * @author xxm
 * @since 2023/7/5
 */
@Scope(SCOPE_PROTOTYPE)
@Component
@RequiredArgsConstructor
public class CashPayRefundStrategy extends AbsRefundStrategy {
    private final CashRecordService cashRecordService;

    /**
     * 策略标识
     */
    @Override
    public PayChannelEnum getChannel() {
        return PayChannelEnum.CASH;
    }


    /**
     * 退款操作
     */
    @Override
    public void doRefundHandler() {
        // 不包含异步支付
        if (!this.getPayOrder().isAsyncPay()){
            cashRecordService.refund(this.getRefundChannelOrder());
        }
    }

    /**
     * 退款发起成功操作
     */
    @Override
    public void doSuccessHandler() {
        // 包含异步支付, 变更状态到退款中
        if (this.getPayOrder().isAsyncPay()) {
            this.getPayChannelOrder().setStatus(PayStatusEnum.REFUNDING.getCode());
            this.getRefundChannelOrder().setStatus(RefundStatusEnum.PROGRESS.getCode());
        } else{
            // 同步支付, 直接标识状态为退款完成
            super.doSuccessHandler();
        }
    }
}
