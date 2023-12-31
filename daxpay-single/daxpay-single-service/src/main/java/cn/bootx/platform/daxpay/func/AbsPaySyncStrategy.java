package cn.bootx.platform.daxpay.func;

import cn.bootx.platform.daxpay.code.PaySyncStatusEnum;
import cn.bootx.platform.daxpay.core.order.pay.entity.PayOrder;
import cn.bootx.platform.daxpay.core.payment.sync.result.GatewaySyncResult;
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
     * 初始化支付的参数
     */
    public void initPayParam(PayOrder order) {
        this.order = order;
    }


    /**
     * 异步支付单与支付网关进行状态比对
     * @see PaySyncStatusEnum
     */
    public abstract GatewaySyncResult doSyncStatus();

}
