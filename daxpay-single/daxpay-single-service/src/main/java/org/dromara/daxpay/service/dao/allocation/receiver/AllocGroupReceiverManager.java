package org.dromara.daxpay.service.dao.allocation.receiver;

import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.daxpay.service.entity.allocation.receiver.AllocGroupReceiver;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 *
 * @author xxm
 * @since 2024/10/6
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
