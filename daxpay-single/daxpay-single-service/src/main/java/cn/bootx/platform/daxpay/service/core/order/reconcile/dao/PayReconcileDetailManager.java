package cn.bootx.platform.daxpay.service.core.order.reconcile.dao;

import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.common.query.generator.QueryGenerator;
import cn.bootx.platform.daxpay.service.core.order.reconcile.entity.PayReconcileDetail;
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
public class PayReconcileDetailManager extends BaseManager<PayReconcileDetailMapper, PayReconcileDetail> {


    /**
     * 根据订单id查询
     */
    public List<PayReconcileDetail> findAllByOrderId(Long orderId){
        return this.findAllByField(PayReconcileDetail::getRecordOrderId, orderId);
    }

    /**
     * 分页
     */
    public Page<PayReconcileDetail> page(PageParam pageParam, ReconcileDetailQuery query){
        Page<PayReconcileDetail> mpPage = MpUtil.getMpPage(pageParam, PayReconcileDetail.class);
        QueryWrapper<PayReconcileDetail> generator = QueryGenerator.generator(query);
        return this.page(mpPage,generator);
    }
}
