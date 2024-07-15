package cn.daxpay.single.service.core.payment.repair.strategy.pay;

import cn.daxpay.single.core.code.PayChannelEnum;
import cn.daxpay.single.service.func.AbsPayAdjustStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

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
public class UnionPayAdjustStrategy extends AbsPayAdjustStrategy {

    /**
     * 策略标识
     */
    @Override
    public String getChannel() {
        return PayChannelEnum.UNION_PAY.getCode();
    }

    /**
     * 关闭三方系统的支付
     */
    @Override
    public void doCloseRemoteHandler() {
        log.warn("云闪付未提供支付订单关闭接口");
    }
}
