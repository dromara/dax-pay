package cn.bootx.platform.daxpay.service.core.order.reconcile.dao;

import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.common.query.generator.QueryGenerator;
import cn.bootx.platform.daxpay.service.core.order.reconcile.entity.ReconcileDetail;
import cn.bootx.platform.daxpay.service.param.reconcile.ReconcileDetailQuery;
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
public class ReconcileDetailManager extends BaseManager<ReconcileDetailMapper, ReconcileDetail> {


    /**
     * 根据订单id查询
     */
    public List<ReconcileDetail> findAllByOrderId(Long orderId){
        return this.findAllByField(ReconcileDetail::getRecordOrderId, orderId);
    }

    /**
     * 分页
     */
    public Page<ReconcileDetail> page(PageParam pageParam, ReconcileDetailQuery query){
        Page<ReconcileDetail> mpPage = MpUtil.getMpPage(pageParam, ReconcileDetail.class);
        QueryWrapper<ReconcileDetail> generator = QueryGenerator.generator(query);
        return this.page(mpPage,generator);
    }
}
