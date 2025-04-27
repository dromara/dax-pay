package org.dromara.daxpay.service.task;

import cn.bootx.platform.core.util.DateTimeUtil;
import org.dromara.daxpay.core.enums.PayStatusEnum;
import org.dromara.daxpay.service.dao.order.pay.PayOrderManager;
import org.dromara.daxpay.service.dao.order.refund.RefundOrderManager;
import org.dromara.daxpay.service.dao.order.transfer.TransferOrderManager;
import org.dromara.daxpay.service.entity.order.pay.PayOrder;
import org.dromara.daxpay.service.entity.order.refund.RefundOrder;
import org.dromara.daxpay.service.entity.order.transfer.TransferOrder;
import org.dromara.daxpay.service.service.assist.PaymentAssistService;
import org.dromara.daxpay.service.service.trade.pay.PayAssistService;
import org.dromara.daxpay.service.service.trade.pay.PayCloseService;
import org.dromara.daxpay.service.service.trade.pay.PaySyncService;
import org.dromara.daxpay.service.service.trade.refund.RefundSyncService;
import org.dromara.daxpay.service.service.trade.transfer.TransferSyncService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * 交易订单同步定时任务
 * @author xxm
 * @since 2024/8/29
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OrderSyncTaskService {

    private final PayOrderManager payOrderManager;
    private final PaySyncService paySyncService;
    private final RefundOrderManager refundOrderManager;
    private final RefundSyncService refundSyncService;
    private final TransferOrderManager transferOrderManager;
    private final TransferSyncService transferSyncService;
    private final PaymentAssistService paymentAssistService;
    private final PayAssistService payAssistService;
    private final PayCloseService payCloseService;

    /**
     * 支付单超时检测 一分钟一次, 查询支付
     */
    @Scheduled(cron = "0 */1 * * * ?")
    public void queryExpiredTask(){
        // 从数据库查询获取超时的任务对象
        List<PayOrder> payOrders = payOrderManager.queryExpiredOrderNotTenant();
        for (PayOrder order : payOrders) {
            try {
                paymentAssistService.initMchAndApp(order.getMchNo(),order.getAppId());
                if (!List.of(PayStatusEnum.WAIT.getCode(),PayStatusEnum.TIMEOUT.getCode()).contains(order.getStatus())){
                    paySyncService.syncPayOrder(order);
                } else {
                    // 判断是超时走关闭订单
                    if (Objects.nonNull(order.getExpiredTime()) && DateTimeUtil.ge(LocalDateTime.now(), order.getExpiredTime())) {
                        payCloseService.closeOrder(order,false);
                    }
                }
            } catch (Exception e) {
                log.error("超时取消任务异常, ID: {}, 订单号: {}",order.getId(), order.getOrderNo(), e);
                // 设置订单任务为失败
                payAssistService.fail(order, e.getMessage());
            }
        }
    }


    /**
     * 退款定时同步任务 一分钟一次, 查询一分钟之前退款中的订单进行同步
     * 10分钟内一分钟一次
     * 一天内一小时一次
     * 超过一天一天一次
     *
     */
    @Scheduled(cron = "0 */1 * * * ?")
    public void refundSyncTask(){
        // 查询退款中的退款订单
        List<RefundOrder> list = refundOrderManager.findAllByProgress();
        for (RefundOrder refundOrder : list) {
            try {
                // 调用同步方法
                paymentAssistService.initMchAndApp(refundOrder.getMchNo(),refundOrder.getAppId());
                refundSyncService.syncRefundOrder(refundOrder);
            } catch (Exception e) {
                log.warn("退款执行同步失败, ID: {}, 退款号: {}",refundOrder.getId(), refundOrder.getRefundNo(), e);
            }
        }
    }

    /**
     * 转账订单同步, 一分钟一次, 获取一分钟之前转账中的订单
     */
    @Scheduled(cron = "0 */1 * * * ?")
    public void transferSyncTask(){
        List<TransferOrder> list = transferOrderManager.findAllByProgress();
        for (var transferOrder : list) {
            try {
                // 调用同步方法
                paymentAssistService.initMchAndApp(transferOrder.getMchNo(),transferOrder.getAppId());
                transferSyncService.syncTransferOrder(transferOrder);
            } catch (Exception e) {
                log.warn("转账执行同步失败, ID: {}, 转账号: {}",transferOrder.getId(),transferOrder.getTransferNo(), e);
            }
        }
    }
}
