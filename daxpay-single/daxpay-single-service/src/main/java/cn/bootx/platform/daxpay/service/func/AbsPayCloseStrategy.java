package cn.bootx.platform.daxpay.service.func;

import cn.bootx.platform.daxpay.code.PayChannelEnum;
import cn.bootx.platform.daxpay.code.PayStatusEnum;
import cn.bootx.platform.daxpay.service.core.order.pay.entity.PayChannelOrder;
import cn.bootx.platform.daxpay.service.core.order.pay.entity.PayOrder;
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

    /** 支付订单 */
    private PayOrder order = null;

    /** 支付通道订单 */
    private PayChannelOrder channelOrder = null;

    /**
     * 策略标识
     * @see PayChannelEnum
     */
    public abstract PayChannelEnum getType();

    public void initCloseParam(PayOrder order, PayChannelOrder channelOrder){
        this.order = order;
        this.channelOrder = channelOrder;
    }

    /**
     * 关闭前的处理方式
     */
    public void doBeforeCloseHandler() {}

    /**
     * 关闭操作
     */
    public abstract void doCloseHandler();

    /**
     * 关闭成功的处理方式
     */
    public void doSuccessHandler() {
        // 更新通道支付订单的状态为关闭
        this.getChannelOrder().setStatus(PayStatusEnum.CLOSE.getCode());
    }
}
