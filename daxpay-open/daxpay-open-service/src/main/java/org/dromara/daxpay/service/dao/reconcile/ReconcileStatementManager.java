package org.dromara.daxpay.service.dao.reconcile;

import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.common.mybatisplus.query.generator.QueryGenerator;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.core.rest.param.PageParam;
import org.dromara.daxpay.service.entity.reconcile.ReconcileStatement;
import org.dromara.daxpay.service.param.reconcile.ReconcileStatementQuery;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author xxm
 * @since 2024/8/5
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class ReconcileStatementManager extends BaseManager<ReconcileStatementMapper, ReconcileStatement> {

    /**
     * 分页
     */
    public Page<ReconcileStatement> page(PageParam pageParam, ReconcileStatementQuery query){
        Page<ReconcileStatement> mpPage = MpUtil.getMpPage(pageParam, ReconcileStatement.class);
        QueryWrapper<ReconcileStatement> generator = QueryGenerator.generator(query);
        return this.page(mpPage,generator);
    }

    /**
     * 根据日期查询
     */
    public List<ReconcileStatement> findByChannelAndData(String channel, LocalDate date){
        return this.lambdaQuery()
                .eq(ReconcileStatement::getDate,date)
                .eq(ReconcileStatement::getChannel,channel)
                .list();
    }
}
