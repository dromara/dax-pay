package org.dromara.daxpay.service.strategy;

import org.dromara.daxpay.service.bo.allocation.AllocStartResultBo;
import org.dromara.daxpay.service.bo.allocation.AllocSyncResultBo;
import org.dromara.daxpay.service.entity.allocation.order.AllocDetail;
import org.dromara.daxpay.service.entity.allocation.order.AllocOrder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * 分账处理抽象策略
 * @author xxm
 * @since 2024/4/1
 */
@Slf4j
@Getter
@Setter
public abstract class AbsAllocationStrategy implements PaymentStrategy{
    private AllocOrder order;

    private List<AllocDetail> details;

    /**
     * 初始化参数
     */
    public void initParam(AllocOrder order, List<AllocDetail> details) {
        this.order = order;
        this.details = details;
    }

    /**
     * 操作前处理, 校验和初始化支付配置
     */
    public void doBeforeHandler(){

    };

    /**
     * 开始分账
     */
    public abstract AllocStartResultBo start();

    /**
     * 分账完结
     */
    public abstract void finish();


    /**
     * 同步状态
     * 分账明细的状态变更会在这个里面进行, 由通道自己处理, 同时订单的状态和结果不要进行变更, 其他参数可以进行更新
     */
    public abstract AllocSyncResultBo doSync();

}
