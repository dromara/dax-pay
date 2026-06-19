package cn.daxpay.open.payment.strategy.pay;

import cn.daxpay.open.payment.common.strategy.PaymentStrategy;
import cn.daxpay.open.platform.core.enums.pay.pay.CloseTypeEnum;
import cn.daxpay.open.payment.pay.order.entity.PayTrade;
import lombok.Getter;
import lombok.Setter;

/// # 支付关闭策略
///
@Getter
@Setter
public abstract class AbsPayCloseStrategy implements PaymentStrategy {

    /// 资金交易凭证
    private PayTrade trade = null;

    /// 是否使用撤销方式进行订单关闭
    private boolean useCancel = false;

    public void init(PayTrade trade, boolean useCancel){
        this.trade = trade;
        this.useCancel = useCancel;
    }

    /// 关闭前的处理方式
    public void doBeforeCloseHandler() {}

    /// 关闭操作
    /// @return 如果执行成功, 返回使用何种方式关闭的支付订单
    public abstract CloseTypeEnum doCloseHandler();

}

