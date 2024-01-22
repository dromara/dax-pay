package cn.bootx.platform.daxpay.service.core.order.reconcile.service;

import cn.bootx.platform.common.core.exception.DataNotExistException;
import cn.bootx.platform.common.core.rest.PageResult;
import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.daxpay.service.core.order.reconcile.dao.PayReconcileDetailManager;
import cn.bootx.platform.daxpay.service.core.order.reconcile.dao.PayReconcileOrderManager;
import cn.bootx.platform.daxpay.service.core.order.reconcile.entity.PayReconcileDetail;
import cn.bootx.platform.daxpay.service.core.order.reconcile.entity.PayReconcileOrder;
import cn.bootx.platform.daxpay.service.dto.order.reconcile.PayReconcileDetailDto;
import cn.bootx.platform.daxpay.service.dto.order.reconcile.PayReconcileOrderDto;
import cn.bootx.platform.daxpay.service.param.reconcile.ReconcileDetailQuery;
import cn.bootx.platform.daxpay.service.param.reconcile.ReconcileOrderQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 支付对账订单查询服务类
 * @author xxm
 * @since 2024/1/21
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PayReconcileQueryService {
    private final PayReconcileOrderManager orderManager;
    private final PayReconcileDetailManager detailManager;

    /**
     * 分页
     */
    public PageResult<PayReconcileOrderDto> page(PageParam pageParam, ReconcileOrderQuery query){
        return MpUtil.convert2DtoPageResult(orderManager.page(pageParam, query));
    }

    /**
     * 对账订单
     */
    public PayReconcileOrderDto findById(Long id){
        return orderManager.findById(id).map(PayReconcileOrder::toDto)
                .orElseThrow(()->new DataNotExistException("对账订单不存在"));
    }

    /**
     * 明细分页
     */
    public PageResult<PayReconcileDetailDto> pageDetail(PageParam pageParam, ReconcileDetailQuery query){
        return MpUtil.convert2DtoPageResult(detailManager.page(pageParam, query));
    }

    /**
     * 明细详情
     */
    public PayReconcileDetailDto findDetailById(Long  id){
        return detailManager.findById(id).map(PayReconcileDetail::toDto)
                .orElseThrow(()->new DataNotExistException("对账详情不存在"));
    }

}
