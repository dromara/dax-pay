package org.dromara.daxpay.service.dao.allocation.transaction;

import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.common.mybatisplus.query.generator.QueryGenerator;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.core.rest.param.PageParam;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.daxpay.service.common.entity.MchAppBaseEntity;
import org.dromara.daxpay.service.entity.allocation.transaction.AllocTransaction;
import org.dromara.daxpay.service.param.order.allocation.AllocOrderQuery;
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
     * 分页
     */
    public Page<AllocTransaction> page(PageParam pageParam, AllocOrderQuery param){
        Page<AllocTransaction> mpPage = MpUtil.getMpPage(pageParam, AllocTransaction.class);
        QueryWrapper<AllocTransaction> generator = QueryGenerator.generator(param);
        return this.page(mpPage, generator);
    }

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
