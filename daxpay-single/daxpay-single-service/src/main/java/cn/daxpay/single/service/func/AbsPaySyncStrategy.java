package cn.daxpay.single.service.func;

import cn.daxpay.single.code.PaySyncStatusEnum;
import cn.daxpay.single.service.core.order.pay.entity.PayOrder;
import cn.daxpay.single.service.core.payment.sync.result.PayRemoteSyncResult;
import lombok.Getter;
import lombok.Setter;

/**
 * 支付同步抽象类
 * @author xxm
 * @since 2023/7/14
 */
@Getter
@Setter
public abstract class AbsPaySyncStrategy implements PayStrategy{

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
     * @see PaySyncStatusEnum
     */
    public abstract PayRemoteSyncResult doSyncStatus();

}
