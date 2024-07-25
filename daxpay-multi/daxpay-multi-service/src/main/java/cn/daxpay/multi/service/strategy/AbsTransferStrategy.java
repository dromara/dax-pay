package cn.daxpay.multi.service.strategy;

import cn.daxpay.multi.service.bo.trade.TransferResultBo;
import cn.daxpay.multi.service.entity.order.transfer.TransferOrder;
import cn.daxpay.multi.core.param.trade.transfer.TransferParam;
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
