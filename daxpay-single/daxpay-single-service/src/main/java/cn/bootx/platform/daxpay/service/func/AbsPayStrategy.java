package cn.bootx.platform.daxpay.service.func;

import cn.bootx.platform.daxpay.code.PayStatusEnum;
import cn.bootx.platform.daxpay.param.pay.PayChannelParam;
import cn.bootx.platform.daxpay.param.pay.PayParam;
import cn.bootx.platform.daxpay.service.core.order.pay.builder.PayBuilder;
import cn.bootx.platform.daxpay.service.core.order.pay.entity.PayChannelOrder;
import cn.bootx.platform.daxpay.service.core.order.pay.entity.PayOrder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

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

    /** 通道支付订单, 异步支付通道单独处理通道支付订单 */
    private PayChannelOrder channelOrder = null;

    /** 支付参数 */
    private PayParam payParam = null;

    /** 支付方式参数 支付参数中的与这个不一致, 以这个为准 */
    private PayChannelParam payChannelParam = null;


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
     * 生成通道支付单, 如果不需要可以进行覆盖
     * 异步支付通道都进行了重新, 通道支付单由自己控制
     */
    public void generateChannelOrder() {
        PayChannelOrder payChannelOrder = PayBuilder.buildPayChannelOrder(this.getPayChannelParam());
        payChannelOrder.setPaymentId(this.getOrder().getOrderNo());
        this.channelOrder = payChannelOrder;
    }

    /**
     * 支付操作
     */
    public abstract void doPayHandler();

    /**
     * 支付调起成功的处理方式
     */
    public void doSuccessHandler() {
        this.channelOrder.setStatus(PayStatusEnum.SUCCESS.getCode()).setPayTime(LocalDateTime.now());
    }

}
