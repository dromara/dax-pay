package org.dromara.daxpay.service.dao.allocation.transaction;

import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.daxpay.service.entity.allocation.transaction.AllocDetail;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 分账明细
 * @author xxm
 * @since 2024/11/14
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class AllocDetailManager extends BaseManager<AllocDetailMapper, AllocDetail> {
    public List<AllocDetail> findAllByOrderId(Long id) {
        return findAllByField(AllocDetail::getAllocationId, id);
    }
}
