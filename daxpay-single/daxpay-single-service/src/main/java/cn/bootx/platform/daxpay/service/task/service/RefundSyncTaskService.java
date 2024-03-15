package cn.bootx.platform.daxpay.service.task.service;

import cn.bootx.platform.daxpay.service.core.order.refund.dao.RefundOrderManager;
import cn.bootx.platform.daxpay.service.core.order.refund.entity.RefundOrder;
import cn.bootx.platform.daxpay.service.core.payment.sync.service.RefundSyncService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 定时
 * @author xxm
 * @since 2024/3/12
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RefundSyncTaskService {

    private final RefundSyncService refundSyncService;

    private final RefundOrderManager refundOrderManager;

    /**
     * 同步任务
     */
    public void syncTask(){
        // 查询退款中的退款订单
        List<RefundOrder> list = refundOrderManager.findAllByProgress();
        for (RefundOrder refundOrder : list) {
            try {
                // 调用同步方法
                refundSyncService.syncRefundOrder(refundOrder);
            } catch (Exception e) {
                log.warn("退款执行同步失败, ID: {}",refundOrder.getId());
                log.warn("退款执行同步失败",e);
            }
        }
    }
}
