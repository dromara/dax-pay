package cn.daxpay.single.service.core.order.reconcile.dao;

import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.common.query.generator.QueryGenerator;
import cn.daxpay.single.service.core.order.reconcile.entity.ReconcileTradeDetail;
import cn.daxpay.single.service.param.reconcile.ReconcileTradeDetailQuery;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 *
 * @author xxm
 * @since 2024/1/20
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class ReconcileTradeDetailManager extends BaseManager<ReconcileTradeDetailMapper, ReconcileTradeDetail> {


    /**
     * 根据订单id查询
     */
    public List<ReconcileTradeDetail> findAllByReconcileId(Long reconcileId){
        return this.findAllByField(ReconcileTradeDetail::getReconcileId, reconcileId);
    }

    /**
     * 分页
     */
    public Page<ReconcileTradeDetail> page(PageParam pageParam, ReconcileTradeDetailQuery query){
        Page<ReconcileTradeDetail> mpPage = MpUtil.getMpPage(pageParam, ReconcileTradeDetail.class);
        QueryWrapper<ReconcileTradeDetail> generator = QueryGenerator.generator(query);
        return this.page(mpPage,generator);
    }

}
