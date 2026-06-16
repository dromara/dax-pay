package org.dromara.daxpay.payment.old.pay.strategy;

import org.dromara.daxpay.payment.common.strategy.PaymentStrategy;
import org.dromara.daxpay.payment.old.pay.bo.trade.TransferResultBo;
import org.dromara.daxpay.payment.old.pay.entity.order.transfer.TransferOrder;
import org.dromara.daxpay.payment.unipay.param.trade.transfer.TransferParam;
import lombok.Getter;
import lombok.Setter;

/// # 转账抽象策略
///
@Getter
@Setter
public abstract class AbsTransferStrategy implements PaymentStrategy {
    /// 转账订单
    private TransferOrder transferOrder;
    /// 转账参数
    private TransferParam transferParam;

    /// 校验参数
    public void doValidateParam() {

    }

    /// 转账前操作
    public void doBeforeHandler(){}

    /// 转账操作
    public abstract TransferResultBo doTransferHandler();

}
