package cn.bootx.platform.daxpay.service.func;

import cn.bootx.platform.daxpay.code.PayChannelEnum;
import cn.bootx.platform.daxpay.service.common.exception.ExceptionInfo;
import cn.bootx.platform.daxpay.service.core.order.pay.entity.PayOrder;
import cn.bootx.platform.daxpay.param.pay.PayParam;
import cn.bootx.platform.daxpay.param.pay.PayChannelParam;
import lombok.Getter;
import lombok.Setter;

/**
 * 抽象支付策略基类 同步支付 异步支付 错误处理 关闭支付 撤销支付 支付网关同步 退款
 *
 * @author xxm
 * @since 2020/12/11
 */
@Getter
@Setter
public abstract class AbsPayStrategy implements PayStrategy{


    /** 支付对象 */
    private PayOrder order = null;

    /** 支付参数 */
    private PayParam payParam = null;

    /** 支付方式参数 支付参数中的与这个不一致, 以这个为准 */
    private PayChannelParam payChannelParam = null;

    /**
     * 策略标识
     * @see PayChannelEnum
     */
    public abstract PayChannelEnum getType();

    /**
     * 初始化支付的参数支付上下文
     *
     */
    public void initPayParam(PayOrder order, PayParam payParam) {
        this.order = order;
        this.payParam = payParam;
    }

    /**
     * 支付前处理 包含必要的校验以及对当前通道支付订单的创建和保存操作
     */
    public void doBeforePayHandler() {
    }

    /**
     * 支付操作
     */
    public abstract void doPayHandler();

    /**
     * 支付调起成功的处理方式
     */
    public void doSuccessHandler() {
    }

    /**
     * 支付失败的处理方式 TODO 后期考虑如何进行错误处理
     */
    public void doErrorHandler(ExceptionInfo exceptionInfo) {
    }

    /**
     * 关闭支付. 支付交易返回失败或支付系统超时调通该接口关闭支付
     */
    public abstract void doCloseHandler();


}
