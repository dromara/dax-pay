package cn.daxpay.single.service.core.payment.allocation.dao;

import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.daxpay.single.service.core.payment.allocation.entity.AllocGroupReceiver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 分账组关联接收方
 * @author xxm
 * @since 2024/3/27
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class AllocGroupReceiverManager extends BaseManager<AllocGroupReceiverMapper, AllocGroupReceiver> {

    /**
     * 判断接收者是否已经被使用
     */
    public boolean isUsed(Long receiverId){
        return existedByField(AllocGroupReceiver::getReceiverId, receiverId);
    }

    /**
     * 根据分组ID进行查询
     */
    public List<AllocGroupReceiver> findByGroupId(Long groupId){
        return findAllByField(AllocGroupReceiver::getGroupId, groupId);
    }

    /**
     * 根据分组ID进行批量删除
     */
    public void deleteByGroupId(Long groupId){
        deleteByField(AllocGroupReceiver::getGroupId, groupId);
    }

    /**
     * 判断是否存在分账接收者
     */

}
