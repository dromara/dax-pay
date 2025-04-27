package org.dromara.daxpay.service.dao.reconcile;

import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.common.mybatisplus.query.generator.QueryGenerator;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.core.rest.param.PageParam;
import org.dromara.daxpay.service.entity.reconcile.ReconcileDiscrepancy;
import org.dromara.daxpay.service.param.reconcile.ReconcileDiscrepancyQuery;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

/**
 *
 * @author xxm
 * @since 2024/8/5
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class ReconcileDiscrepancyManager extends BaseManager<ReconcileDiscrepancyMapper, ReconcileDiscrepancy> {

    /**
     * 分页
     */
    public Page<ReconcileDiscrepancy> page(PageParam pageParam, ReconcileDiscrepancyQuery query){
        Page<ReconcileDiscrepancy> mpPage = MpUtil.getMpPage(pageParam, ReconcileDiscrepancy.class);
        QueryWrapper<ReconcileDiscrepancy> generator = QueryGenerator.generator(query);
        return this.page(mpPage,generator);
    }
}
