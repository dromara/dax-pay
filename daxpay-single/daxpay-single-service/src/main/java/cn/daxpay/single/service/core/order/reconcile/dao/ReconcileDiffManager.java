package cn.daxpay.single.service.core.order.reconcile.dao;

import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.common.query.generator.QueryGenerator;
import cn.daxpay.single.service.core.order.reconcile.entity.ReconcileDiff;
import cn.daxpay.single.service.param.reconcile.ReconcileDiffQuery;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 *
 * @author xxm
 * @since 2024/2/28
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class ReconcileDiffManager extends BaseManager<ReconcileDiffMapper, ReconcileDiff> {

    /**
     * 分页
     */
    public Page<ReconcileDiff> page(PageParam pageParam, ReconcileDiffQuery query){
        Page<ReconcileDiff> mpPage = MpUtil.getMpPage(pageParam, ReconcileDiff.class);
        QueryWrapper<ReconcileDiff> generator = QueryGenerator.generator(query);
        return this.page(mpPage,generator);
    }

    /**
     * 查询对账单对应的差异明细
     */
    public List<ReconcileDiff> findAllByReconcileId(Long reconcileId) {
        return this.findAllByField(ReconcileDiff::getReconcileId,reconcileId);
    }
}
