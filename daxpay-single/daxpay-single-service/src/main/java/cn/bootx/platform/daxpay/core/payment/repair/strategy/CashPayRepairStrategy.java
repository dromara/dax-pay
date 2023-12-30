package cn.bootx.platform.daxpay.core.payment.repair.strategy;

import cn.bootx.platform.daxpay.code.PayChannelEnum;
import cn.bootx.platform.daxpay.common.entity.OrderRefundableInfo;
import cn.bootx.platform.daxpay.core.channel.cash.service.CashService;
import cn.bootx.platform.daxpay.func.AbsPayRepairStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.Objects;

import static cn.bootx.platform.daxpay.code.PayChannelEnum.CASH;
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
public class CashPayRepairStrategy extends AbsPayRepairStrategy {

    private final CashService cashService;

    /**
     * 策略标识
     *
     * @see PayChannelEnum
     */
    @Override
    public PayChannelEnum getType() {
        return CASH;
    }

    /**
     * 取消支付
     */
    @Override
    public void doCloseHandler() {
        // 获取现金的可退款金额
        Integer amount = this.getOrder()
                .getRefundableInfos()
                .stream()
                .filter(info -> Objects.equals(info.getChannel(), CASH.getCode()))
                .findFirst()
                .map(OrderRefundableInfo::getAmount)
                .orElse(0);
        cashService.close(this.getOrder().getId());


    }
}
