package cn.bootx.platform.daxpay.service.func;

import cn.bootx.platform.daxpay.service.core.order.allocation.entity.AllocationOrder;
import cn.bootx.platform.daxpay.service.core.order.allocation.entity.AllocationOrderDetail;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * 分账接收方管理抽象策略
 * @author xxm
 * @since 2024/4/1
 */
@Slf4j
@Getter
@Setter
public abstract class AbsAllocationStrategy implements PayStrategy{

    private AllocationOrder allocationOrder;

    private List<AllocationOrderDetail> allocationOrderDetails;

    /**
     * 初始化参数
     */
    public void initParam(AllocationOrder allocationOrder, List<AllocationOrderDetail> allocationOrderDetails) {
        this.allocationOrder = allocationOrder;
        this.allocationOrderDetails = allocationOrderDetails;
    }

    /**
     * 操作前处理, 校验和初始化支付配置
     */
    public abstract void doBeforeHandler();

    /**
     * 分账操作
     */
    public abstract void allocation();

    /**
     * 分账完结
     */
    public abstract void finish();

    /**
     * 同步状态
     */
    public abstract void doSync();
}
