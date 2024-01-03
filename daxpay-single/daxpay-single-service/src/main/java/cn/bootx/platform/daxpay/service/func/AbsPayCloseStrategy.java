package cn.bootx.platform.daxpay.service.func;

import cn.bootx.platform.daxpay.service.common.exception.ExceptionInfo;
import cn.bootx.platform.daxpay.service.core.record.pay.entity.PayOrder;
import lombok.Getter;
import lombok.Setter;

/**
 * 支付关闭策略
 * @author xxm
 * @since 2023/12/29
 */
@Getter
@Setter
public abstract class AbsPayCloseStrategy implements PayStrategy{

    /** 支付对象 */
    private PayOrder order = null;

    public void initCloseParam(PayOrder order){
        this.order = order;
    }

    /**
     * 关闭前的处理方式
     */
    public void doBeforeCloseHandler() {

    }

    /**
     * 关闭操作
     */
    public abstract void doCloseHandler();

    /**
     * 关闭失败的处理方式
     */
    public void doErrorHandler(ExceptionInfo exceptionInfo) {
    }
}
