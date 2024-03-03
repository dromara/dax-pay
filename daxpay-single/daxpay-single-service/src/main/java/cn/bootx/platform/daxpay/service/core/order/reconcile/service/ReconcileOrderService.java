package cn.bootx.platform.daxpay.service.core.order.reconcile.service;

import cn.bootx.platform.common.sequence.func.Sequence;
import cn.bootx.platform.daxpay.service.core.order.reconcile.dao.ReconcileOrderManager;
import cn.bootx.platform.daxpay.service.core.order.reconcile.entity.ReconcileOrder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * 支付对账订单服务
 * @author xxm
 * @since 2024/1/20
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ReconcileOrderService {
    private final ReconcileOrderManager reconcileOrderManager;
    private final Sequence sequence;

    /**
     * 更新, 开启一个新事务进行更新
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void update(ReconcileOrder order){
        reconcileOrderManager.updateById(order);
    }

    /**
     * 根据Id查询
     */
    public Optional<ReconcileOrder> findById(Long id){
        return reconcileOrderManager.findById(id);
    }

}
