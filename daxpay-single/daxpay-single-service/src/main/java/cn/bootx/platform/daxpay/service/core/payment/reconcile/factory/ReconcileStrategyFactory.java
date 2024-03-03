package cn.bootx.platform.daxpay.service.core.payment.reconcile.factory;

import cn.bootx.platform.daxpay.code.PayChannelEnum;
import cn.bootx.platform.daxpay.exception.pay.PayUnsupportedMethodException;
import cn.bootx.platform.daxpay.service.func.AbsReconcileStrategy;
import cn.hutool.extra.spring.SpringUtil;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.List;
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
    public List<AbsReconcileStrategy> create() {
        Map<String, AbsReconcileStrategy> beansOfType = SpringUtil.getBeansOfType(AbsReconcileStrategy.class);
        return new ArrayList<>(beansOfType.values());
    }

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
