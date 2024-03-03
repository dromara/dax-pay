package cn.bootx.platform.daxpay.service.task.service;

import cn.bootx.platform.daxpay.service.core.order.reconcile.entity.ReconcileOrder;
import cn.bootx.platform.daxpay.service.core.payment.reconcile.service.ReconcileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

/**
 * 支付对账定时任务服务类
 * @author xxm
 * @since 2024/1/20
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ReconcileTaskService {
    private final ReconcileService reconcileService;

    /**
     * 执行任务
     */
    public void reconcileTask(LocalDate date, String channel){

        // 1. 查询需要定时对账的通道, 创建出来对账订单
        ReconcileOrder reconcileOrder = reconcileService.create(date, channel);

        // 2. 执行对账任务, 下载对账单并解析, 分别存储为原始数据和通用对账数据
        reconcileService.downAndSave(reconcileOrder);

        // 3. 执行账单比对, 生成差异单
        reconcileService.compare(reconcileOrder);
    }

}
