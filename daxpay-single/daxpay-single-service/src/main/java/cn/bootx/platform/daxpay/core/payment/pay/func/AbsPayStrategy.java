package cn.bootx.platform.daxpay.core.payment.pay.func;

import cn.bootx.platform.daxpay.code.PayChannelEnum;
import cn.bootx.platform.daxpay.common.exception.ExceptionInfo;
import cn.bootx.platform.daxpay.core.order.pay.entity.PayOrder;
import cn.bootx.platform.daxpay.param.pay.PayParam;
import cn.bootx.platform.daxpay.param.pay.PayWayParam;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * 抽象支付策略基类 同步支付 异步支付 错误处理 关闭支付 撤销支付 支付网关同步 退款
 *
 * @author xxm
 * @since 2020/12/11
 */
@Getter
@Setter
public abstract class AbsPayStrategy {

    /** 支付对象 */
    private PayOrder order = null;

    /** 支付参数 */
    private PayParam payParam = null;

    /** 支付方式参数 支付参数中的与这个不一致, 以这个为准 */
    private PayWayParam payWayParam = null;

    /**
     * 策略标示
     * @see PayChannelEnum
     */
    public abstract PayChannelEnum getType();

    /**
     * 初始化支付的参数
     */
    public void initPayParam(PayOrder order, PayParam payParam) {
        this.order = order;
        this.payParam = payParam;
    }

    /**
     * 支付前对处理 包含必要的校验以及对Payment对象的创建和保存操作
     */
    public void doBeforePayHandler() {
    }

    /**
     * 支付操作
     */
    public abstract void doPayHandler();

    /**
     * 支付成功的处理方式
     */
    public void doSuccessHandler() {
    }

    /**
     * 支付失败的处理方式
     */
    public void doErrorHandler(ExceptionInfo exceptionInfo) {
    }

    /**
     * 异步支付成功的处理方式
     */
    public void doAsyncSuccessHandler(Map<String, String> map) {
    }

    /**
     * 异步支付失败的处理方式, 默认使用支付失败的处理方式 同步支付方式调用时同 this#doErrorHandler
     */
    public void doAsyncErrorHandler(ExceptionInfo exceptionInfo) {
        this.doErrorHandler(exceptionInfo);
    }

    /**
     * 撤销支付操作，支付交易返回失败或支付系统超时，调用该接口撤销交易 默认为关闭本地支付记录
     */
    public void doCancelHandler() {
        this.doCloseHandler();
    }

    /**
     * 关闭本地支付记录
     */
    public abstract void doCloseHandler();

}
