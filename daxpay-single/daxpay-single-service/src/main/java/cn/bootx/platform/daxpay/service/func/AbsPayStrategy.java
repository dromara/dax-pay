package cn.bootx.platform.daxpay.service.func;

import cn.bootx.platform.daxpay.param.payment.pay.PayParam;
import cn.bootx.platform.daxpay.service.core.order.pay.entity.PayOrder;
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

    /** 支付订单 */
    private PayOrder order = null;

    /** 支付参数 */
    private PayParam payParam = null;


    /**
     * 初始化支付的参数支付上下文
     *
     */
    public void initPayParam(PayOrder order, PayParam payParam) {
        this.order = order;
        this.payParam = payParam;
    }

    /**
     * 支付前处理 包含必要的校验以及对当前通道支付配置信息的初始化
     */
    public void doBeforePayHandler() {
    }

    /**
     * 支付操作
     */
    public abstract void doPayHandler();

}
