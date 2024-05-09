package cn.daxpay.single.service.func;

import cn.daxpay.single.service.core.order.refund.entity.RefundOrder;
import lombok.Getter;
import lombok.Setter;

/**
 * 抽象支付退款策略
 *
 * @author xxm
 * @since 2020/12/11
 */
@Getter
@Setter
public abstract class AbsRefundStrategy implements PayStrategy{

    /** 退款订单 */
    private RefundOrder refundOrder = null;


    /**
     * 退款前对处理, 主要进行配置的加载和检查
     */
    public void doBeforeRefundHandler() {
    }

    /**
     * 退款操作
     */
    public abstract void doRefundHandler();

}
