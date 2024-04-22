package cn.bootx.platform.daxpay.service.func;

import cn.bootx.platform.daxpay.param.payment.refund.RefundParam;
import cn.bootx.platform.daxpay.service.core.order.pay.entity.PayOrder;
import cn.bootx.platform.daxpay.service.core.order.refund.entity.RefundOrder;
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

    /** 支付订单 */
    private PayOrder payOrder = null;

    /** 退款订单 */
    private RefundOrder refundOrder = null;

    /** 退款参数 */
    private RefundParam refundParam;


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
