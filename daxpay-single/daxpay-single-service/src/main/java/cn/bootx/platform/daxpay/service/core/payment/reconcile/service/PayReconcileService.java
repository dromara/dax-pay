package cn.bootx.platform.daxpay.service.core.payment.reconcile.service;

import cn.bootx.platform.common.core.exception.DataNotExistException;
import cn.bootx.platform.daxpay.code.PayChannelEnum;
import cn.bootx.platform.daxpay.exception.pay.PayFailureException;
import cn.bootx.platform.daxpay.service.common.local.PaymentContextLocal;
import cn.bootx.platform.daxpay.service.core.order.reconcile.dao.PayReconcileDetailManager;
import cn.bootx.platform.daxpay.service.core.order.reconcile.dao.PayReconcileOrderManager;
import cn.bootx.platform.daxpay.service.core.order.reconcile.entity.PayReconcileDetail;
import cn.bootx.platform.daxpay.service.core.order.reconcile.entity.PayReconcileOrder;
import cn.bootx.platform.daxpay.service.core.order.reconcile.service.PayReconcileOrderService;
import cn.bootx.platform.daxpay.service.core.payment.reconcile.factory.PayReconcileStrategyFactory;
import cn.bootx.platform.daxpay.service.func.AbsReconcileStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
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
    private final PayReconcileOrderManager reconcileOrderManager;
    private final PayReconcileOrderService reconcileOrderService;
    private final PayReconcileDetailManager reconcileDetailManager;

    /**
     * 创建对账订单
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public PayReconcileOrder create(LocalDate date, String channel) {

        // 获取通道枚举
        PayChannelEnum channelEnum = PayChannelEnum.findByCode(channel);
        // 判断是否为为异步通道
        if (!PayChannelEnum.ASYNC_TYPE.contains(channelEnum)){
            throw new PayFailureException("不支持对账的通道, 请检查");
        }
        // 构建对账策略
        AbsReconcileStrategy reconcileStrategy = PayReconcileStrategyFactory.create(channel);

        // 生成批次号
        String seqNo = reconcileStrategy.generateSequence(date);

        PayReconcileOrder order = new PayReconcileOrder()
                .setBatchNo(seqNo)
                .setChannel(channel)
                .setDate(date);
        reconcileOrderManager.save(order);
        return order;
    }

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
    public void downAndSave(PayReconcileOrder reconcileOrder) {
        // 构建对账策略
        AbsReconcileStrategy reconcileStrategy = PayReconcileStrategyFactory.create(reconcileOrder.getChannel());
        reconcileStrategy.setRecordOrder(reconcileOrder);
        reconcileStrategy.doBeforeHandler();
        try {
            reconcileStrategy.downAndSave();
            reconcileOrder.setDown(true);
            reconcileOrderService.update(reconcileOrder);
        } catch (Exception e) {
            log.error("下载对账单异常", e);
            reconcileOrder.setErrorMsg("原因: " + e.getMessage());
            reconcileOrderService.update(reconcileOrder);
            throw new RuntimeException(e);
        }
        // 保存转换后的通用结构对账单
        List<PayReconcileDetail> reconcileDetails = PaymentContextLocal.get()
                .getReconcileInfo()
                .getReconcileDetails();
        reconcileDetailManager.saveAll(reconcileDetails);
    }

    /**
     * 对账单比对
     */
    public void compare(Long reconcileOrderId){
        PayReconcileOrder payReconcileOrder = reconcileOrderService.findById(reconcileOrderId)
                .orElseThrow(() -> new DataNotExistException("未找到对账订单"));
        this.compare(payReconcileOrder);
    }

    /**
     * 对账单比对
     */
    public void compare(PayReconcileOrder reconcileOrder){
        // 判断是否已经下载了对账单明细
        if (!reconcileOrder.isDown()){
            throw new PayFailureException("请先下载对账单");
        }

        // 查询对账单
        List<PayReconcileDetail> reconcileDetails = reconcileDetailManager.findAllByOrderId(reconcileOrder.getId());
        // 获取通道枚举
        if (!PayChannelEnum.ASYNC_TYPE_CODE.contains(reconcileOrder.getChannel())){
            log.error("不支持对账的通道, 请检查, 对账订单ID: {}", reconcileOrder.getId());
            throw new PayFailureException("不支持对账的通道, 请检查");
        }
        // 判断是否为为异步通道
        // 构建对账策略
        AbsReconcileStrategy reconcileStrategy = PayReconcileStrategyFactory.create(reconcileOrder.getChannel());
        // 初始化参数
        reconcileStrategy.setRecordOrder(reconcileOrder);
        reconcileStrategy.setReconcileDetails(reconcileDetails);
        try {
            // 执行比对任务
            reconcileStrategy.compare();
//            reconcileOrder.setCompare(true);
//            reconcileOrderService.update(reconcileOrder);
        } catch (Exception e) {
            log.error("比对对账单异常", e);
            reconcileOrder.setErrorMsg("原因: " + e.getMessage());
            reconcileOrderService.update(reconcileOrder);
            throw new RuntimeException(e);
        }
    }
}
