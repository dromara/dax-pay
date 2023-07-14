package cn.bootx.platform.daxpay.core.sync.func;

import cn.bootx.platform.daxpay.code.pay.PaySyncStatus;
import cn.bootx.platform.daxpay.core.payment.entity.Payment;
import cn.bootx.platform.daxpay.core.sync.result.PaySyncResult;
import lombok.Getter;
import lombok.Setter;

/**
 * 支付同步抽象类
 * @author xxm
 * @since 2023/7/14
 */
@Getter
@Setter
public abstract class AbsPaySyncStrategy {

    /** 支付对象 */
    private Payment payment = null;

    /**
     * 初始化支付的参数
     */
    public void initPayParam(Payment payment) {
        this.payment = payment;
    }


    /**
     * 异步支付单与支付网关进行状态比对
     * @see PaySyncStatus
     */
    public abstract PaySyncResult doSyncPayStatusHandler();

}
