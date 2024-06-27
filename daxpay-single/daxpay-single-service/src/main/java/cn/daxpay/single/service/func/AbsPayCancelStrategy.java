package cn.daxpay.single.service.func;

import cn.daxpay.single.service.core.order.pay.entity.PayOrder;
import lombok.Getter;
import lombok.Setter;

/**
 * 支付订单撤销接口
 * @author xxm
 * @since 2024/6/5
 */
@Getter
@Setter
public abstract class AbsPayCancelStrategy implements PayStrategy {
    /** 支付订单 */
    private PayOrder order = null;


    /**
     * 撤销前的处理方式
     */
    public void doBeforeCancelHandler() {}

    /**
     * 撤销操作
     */
    public abstract void doCancelHandler();
}
