package cn.daxpay.single.service.func;

import cn.daxpay.single.service.core.order.pay.entity.PayOrder;
import lombok.Getter;
import lombok.Setter;

/**
 * 支付订单调整策略
 * @author xxm
 * @since 2023/12/27
 */
@Getter
@Setter
public abstract class AbsPayAdjustStrategy implements PayStrategy{

    /** 支付订单 */
    private PayOrder order = null;

    /**
     * 调整前处理
     */
    public void doBeforeHandler(){

    }

    /**
     * 关闭三方系统的支付
     */
    public abstract void doCloseRemoteHandler();

}
