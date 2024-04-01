package cn.bootx.platform.daxpay.service.core.payment.allocation.dao;

import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.daxpay.service.core.payment.allocation.entity.AllocationGroupReceiver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

/**
 * 分账组关联接收方
 * @author xxm
 * @since 2024/3/27
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class AllocationGroupReceiverManager extends BaseManager<AllocationGroupReceiverMapper, AllocationGroupReceiver> {

    /**
     * 判断接收者是否已经被使用
     */
    public boolean isUsed(Long receiverId){
        return existedByField(AllocationGroupReceiver::getReceiverId, receiverId);
    }
}
