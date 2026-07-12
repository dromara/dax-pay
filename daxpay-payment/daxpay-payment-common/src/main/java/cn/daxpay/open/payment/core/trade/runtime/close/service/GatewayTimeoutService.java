package cn.daxpay.open.payment.core.trade.runtime.close.service;

import cn.daxpay.open.payment.common.enums.GatewayOrderStatusEnum;
import cn.daxpay.open.payment.common.enums.PayTradeTypeEnum;
import cn.daxpay.open.payment.gateway.dao.GatewayPayOrderManager;
import cn.daxpay.open.payment.core.trade.order.dao.PayTradeManager;
import cn.daxpay.open.payment.gateway.entity.GatewayPayOrder;
import cn.daxpay.open.payment.core.trade.order.entity.PayTrade;
import cn.daxpay.open.payment.core.trade.runtime.service.pay.PayUniHandleService;
import com.baomidou.lock.LockInfo;
import com.baomidou.lock.LockTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/// # 网关支付超时关单服务
@Slf4j
@Service
@RequiredArgsConstructor
public class GatewayTimeoutService {

    private final GatewayPayOrderManager gatewayPayOrderManager;
    private final PayTradeManager payTradeManager;
    private final PayCloseService payCloseService;
    private final PayUniHandleService payUniHandleService;
    private final LockTemplate lockTemplate;

    /// 按网关单号超时关闭(幂等)
    public void closeForTimeout(String orderNo) {
        GatewayPayOrder order = gatewayPayOrderManager.findByOrderNoNotTenant(orderNo).orElse(null);
        if (order == null) {
            return;
        }
        if (!List.of(GatewayOrderStatusEnum.WAIT_PAY.getCode(), GatewayOrderStatusEnum.PAYING.getCode())
                .contains(order.getStatus())) {
            return;
        }
        LockInfo lock = lockTemplate.lock("payment:gateway:timeout:" + order.getId(), 10000, 50);
        if (lock == null) {
            log.warn("网关超时关单获取锁失败, orderNo={}", orderNo);
            return;
        }
        try {
            order = gatewayPayOrderManager.findByOrderNoNotTenant(orderNo).orElse(null);
            if (order == null
                    || !List.of(GatewayOrderStatusEnum.WAIT_PAY.getCode(), GatewayOrderStatusEnum.PAYING.getCode())
                    .contains(order.getStatus())) {
                return;
            }
            PayTrade trade = payTradeManager.findByContainerId(order.getId(), PayTradeTypeEnum.GATEWAY.getCode())
                    .orElse(null);
            if (trade != null) {
                // 有资金凭证: 走统一超时关单(含通道关闭)
                payCloseService.closeForTimeout(trade.getTradeNo());
            } else {
                // 仅容器: 直接过期
                payUniHandleService.gatewayOrderTimeout(order);
            }
        } finally {
            lockTemplate.releaseLock(lock);
        }
    }
}
