package cn.bootx.platform.daxpay.func;

import cn.bootx.platform.daxpay.code.PayChannelEnum;
import cn.bootx.platform.daxpay.core.order.pay.entity.PayOrder;
import cn.bootx.platform.daxpay.core.payment.repair.param.PayOrderRepairParam;
import lombok.Getter;
import lombok.Setter;

/**
 * 支付订单修复策略
 * @author xxm
 * @since 2023/12/27
 */
@Getter
@Setter
public abstract class AbsPayRepairStrategy {


    /** 支付对象 */
    private PayOrder order = null;

    /** 支付对象 */
    private PayOrderRepairParam repairParam = null;


    /**
     * 初始化修复参数
     */
    public void initRepairParam(PayOrder order,PayOrderRepairParam repairParam){
        this.order = order;
        this.repairParam = repairParam;
    }

    /**
     * 策略标识
     * @see PayChannelEnum
     */
    public abstract PayChannelEnum getType();

    /**
     * 支付成功处理
     */
    public abstract void successHandler();

    /**
     * 取消支付
     */
    public abstract void closeHandler();

    /**
     * 退款
     */
    public abstract void refundHandler();

}
