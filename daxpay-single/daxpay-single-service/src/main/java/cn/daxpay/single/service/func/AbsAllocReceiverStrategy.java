package cn.daxpay.single.service.func;

import cn.daxpay.single.service.core.payment.allocation.entity.AllocReceiver;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * 分账抽象策略
 * @author xxm
 * @since 2024/4/1
 */
@Slf4j
@Getter
@Setter
public abstract class AbsAllocReceiverStrategy implements PayStrategy{

    private AllocReceiver allocReceiver;

    /**
     * 操作前校验
     */
    public abstract boolean validation();

    /**
     * 操作前处理, 校验和初始化支付配置
     */
    public abstract void doBeforeHandler();

    /**
     * 添加到三方支付系统中
     */
    public abstract void bind();

    /**
     * 从三方支付系统中删除
     */
    public abstract void unbind();
}
