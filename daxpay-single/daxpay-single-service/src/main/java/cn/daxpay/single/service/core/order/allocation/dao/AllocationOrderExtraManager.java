package cn.daxpay.single.service.core.order.allocation.dao;

import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.daxpay.single.service.core.order.allocation.entity.AllocationOrderExtra;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

/**
 * 分账订单扩展
 * @author xxm
 * @since 2024/5/22
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class AllocationOrderExtraManager extends BaseManager<AllocationOrderExtraMapper, AllocationOrderExtra> {
}
