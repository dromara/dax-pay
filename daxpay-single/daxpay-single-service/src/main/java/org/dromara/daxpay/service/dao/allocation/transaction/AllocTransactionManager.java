package org.dromara.daxpay.service.dao.allocation.transaction;

import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.daxpay.service.common.entity.MchAppBaseEntity;
import org.dromara.daxpay.service.entity.allocation.transaction.AllocTransaction;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 分账交易
 * @author xxm
 * @since 2024/11/14
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class AllocTransactionManager extends BaseManager<AllocTransactionMapper, AllocTransaction> {
    /**
     * 根据商户分账单号查询数据
     */
    public Optional<AllocTransaction> findByBizAllocNo(String bizAllocNo, String appId) {
        return lambdaQuery()
                .eq(AllocTransaction::getBizAllocNo, bizAllocNo)
                .eq(MchAppBaseEntity::getAppId, appId)
                .oneOpt();
    }

    /**
     * 根据分账单号查询数据
     */
    public Optional<AllocTransaction> findByAllocNo(String allocNo, String appId) {
        return lambdaQuery()
                .eq(AllocTransaction::getAllocNo, allocNo)
                .eq(MchAppBaseEntity::getAppId, appId)
                .oneOpt();
    }
}
