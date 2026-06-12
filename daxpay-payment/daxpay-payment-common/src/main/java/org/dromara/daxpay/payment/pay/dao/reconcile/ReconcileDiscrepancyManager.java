package org.dromara.daxpay.payment.pay.dao.reconcile;

import org.dromara.daxpay.platform.common.mybatisplus.impl.BaseManager;
import org.dromara.daxpay.platform.common.mybatisplus.query.generator.QueryGenerator;
import org.dromara.daxpay.platform.common.mybatisplus.util.MpUtil;
import org.dromara.daxpay.platform.core.rest.param.PageParam;
import org.dromara.daxpay.payment.pay.entity.reconcile.ReconcileDiscrepancy;
import org.dromara.daxpay.payment.pay.param.reconcile.ReconcileDiscrepancyQuery;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;


@Slf4j
@Repository
@RequiredArgsConstructor
public class ReconcileDiscrepancyManager extends BaseManager<ReconcileDiscrepancyMapper, ReconcileDiscrepancy> {

    /// 分页
    public Page<ReconcileDiscrepancy> page(PageParam pageParam, ReconcileDiscrepancyQuery query){
        Page<ReconcileDiscrepancy> mpPage = MpUtil.getMpPage(pageParam, ReconcileDiscrepancy.class);
        QueryWrapper<ReconcileDiscrepancy> generator = QueryGenerator.generator(query);
        return this.page(mpPage,generator);
    }
}
