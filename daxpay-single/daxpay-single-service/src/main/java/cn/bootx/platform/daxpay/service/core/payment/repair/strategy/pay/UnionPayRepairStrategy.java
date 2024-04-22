package cn.bootx.platform.daxpay.service.core.payment.repair.strategy.pay;

import cn.bootx.platform.daxpay.code.PayChannelEnum;
import cn.bootx.platform.daxpay.code.PayStatusEnum;
import cn.bootx.platform.daxpay.service.common.local.PaymentContextLocal;
import cn.bootx.platform.daxpay.service.func.AbsPayRepairStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

/**
 *
 * @author xxm
 * @since 2023/12/29
 */
@Slf4j
@Scope(SCOPE_PROTOTYPE)
@Service
@RequiredArgsConstructor
public class UnionPayRepairStrategy extends AbsPayRepairStrategy {

    /**
     * 策略标识
     */
    @Override
    public PayChannelEnum getChannel() {
        return PayChannelEnum.UNION_PAY;
    }

    /**
     * 支付成功处理
     */
    @Override
    public void doPaySuccessHandler() {
        LocalDateTime payTime = PaymentContextLocal.get()
                .getRepairInfo()
                .getFinishTime();
        this.getChannelOrder().setStatus(PayStatusEnum.SUCCESS.getCode())
                .setPayTime(payTime);
    }

    /**
     * 等待支付处理
     */
    @Override
    public void doWaitPayHandler(){
        this.getChannelOrder().setPayTime(null).setStatus(PayStatusEnum.PROGRESS.getCode());
    }

    /**
     * 关闭本地支付
     */
    @Override
    public void doCloseLocalHandler() {
        this.getChannelOrder().setStatus(PayStatusEnum.CLOSE.getCode());
    }
}
