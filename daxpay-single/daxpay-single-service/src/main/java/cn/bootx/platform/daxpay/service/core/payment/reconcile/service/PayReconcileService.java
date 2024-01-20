package cn.bootx.platform.daxpay.service.core.payment.reconcile.service;

import cn.bootx.platform.common.core.exception.DataNotExistException;
import cn.bootx.platform.daxpay.service.core.order.reconcile.entity.PayReconcileOrder;
import cn.bootx.platform.daxpay.service.core.order.reconcile.service.PayReconcileOrderService;
import cn.bootx.platform.daxpay.service.core.payment.reconcile.factory.PayReconcileStrategyFactory;
import cn.bootx.platform.daxpay.service.func.AbsReconcileStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 支付对账单下载服务
 * @author xxm
 * @since 2024/1/17
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PayReconcileService {
    private final PayReconcileOrderService reconcileOrderService;

    /**
     * 下载对账单并进行保存
     */
    public void downAndSave(Long reconcileOrderId) {
        PayReconcileOrder payReconcileOrder = reconcileOrderService.findById(reconcileOrderId)
                .orElseThrow(() -> new DataNotExistException("未找到对账订单"));
        this.downAndSave(payReconcileOrder);
    }

    /**
     * 下载对账单并进行保存
     */
    public void downAndSave(PayReconcileOrder recordOrder) {
        AbsReconcileStrategy absReconcileStrategy = PayReconcileStrategyFactory.create(recordOrder.getChannel());
        absReconcileStrategy.initParam(recordOrder);
        absReconcileStrategy.doBeforeHandler();
        try {
            absReconcileStrategy.downAndSave();
            recordOrder.setDown(true);
            reconcileOrderService.update(recordOrder);
        } catch (Exception e) {
            log.error("下载对账单异常", e);
            recordOrder.setErrorMsg(e.getMessage());
            reconcileOrderService.update(recordOrder);
            throw new RuntimeException(e);
        }
    }

}
