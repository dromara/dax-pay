package cn.daxpay.open.payment.trade.runtime.service.close;

import cn.daxpay.open.payment.common.context.PaymentContext;
import cn.daxpay.open.payment.common.enums.GatewayOrderStatusEnum;
import cn.daxpay.open.payment.common.enums.PayTradeTypeEnum;
import cn.daxpay.open.payment.trade.order.dao.GatewayPayOrderManager;
import cn.daxpay.open.payment.trade.order.dao.PayTradeManager;
import cn.daxpay.open.payment.trade.order.entity.GatewayPayOrder;
import cn.daxpay.open.payment.trade.order.entity.PayTrade;
import cn.daxpay.open.payment.trade.runtime.service.pay.PayUniHandleService;
import cn.hutool.core.util.StrUtil;
import com.baomidou.lock.LockInfo;
import com.baomidou.lock.LockTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/// # 网关支付超时关单服务
///
/// 引导读订单用 `*NotTenant`，随后仅 [PaymentContext#setMchNo] 装载身份（不校验商户启用，
/// 禁用商户的历史单仍需超时关），再走租户内查 Trade / 关单。
@Slf4j
@Service
@RequiredArgsConstructor
public class GatewayTimeoutService {

    private final GatewayPayOrderManager gatewayPayOrderManager;
    private final PayTradeManager payTradeManager;
    private final PayCloseService payCloseService;
    private final PayUniHandleService payUniHandleService;
    private final LockTemplate lockTemplate;
    private final PaymentContext paymentContext;

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
            GatewayPayOrder finalOrder = order;
            // 定时/MQ 无登录上下文：按订单 mchNo 开启作用域后走租户过滤
            paymentContext.runAs(() -> {
                if (StrUtil.isBlank(finalOrder.getMchNo())) {
                    log.error("网关超时关单订单缺少 mchNo, orderNo={}", orderNo);
                    return;
                }
                paymentContext.setMchNo(finalOrder.getMchNo());
                PayTrade trade = payTradeManager.findByContainerId(finalOrder.getId(), PayTradeTypeEnum.GATEWAY.getCode())
                        .orElse(null);
                if (trade != null) {
                    // 有资金凭证: 走统一超时关单(含通道关闭)
                    payCloseService.closeForTimeout(trade.getTradeNo());
                } else {
                    // 仅容器: 直接过期
                    payUniHandleService.gatewayOrderTimeout(finalOrder);
                }
            });
        } finally {
            lockTemplate.releaseLock(lock);
        }
    }
}
