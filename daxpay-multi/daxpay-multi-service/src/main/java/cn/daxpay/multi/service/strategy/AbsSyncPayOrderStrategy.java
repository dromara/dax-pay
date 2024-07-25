package cn.daxpay.multi.service.strategy;

import cn.daxpay.multi.service.bo.sync.PaySyncResultBo;
import cn.daxpay.multi.service.entity.order.pay.PayOrder;
import cn.daxpay.multi.service.enums.PaySyncResultEnum;
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
     * 初始化参数
     */
    public void initPayParam(PayOrder order) {
        this.order = order;
    }


    /**
     * 异步支付单与支付网关进行状态比对后的结果
     * @see PaySyncResultEnum
     */
    public abstract PaySyncResultBo doSync();

}
