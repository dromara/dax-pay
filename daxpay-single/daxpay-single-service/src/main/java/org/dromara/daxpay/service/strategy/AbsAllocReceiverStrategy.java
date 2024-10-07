package org.dromara.daxpay.service.strategy;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.dromara.daxpay.core.enums.AllocReceiverTypeEnum;
import org.dromara.daxpay.service.entity.allocation.receiver.AllocReceiver;

import java.util.List;

/**
 * 分账抽象策略
 * @author xxm
 * @since 2024/4/1
 */
@Slf4j
@Getter
@Setter
public abstract class AbsAllocReceiverStrategy implements PaymentStrategy{

    private AllocReceiver allocReceiver;

    /**
     * 支持的接收者类型
     */
    public abstract List<AllocReceiverTypeEnum> getSupportReceiverTypes();

    /**
     * 操作前校验
     */
    public boolean validation() {
        return true;
    }

    /**
     * 操作前处理, 校验和初始化支付配置
     */
    public void doBeforeHandler() {
    }

    /**
     * 添加到三方支付系统中
     */
    public abstract void bind();

    /**
     * 从三方支付系统中删除
     */
    public abstract void unbind();
}
