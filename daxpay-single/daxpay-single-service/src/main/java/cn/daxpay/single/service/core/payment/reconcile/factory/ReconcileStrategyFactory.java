package cn.daxpay.single.service.core.payment.reconcile.factory;

import cn.daxpay.single.code.PayChannelEnum;
import cn.daxpay.single.exception.pay.PayUnsupportedMethodException;
import cn.daxpay.single.service.func.AbsReconcileStrategy;
import cn.hutool.extra.spring.SpringUtil;
import lombok.experimental.UtilityClass;

import java.util.Map;
import java.util.Objects;

/**
 * 支付对账工厂类
 * @author xxm
 * @since 2024/1/18
 */
@UtilityClass
public class ReconcileStrategyFactory {

    /**
     * 根据传入的支付类型批量创建策略
     */
    public AbsReconcileStrategy create(String channel) {
        PayChannelEnum channelEnum = PayChannelEnum.findByCode(channel);
        Map<String, AbsReconcileStrategy> beansOfType = SpringUtil.getBeansOfType(AbsReconcileStrategy.class);
        return beansOfType.values().stream()
                .filter(strategy -> Objects.equals(strategy.getChannel(), channelEnum))
                .findFirst()
                .orElseThrow(PayUnsupportedMethodException::new);
    }

}
