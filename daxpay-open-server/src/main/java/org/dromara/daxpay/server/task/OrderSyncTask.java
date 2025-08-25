package org.dromara.daxpay.server.task;

import cn.bootx.platform.core.util.DateTimeUtil;
import org.dromara.daxpay.core.enums.PayStatusEnum;
import org.dromara.daxpay.service.pay.dao.order.pay.PayOrderManager;
import org.dromara.daxpay.service.pay.dao.order.refund.RefundOrderManager;
import org.dromara.daxpay.service.pay.dao.order.transfer.TransferOrderManager;
import org.dromara.daxpay.service.pay.entity.order.pay.PayOrder;
import org.dromara.daxpay.service.pay.entity.order.refund.RefundOrder;
import org.dromara.daxpay.service.pay.entity.order.transfer.TransferOrder;
import org.dromara.daxpay.service.pay.service.assist.PaymentAssistService;
import org.dromara.daxpay.service.pay.service.trade.pay.PayAssistService;
import org.dromara.daxpay.service.pay.service.trade.pay.PayCloseService;
import org.dromara.daxpay.service.pay.service.trade.pay.PaySyncService;
import org.dromara.daxpay.service.pay.service.trade.refund.RefundSyncService;
import org.dromara.daxpay.service.pay.service.trade.transfer.TransferSyncService;
import cn.hutool.core.date.LocalDateTimeUtil;
import com.baomidou.lock.LockInfo;
import com.baomidou.lock.LockTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
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
public class OrderSyncTask {

    private final PayOrderManager payOrderManager;
    private final PaySyncService paySyncService;
    private final RefundOrderManager refundOrderManager;
    private final RefundSyncService refundSyncService;
    private final TransferOrderManager transferOrderManager;
    private final TransferSyncService transferSyncService;
    private final PaymentAssistService paymentAssistService;
    private final PayAssistService payAssistService;
    private final PayCloseService payCloseService;
    private final LockTemplate lockTemplate;

    /**
     * 支付单超时检测 一分钟一次
     */
    @Scheduled(cron = "0 */1 * * * ?")
    public void queryExpiredTask(){
        // 加锁
        LockInfo lock = lockTemplate.lock("task:queryExpiredTask",30000,200);
        if (Objects.isNull(lock)){
            log.info("检测任务存在锁，其他节点可能在执行中");
            return;
        }
        try {
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
        } finally {
            lockTemplate.releaseLock(lock);
        }
    }

    /**
     * 10分钟内一分钟一次
     */
    @Scheduled(cron = "0 */1 * * * ?")
    public void syncOrderBy10M(){
        // 加锁
        LockInfo lock = lockTemplate.lock("task:syncOrderBy10M",30000,200);
        if (Objects.isNull(lock)){
            log.info("检测任务存在锁，其他节点可能在执行中");
            return;
        }
        try {
            // 获取1分钟之前到10分钟之内的时间
            var now = LocalDateTime.now();
            var end = LocalDateTimeUtil.offset(now, -1, ChronoUnit.MINUTES);
            var start = LocalDateTimeUtil.offset(now, -10, ChronoUnit.MINUTES);
            List<PayOrder> orders = payOrderManager.queryExpiredOrderNotTenant(start, end);
            this.syncPayOrder(orders);
        } finally {
            lockTemplate.releaseLock(lock);
        }
    }

    /**
     * 1小时内10分钟一次
     */
    @Scheduled(cron = "0 */10 * * * ?")
    public void syncPayOrderBy1H(){
        // 加锁
        LockInfo lock = lockTemplate.lock("task:syncPayOrderBy1H",30000,200);
        if (Objects.isNull(lock)){
            log.info("检测任务存在锁，其他节点可能在执行中");
            return;
        }
        try {
            // 获取 10分钟之前到1小时内的时间
            var now = LocalDateTime.now();
            var end = LocalDateTimeUtil.offset(now, -10, ChronoUnit.MINUTES);
            var start = LocalDateTimeUtil.offset(now, -1, ChronoUnit.HOURS);
            List<PayOrder> orders = payOrderManager.queryExpiredOrderNotTenant(start,end);
            this.syncPayOrder(orders);
        } finally {
            lockTemplate.releaseLock(lock);
        }
    }

