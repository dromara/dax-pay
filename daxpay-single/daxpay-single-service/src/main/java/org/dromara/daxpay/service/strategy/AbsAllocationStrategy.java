package org.dromara.daxpay.service.strategy;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.dromara.daxpay.service.bo.allocation.AllocStartResultBo;
import org.dromara.daxpay.service.entity.allocation.transaction.AllocDetail;
import org.dromara.daxpay.service.entity.allocation.transaction.AllocOrder;

import java.util.List;

/**
 * 分账接收方管理抽象策略
 * @author xxm
 * @since 2024/4/1
 */
@Slf4j
@Getter
@Setter
public abstract class AbsAllocationStrategy implements PaymentStrategy{
    private AllocOrder transaction;

    private List<AllocDetail> details;

    /**
     * 初始化参数
     */
    public void initParam(AllocOrder transaction, List<AllocDetail> details) {
        this.transaction = transaction;
        this.details = details;
    }

    /**
     * 操作前处理, 校验和初始化支付配置
     */
    public abstract void doBeforeHandler();

    /**
     * 分账启动
     */
    public abstract AllocStartResultBo start();

    /**
     * 分账完结
     */
    public abstract void finish();

}
