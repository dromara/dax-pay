package cn.daxpay.single.service.func;

import cn.daxpay.single.service.core.order.allocation.entity.AllocOrder;
import cn.daxpay.single.service.core.order.allocation.entity.AllocOrderDetail;
import cn.daxpay.single.service.core.payment.sync.result.AllocRemoteSyncResult;
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
public abstract class AbsAllocStrategy implements PayStrategy{

    private AllocOrder allocOrder;

    private List<AllocOrderDetail> allocOrderDetails;

    /**
     * 初始化参数
     */
    public void initParam(AllocOrder allocOrder, List<AllocOrderDetail> allocOrderDetails) {
        this.allocOrder = allocOrder;
        this.allocOrderDetails = allocOrderDetails;
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
    public abstract AllocRemoteSyncResult doSync();
}
