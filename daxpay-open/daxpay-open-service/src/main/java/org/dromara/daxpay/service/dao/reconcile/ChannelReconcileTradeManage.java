package org.dromara.daxpay.service.dao.reconcile;

import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import org.dromara.daxpay.service.entity.reconcile.ChannelReconcileTrade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 *
 * @author xxm
 * @since 2024/8/6
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class ChannelReconcileTradeManage extends BaseManager<ChannelReconcileTradeMapper, ChannelReconcileTrade> {

    /**
     * 根据订单id查询
     */
    public List<ChannelReconcileTrade> findAllByReconcileId(Long reconcileId){
        return this.findAllByField(ChannelReconcileTrade::getReconcileId, reconcileId);
    }
}
