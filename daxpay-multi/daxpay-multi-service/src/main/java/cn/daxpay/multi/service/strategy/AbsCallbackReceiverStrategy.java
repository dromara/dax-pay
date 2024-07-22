package cn.daxpay.multi.service.strategy;

/**
 * 回调处理策略
 * @author xxm
 * @since 2024/7/19
 */
public abstract class AbsCallbackReceiverStrategy implements PaymentStrategy{

    void doCallbackHandler(){}
}
