package cn.bootx.platform.daxpay.service.core.order.allocation.dao;

import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.daxpay.service.core.order.allocation.entity.AllocationOrder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 *
 * @author xxm
 * @since 2024/4/7
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class AllocationOrderManager extends BaseManager<AllocationOrderMapper, AllocationOrder> {

    /**
     * 根据分账单号查询
     */
    public Optional<AllocationOrder> findByAllocationNo(String allocationNo){
        return findByField(AllocationOrder::getAllocationNo, allocationNo);
    }
}