    /**
     * 1天内1小时一次
     */
    @Scheduled(cron = "0 0 */1 * * ?")
    public void syncPayOrderBy1D(){
        // 加锁
        LockInfo lock = lockTemplate.lock("task:syncPayOrderBy1D",30000,200);
        if (Objects.isNull(lock)){
            log.info("检测任务存在锁，其他节点可能在执行中");
            return;
        }
        try {
            // 获取 1小时之前到24小时内的时间
            var now = LocalDateTime.now();
            var end = LocalDateTimeUtil.offset(now, -1, ChronoUnit.HOURS);
            var start = LocalDateTimeUtil.offset(now, -1, ChronoUnit.DAYS);
            List<PayOrder> orders = payOrderManager.queryExpiredOrderNotTenant(start,end);
            this.syncPayOrder(orders);
        } finally {
            lockTemplate.releaseLock(lock);
        }
    }

    /**
     * 支付订单同步检测
     * 10分钟内一分钟一次
     * 一小时内10分钟一次
     * 一天内一小时一次
     */
    public void syncPayOrder(List<PayOrder> payOrders){
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
     * 10分钟内一分钟一次
     */
    @Scheduled(cron = "0 */1 * * * ?")
    public void refundSyncBy10M(){
        // 加锁
        LockInfo lock = lockTemplate.lock("task:refundSyncBy10M",30000,200);
        if (Objects.isNull(lock)){
            log.info("检测任务存在锁，其他节点可能在执行中");
            return;
        }
        try {
            // 获取1分钟之前到10分钟之内的时间
            var now = LocalDateTime.now();
            var end = LocalDateTimeUtil.offset(now, -1, ChronoUnit.MINUTES);
            var start = LocalDateTimeUtil.offset(now, -10, ChronoUnit.MINUTES);
            List<RefundOrder> list = refundOrderManager.findAllByProgress(start, end);
            this.refundSync(list);
        } finally {
            lockTemplate.releaseLock(lock);
        }
    }

    /**
     * 一小时内10分钟一次
     */
    @Scheduled(cron = "0 */10 * * * ?")
    public void refundSyncBy1H(){
        // 加锁
        LockInfo lock = lockTemplate.lock("task:refundSyncBy1H",30000,200);
        if (Objects.isNull(lock)){
            log.info("检测任务存在锁，其他节点可能在执行中");
            return;
        }
        try {
            // 获取 10分钟之前到1小时内的时间
            var now = LocalDateTime.now();
            var end = LocalDateTimeUtil.offset(now, -10, ChronoUnit.MINUTES);
            var start = LocalDateTimeUtil.offset(now, -1, ChronoUnit.HOURS);
            List<RefundOrder> list = refundOrderManager.findAllByProgress(start, end);
            this.refundSync(list);
        } finally {
            lockTemplate.releaseLock(lock);
        }
    }

    /**
     * 一天内一小时一次
     */
    @Scheduled(cron = "0 5 */1 * * ?")
    public void refundSyncBy1D(){
        // 加锁
        LockInfo lock = lockTemplate.lock("task:refundSyncBy1D",30000,200);
        if (Objects.isNull(lock)){
            log.info("检测任务存在锁，其他节点可能在执行中");
            return;
        }
        try {
            // 获取 1小时之前到24小时内的时间
            var now = LocalDateTime.now();
            var end = LocalDateTimeUtil.offset(now, -1, ChronoUnit.HOURS);
            var start = LocalDateTimeUtil.offset(now, -1, ChronoUnit.DAYS);
            List<RefundOrder> list = refundOrderManager.findAllByProgress(start, end);
            this.refundSync(list);
        } finally {
            lockTemplate.releaseLock(lock);
        }
    }

    /**
     * 超过一天一天一次
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void refundSyncByAll(){
        // 加锁
        LockInfo lock = lockTemplate.lock("task:refundSyncByAll",30000,200);
        if (Objects.isNull(lock)){
            log.info("检测任务存在锁，其他节点可能在执行中");
            return;
        }
        try {
            // 获取 1天之前到1天内的时间
            var now = LocalDateTime.now().plusDays(-1);
            List<RefundOrder> list = refundOrderManager.findAllByBeforeProgress(now);
            this.refundSync(list);
        } finally {
            lockTemplate.releaseLock(lock);
        }
    }

    /**
     * 退款定时同步任务
     * 10分钟内一分钟一次
     * 一小时内10分钟一次
     * 一天内一小时一次
     * 超过一天一天一次
     */
    public void refundSync(List<RefundOrder> list){
        // 查询退款中的退款订单
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
