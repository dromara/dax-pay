package cn.bootx.platform.daxpay.func;

import cn.bootx.platform.daxpay.code.PayChannelEnum;
import cn.bootx.platform.daxpay.common.exception.ExceptionInfo;
import cn.bootx.platform.daxpay.core.order.pay.entity.PayOrder;
import cn.bootx.platform.daxpay.param.pay.RefundChannelParam;
import cn.bootx.platform.daxpay.param.pay.RefundParam;
import lombok.Getter;
import lombok.Setter;

/**
 * 抽象支付退款策略基类
 *
 * @author xxm
 * @since 2020/12/11
 */
@Getter
@Setter
public abstract class AbsPayRefundStrategy implements PayStrategy{

    /** 支付对象 */
    private PayOrder order = null;

    /** 退款参数 */
    private RefundParam refundParam = null;

    /** 当前支付通道退款参数 退款参数中的与这个不一致, 以这个为准 */
    private RefundChannelParam channelParam = null;

    /**
     * 策略标识
     * @see PayChannelEnum
     */
    public abstract PayChannelEnum getType();

    /**
     * 初始化支付的参数
     */
    public void initPayParam(PayOrder payOrder, RefundParam refundParam) {
        this.order = payOrder;
        this.refundParam = refundParam;
    }

    /**
     * 退款前对处理 包含必要的校验以及对Payment对象的创建和保存操作
     */
    public void doBeforeRefundHandler() {
    }

    /**
     * 退款操作
     */
    public abstract void doRefundHandler();

    /**
     * 退款失败的处理方式
     */
    public void doErrorHandler(ExceptionInfo exceptionInfo) {
    }

}
