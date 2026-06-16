package org.dromara.daxpay.payment.old.pay.strategy;

import org.dromara.daxpay.payment.common.strategy.PaymentStrategy;
import org.dromara.daxpay.platform.core.enums.pay.pay.CloseTypeEnum;
import org.dromara.daxpay.payment.old.pay.entity.order.pay.PayOrder;
import lombok.Getter;
import lombok.Setter;

/// # 支付关闭策略
///
@Getter
@Setter
public abstract class AbsPayCloseStrategy implements PaymentStrategy {

    /// 支付订单
    private PayOrder order = null;

    /// 是否使用撤销方式进行订单关闭
    private boolean useCancel = false;

    public void init(PayOrder order, boolean useCancel){
        this.order = order;
        this.useCancel = useCancel;
    }

    /// 关闭前的处理方式
    public void doBeforeCloseHandler() {}

    /// 关闭操作
    /// @return 如果执行成功, 返回使用何种方式关闭的支付订单
    public abstract CloseTypeEnum doCloseHandler();

}

