package cn.bootx.platform.daxpay.service.core.order.reconcile.dao;

import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.common.query.generator.QueryGenerator;
import cn.bootx.platform.daxpay.service.core.order.reconcile.entity.ReconcileOrder;
import cn.bootx.platform.daxpay.service.param.reconcile.ReconcileOrderQuery;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

/**
 *
 * @author xxm
 * @since 2024/1/18
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class ReconcileOrderManager extends BaseManager<ReconcileOrderMapper, ReconcileOrder> {

    /**
     * 分页
     */
    public Page<ReconcileOrder> page(PageParam pageParam, ReconcileOrderQuery query){
        Page<ReconcileOrder> mpPage = MpUtil.getMpPage(pageParam, ReconcileOrder.class);
        QueryWrapper<ReconcileOrder> generator = QueryGenerator.generator(query);
        return this.page(mpPage,generator);
    }
}
