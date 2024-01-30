package cn.bootx.platform.daxpay.service.core.order.refund.service;

import cn.bootx.platform.common.core.exception.DataNotExistException;
import cn.bootx.platform.daxpay.result.pay.PaySyncResult;
import cn.bootx.platform.daxpay.service.core.order.refund.dao.PayRefundOrderManager;
import cn.bootx.platform.daxpay.service.core.order.refund.entity.PayRefundOrder;
import cn.bootx.platform.daxpay.service.core.payment.sync.service.PayRefundSyncService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 退款订单服务类
 * @author xxm
 * @since 2024/1/29
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PayRefundOrderService {
    private final PayRefundOrderManager refundOrderManager;

    private final PayRefundSyncService refundSyncService;;

    /**
     * 退款同步
     */
    public PaySyncResult syncById(Long id){
        PayRefundOrder refundOrder = refundOrderManager.findById(id)
                .orElseThrow(() -> new DataNotExistException("退款订单不存在"));
        return refundSyncService.syncRefundOrder(refundOrder);
    }

}
