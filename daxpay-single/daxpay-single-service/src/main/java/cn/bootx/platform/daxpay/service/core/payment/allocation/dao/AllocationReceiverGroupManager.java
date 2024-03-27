package cn.bootx.platform.daxpay.service.core.payment.allocation.dao;

import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.daxpay.service.core.payment.allocation.entity.AllocationReceiverGroup;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

/**   
 * 
 * @author xxm  
 * @since 2024/3/27 
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class AllocationReceiverGroupManager extends BaseManager<AllocationReceiverGroupMapper, AllocationReceiverGroup> {
}
