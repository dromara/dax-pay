package org.dromara.daxpay.service.dao.order.allocation;

import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import org.dromara.daxpay.service.entity.order.allocation.AllocOrderDetail;
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
public class AllocOrderDetailManager extends BaseManager<AllocOrderDetailMapper, AllocOrderDetail> {

    /**
     * 根据订单ID查询
     */
    public List<AllocOrderDetail> findAllByOrderId(Long orderId) {
        return findAllByField(AllocOrderDetail::getAllocId, orderId);
    }
}
