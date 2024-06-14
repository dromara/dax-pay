package cn.daxpay.single.service.func;

import cn.daxpay.single.param.payment.transfer.TransferParam;
import cn.daxpay.single.service.core.order.transfer.entity.TransferOrder;
import lombok.Getter;
import lombok.Setter;

/**
 * 转账抽象策略
 * @author xxm
 * @since 2024/3/21
 */
@Getter
@Setter
public abstract class AbsTransferStrategy implements PayStrategy{
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
    public abstract void doTransferHandler();

}
