package cn.bootx.platform.daxpay.service.core.payment.reconcile.service;

import cn.bootx.platform.common.core.exception.DataNotExistException;
import cn.bootx.platform.daxpay.service.common.context.PaymentContext;
import cn.bootx.platform.daxpay.service.common.local.PaymentContextLocal;
import cn.bootx.platform.daxpay.service.core.order.reconcile.dao.PayReconcileDetailManager;
import cn.bootx.platform.daxpay.service.core.order.reconcile.entity.PayReconcileDetail;
import cn.bootx.platform.daxpay.service.core.order.reconcile.entity.PayReconcileOrder;
import cn.bootx.platform.daxpay.service.core.order.reconcile.service.PayReconcileOrderService;
import cn.bootx.platform.daxpay.service.core.payment.reconcile.factory.PayReconcileStrategyFactory;
import cn.bootx.platform.daxpay.service.func.AbsReconcileStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

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
    private final PayReconcileDetailManager reconcileDetailManager;

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
        // 初始化上下文
        PaymentContextLocal.setIfAbsent(new PaymentContext());
        // 构建对账策略
        AbsReconcileStrategy absReconcileStrategy = PayReconcileStrategyFactory.create(recordOrder.getChannel());
        absReconcileStrategy.initParam(recordOrder);
        absReconcileStrategy.doBeforeHandler();
        try {
            absReconcileStrategy.downAndSave();
            recordOrder.setDown(true);
            reconcileOrderService.update(recordOrder);
        } catch (Exception e) {
            log.error("下载对账单异常", e);
            recordOrder.setErrorMsg("原因: " + e.getMessage());
            reconcileOrderService.update(recordOrder);
            throw new RuntimeException(e);
        }
        // 保存转换后的通用结构对账单
        List<PayReconcileDetail> reconcileDetails = PaymentContextLocal.get()
                .getReconcile()
                .getReconcileDetails();
        reconcileDetailManager.saveAll(reconcileDetails);
    }

}
