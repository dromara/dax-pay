package cn.daxpay.open.payment.trade.runtime.close.job;

import cn.daxpay.open.payment.gateway.dao.GatewayPayOrderManager;
import cn.daxpay.open.payment.trade.order.dao.PayTradeManager;
import cn.daxpay.open.payment.gateway.entity.GatewayPayOrder;
import cn.daxpay.open.payment.trade.order.entity.PayTrade;
import cn.daxpay.open.payment.trade.runtime.close.service.GatewayTimeoutService;
import cn.daxpay.open.payment.trade.runtime.close.service.PayCloseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

/// # 网关支付超时关单兜底定时任务
@Slf4j
@Component
@RequiredArgsConstructor
public class GatewayTimeoutJob {

    private final GatewayPayOrderManager gatewayPayOrderManager;
    private final PayTradeManager payTradeManager;
    private final GatewayTimeoutService gatewayTimeoutService;
    private final PayCloseService payCloseService;

    @Scheduled(fixedDelay = 300_000L)
    public void scanTimeoutOrders() {
        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
        // 1. 容器层超时(含尚未建 Trade 的 wait_pay)
        List<GatewayPayOrder> timeoutOrders = gatewayPayOrderManager.findTimeoutOrders(now);
        if (!timeoutOrders.isEmpty()) {
            log.info("网关超时关单兜底扫描命中容器 {} 笔", timeoutOrders.size());
            for (GatewayPayOrder order : timeoutOrders) {
                try {
                    gatewayTimeoutService.closeForTimeout(order.getOrderNo());
                } catch (Exception e) {
                    log.error("网关超时关单兜底失败, orderNo={}", order.getOrderNo(), e);
                }
            }
        }
        // 2. Trade 层超时(processing)
        List<PayTrade> timeoutTrades = payTradeManager.findGatewayTimeoutTrades(now);
        if (!timeoutTrades.isEmpty()) {
            log.info("网关超时关单兜底扫描命中交易 {} 笔", timeoutTrades.size());
            for (PayTrade trade : timeoutTrades) {
                try {
                    payCloseService.closeForTimeout(trade.getTradeNo());
                } catch (Exception e) {
                    log.error("网关交易超时关单兜底失败, tradeNo={}", trade.getTradeNo(), e);
                }
            }
        }
    }
}
