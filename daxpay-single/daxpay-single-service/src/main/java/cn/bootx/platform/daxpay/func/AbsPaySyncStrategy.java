package cn.bootx.platform.daxpay.func;

import cn.bootx.platform.daxpay.code.PayStatusEnum;
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

    /**
     * 支付单和网关状态是否一致
     */
    public boolean isStatusSync(GatewaySyncResult syncResult){
        String syncStatus = syncResult.getSyncStatus();
        String orderStatus = order.getStatus();
        // 支付成功比对
        if (orderStatus.equals(PayStatusEnum.SUCCESS.getCode()) && syncStatus.equals(PaySyncStatusEnum.PAY_SUCCESS.getCode())){
            return true;
        }
        // 待支付比对
        if (orderStatus.equals(PayStatusEnum.PROGRESS.getCode()) && syncStatus.equals(PaySyncStatusEnum.PAY_WAIT.getCode())){
            return true;
        }

        // 支付关闭比对
        if (orderStatus.equals(PayStatusEnum.CLOSE.getCode()) && syncStatus.equals(PaySyncStatusEnum.CLOSED.getCode())){
            return true;
        }

        // 退款比对
        if (orderStatus.equals(PayStatusEnum.REFUNDED.getCode()) && syncStatus.equals(PaySyncStatusEnum.REFUND.getCode())){
            return true;
        }
        return false;
    }

}
