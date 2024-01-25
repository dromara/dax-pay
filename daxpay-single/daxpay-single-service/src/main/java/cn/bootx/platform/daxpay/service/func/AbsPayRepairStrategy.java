package cn.bootx.platform.daxpay.service.func;

import cn.bootx.platform.daxpay.service.core.order.pay.entity.PayOrder;
import lombok.Getter;
import lombok.Setter;

/**
 * 支付订单修复策略
 * 1. 异步支付方式要实现所有的修复方法
 * 2. 同步支付通常只需要实现必须得修复方法即可(关闭订单)
 * @author xxm
 * @since 2023/12/27
 */
@Getter
@Setter
public abstract class AbsPayRepairStrategy {

    /** 支付订单 */
    private PayOrder order = null;

    /** 支付订单 */


    /**
     * 初始化修复参数
     */
    public void initRepairParam(PayOrder order){
        this.order = order;
    }

    /**
     * 修复前处理
     */
    public void doBeforeHandler(){

    }

    /**
     * 支付成功处理
     */
    public void doSuccessHandler(){

    }

    /**
     * 关闭本地支付
     */
    public abstract void doCloseLocalHandler();

    /**
     * 关闭本地支付和网关支付, 默认为关闭本地支付
     */
    public void doCloseGatewayHandler() {
        this.doCloseLocalHandler();
    }

    /**
     * 退款处理 todo 需要结合退款同步功能进行协同实现
     */
    public void doRefundHandler() {

    }
}
