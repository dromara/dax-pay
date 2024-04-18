package cn.bootx.platform.daxpay.service.core.order.allocation.dao;

import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.daxpay.service.core.order.allocation.entity.AllocationOrderDetail;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 *
 * @author xxm
 * @since 2024/4/7
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class AllocationOrderDetailManager extends BaseManager<AllocationOrderDetailMapper, AllocationOrderDetail> {

    /**
     * 根据订单ID查询
     */
    public List<AllocationOrderDetail> findAllByOrderId(Long orderId) {
        return findAllByField(AllocationOrderDetail::getAllocationId, orderId);
    }
}
