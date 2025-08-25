package org.dromara.daxpay.core.util;

import cn.hutool.core.date.DatePattern;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 各类型订单号生成工具类
 *
 * 前缀(5)+业务类型(1)+机器码(2)+日期(14)+流水号(6)
 *
 * @author yxc
 * @since 2024/4/15
 */
@Slf4j
public class TradeNoGenerateUtil {

    private static final AtomicLong ATOMIC_LONG = new AtomicLong();
    private final static long ORDER_MAX_LIMIT = 999999L;
    /** 机器号 两位 */
    @Setter
    private static String machineNo;
    /** 环境前缀 最长五位 */
    @Setter
    private static String env;

    /**
     * 生成支付订单号
     */
    public static String pay() {
        StringBuilder orderNo = new StringBuilder();
        String dateStr = LocalDateTime.now().format(DatePattern.PURE_DATETIME_FORMATTER);
        long id = ATOMIC_LONG.incrementAndGet();
        orderNo.append(env).append("P").append(dateStr).append(machineNo).append(String.format("%06d", Math.abs(id) % ORDER_MAX_LIMIT));
        return orderNo.toString();
    }

    /**
     * 生成退款订单号
     */
    public static String refund() {
        StringBuilder orderNo = new StringBuilder();
        String dateStr = LocalDateTime.now().format(DatePattern.PURE_DATETIME_FORMATTER);
        long id = ATOMIC_LONG.incrementAndGet();
        orderNo.append(env).append("R").append(dateStr).append(machineNo).append(String.format("%06d", Math.abs(id) % ORDER_MAX_LIMIT));
        return orderNo.toString();
    }

    /**
     * 生成转账订单号
     */
    public static String transfer() {
        StringBuilder orderNo = new StringBuilder();
        String dateStr = LocalDateTime.now().format(DatePattern.PURE_DATETIME_FORMATTER);
        long id = ATOMIC_LONG.incrementAndGet();
        orderNo.append(env).append("T").append(dateStr).append(machineNo).append(String.format("%06d", Math.abs(id) % ORDER_MAX_LIMIT));
        return orderNo.toString();
    }

    /**
     * 生成分账订单号
     */
    public static String allocation() {
        StringBuilder orderNo = new StringBuilder();
        String dateStr = LocalDateTime.now().format(DatePattern.PURE_DATETIME_FORMATTER);
        long id = ATOMIC_LONG.incrementAndGet();
        orderNo.append(env).append("A").append(dateStr).append(machineNo).append(String.format("%06d", Math.abs(id) % ORDER_MAX_LIMIT));
        return orderNo.toString();
    }

    /**
     * 生成对账订单号
     */
    public static String reconciliation() {
        StringBuilder orderNo = new StringBuilder();
        String dateStr = LocalDateTime.now().format(DatePattern.PURE_DATETIME_FORMATTER);
        long id = ATOMIC_LONG.incrementAndGet();
        orderNo.append(env).append("C").append(dateStr).append(machineNo).append(String.format("%06d", Math.abs(id) % ORDER_MAX_LIMIT));
        return orderNo.toString();
    }

    /**
     * 生成修复单号
     */
    public static String repair() {
        StringBuilder orderNo = new StringBuilder();
        String dateStr = LocalDateTime.now().format(DatePattern.PURE_DATETIME_FORMATTER);
        long id = ATOMIC_LONG.incrementAndGet();
        orderNo.append(env).append("X").append(dateStr).append(machineNo).append(String.format("%06d", Math.abs(id) % ORDER_MAX_LIMIT));
        return orderNo.toString();
    }
}
