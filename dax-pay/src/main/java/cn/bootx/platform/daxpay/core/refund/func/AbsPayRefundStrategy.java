package cn.bootx.platform.daxpay.core.refund.func;

import cn.bootx.platform.daxpay.code.pay.PayChannelEnum;
import cn.bootx.platform.daxpay.core.pay.exception.ExceptionInfo;
import cn.bootx.platform.daxpay.core.payment.entity.Payment;
import cn.bootx.platform.daxpay.param.refund.RefundModeParam;
import cn.bootx.platform.daxpay.param.refund.RefundParam;
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
public abstract class AbsPayRefundStrategy {

    /** 支付对象 */
    private Payment payment = null;

    /** 退款参数 */
    private RefundParam refundParam = null;

    /** 当前支付方式退款参数 退款参参数中的与这个不一致, 以这个为准 */
    private RefundModeParam refundModeParam = null;

    /**
     * 策略标识
     * @see PayChannelEnum
     */
    public abstract PayChannelEnum getType();

    /**
     * 初始化支付的参数
     */
    public void initPayParam(Payment payment, RefundParam refundParam) {
        this.payment = payment;
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
