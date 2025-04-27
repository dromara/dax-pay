package org.dromara.daxpay.service.strategy;

import org.dromara.daxpay.service.bo.trade.TransferResultBo;
import org.dromara.daxpay.service.entity.order.transfer.TransferOrder;
import org.dromara.daxpay.core.param.trade.transfer.TransferParam;
import lombok.Getter;
import lombok.Setter;

/**
 * 转账抽象策略
 * @author xxm
 * @since 2024/3/21
 */
@Getter
@Setter
public abstract class AbsTransferStrategy implements PaymentStrategy{
    /** 转账订单 */
    private TransferOrder transferOrder;

    /**
     * 校验参数
     */
    public void doValidateParam(TransferParam transferParam) {

    }

    /**
     * 转账前操作
     */
    public void doBeforeHandler(){}

    /**
     * 转账操作
     */
    public abstract TransferResultBo doTransferHandler();

}
