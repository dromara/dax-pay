package org.dromara.daxpay.service.strategy;

import org.dromara.daxpay.service.bo.sync.PaySyncResultBo;
import org.dromara.daxpay.service.entity.order.pay.PayOrder;
import lombok.Getter;
import lombok.Setter;

/**
 * 支付同步抽象类
 * @author xxm
 * @since 2023/7/14
 */
@Getter
@Setter
public abstract class AbsSyncPayOrderStrategy implements PaymentStrategy{

    /** 支付订单 */
    private PayOrder order = null;


    /**
     * 异步支付单与支付网关进行状态比对后的结果
     */
    public abstract PaySyncResultBo doSync();

}
