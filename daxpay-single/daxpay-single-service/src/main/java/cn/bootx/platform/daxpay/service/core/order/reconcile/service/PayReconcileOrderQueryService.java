package cn.bootx.platform.daxpay.service.core.order.reconcile.service;

import cn.bootx.platform.common.core.rest.PageResult;
import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.daxpay.service.core.order.reconcile.dao.PayReconcileDetailManager;
import cn.bootx.platform.daxpay.service.core.order.reconcile.dao.PayReconcileOrderManager;
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
public class PayReconcileOrderQueryService {
    private final PayReconcileOrderManager orderManager;
    private final PayReconcileDetailManager detailManager;

    /**
     * 分页
     */
    public PageResult<?> page(PageParam pageParam){
        return null;
    }

    /**
     * 对账订单详情
     */
    public void findById(Long id){

    }

    /**
     * 明细分页
     */
    public PageResult<?> pageDetail(PageParam pageParam, Long orderId){
        return null;
    }

    /**
     * 明细详情
     */
    public void findDetailById(Long  id){

    }

}
