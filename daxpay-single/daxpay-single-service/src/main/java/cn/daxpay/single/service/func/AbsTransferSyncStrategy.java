package cn.daxpay.single.service.func;

import cn.daxpay.single.code.PaySyncStatusEnum;
import cn.daxpay.single.service.core.order.transfer.entity.TransferOrder;
import cn.daxpay.single.service.core.payment.sync.result.RefundRemoteSyncResult;
import lombok.Getter;
import lombok.Setter;

/**
 * 转账修复策略
 * @author xxm
 * @since 2024/6/17
 */
@Getter
@Setter
public abstract class AbsTransferSyncStrategy implements PayStrategy{

    private TransferOrder transferOrder;

    /**
     * 同步前处理
     */
    public void doBeforeHandler(){}
    /**
     * 异步支付单与支付网关进行状态比对后的结果
     * @see PaySyncStatusEnum
     */
    public abstract RefundRemoteSyncResult doSyncStatus();
}
