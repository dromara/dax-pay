package org.dromara.daxpay.payment.pay.dao.reconcile;

import org.dromara.daxpay.platform.common.mybatisplus.impl.BaseManager;
import org.dromara.daxpay.platform.common.mybatisplus.query.generator.QueryGenerator;
import org.dromara.daxpay.platform.common.mybatisplus.util.MpUtil;
import org.dromara.daxpay.platform.core.rest.param.PageParam;
import org.dromara.daxpay.payment.pay.entity.reconcile.ReconcileStatement;
import org.dromara.daxpay.payment.pay.param.reconcile.ReconcileStatementQuery;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;


@Slf4j
@Repository
@RequiredArgsConstructor
public class ReconcileStatementManager extends BaseManager<ReconcileStatementMapper, ReconcileStatement> {

    /// 分页
    public Page<ReconcileStatement> page(PageParam pageParam, ReconcileStatementQuery query){
        Page<ReconcileStatement> mpPage = MpUtil.getMpPage(pageParam, ReconcileStatement.class);
        QueryWrapper<ReconcileStatement> generator = QueryGenerator.generator(query);
        return this.page(mpPage,generator);
    }

    /// 根据日期查询
    public List<ReconcileStatement> findByProductAndData(String product, LocalDate date){
        return this.lambdaQuery()
                .eq(ReconcileStatement::getDate,date)
                .eq(ReconcileStatement::getProduct,product)
                .list();
    }
}
