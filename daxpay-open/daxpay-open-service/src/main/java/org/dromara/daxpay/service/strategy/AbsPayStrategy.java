package org.dromara.daxpay.service.strategy;

import org.dromara.daxpay.core.param.trade.pay.PayParam;
import org.dromara.daxpay.service.bo.trade.PayResultBo;
import org.dromara.daxpay.service.entity.order.pay.PayOrder;
import lombok.Getter;
import lombok.Setter;

/**
 * 抽象支付策略基类
 *
 * @author xxm
 * @since 2020/12/11
 */
@Getter
@Setter
public abstract class AbsPayStrategy implements PaymentStrategy{

    /** 支付订单 */
    private PayOrder order = null;

    /** 支付参数 */
    private PayParam payParam = null;


    /**
     * 初始化支付的参数支付上下文
     */
    public void initPayParam(PayOrder order, PayParam payParam) {
        this.order = order;
        this.payParam = payParam;
    }

    /**
     * 支付前处理 包含必要的校验以及对当前通道支付配置信息的初始化
     * 出现错误不会保存相关信息
     */
    public void doBeforePayHandler(){
    }

    /**
     * 支付操作
     * 出现错误会保存相关信息
     */
    public abstract PayResultBo doPayHandler();

}
