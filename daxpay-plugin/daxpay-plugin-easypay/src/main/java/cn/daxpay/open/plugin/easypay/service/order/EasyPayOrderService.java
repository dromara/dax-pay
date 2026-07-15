package cn.daxpay.open.plugin.easypay.service.order;

import cn.daxpay.open.payment.trade.order.dao.NormalPayOrderManager;
import cn.daxpay.open.payment.trade.order.entity.PayTrade;
import cn.daxpay.open.plugin.easypay.dao.EasyPayOrderManager;
import cn.daxpay.open.plugin.easypay.entity.EasyPayOrder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

/// # 易支付订单服务
///
@Slf4j
@Service
@RequiredArgsConstructor
public class EasyPayOrderService {

    private final EasyPayOrderManager easyPayOrderManager;
    private final NormalPayOrderManager normalPayOrderManager;

    /// 支付成功回写协议单
    public EasyPayOrder paySuccess(PayTrade trade) {
        if (trade.getContainerId() == null) {
            return null;
        }
        var optional = easyPayOrderManager.findByOrderIdNotTenant(trade.getContainerId());
        if (optional.isEmpty()) {
            // 收银台中间态可能仅有 outTradeNo 关联
            var normal = normalPayOrderManager.findById(trade.getContainerId()).orElse(null);
            if (normal != null) {
                optional = easyPayOrderManager.findByOutTradeNo(normal.getBizOrderNo());
            }
        }
        if (optional.isEmpty()) {
            log.warn("易支付订单不存在, containerId={}, tradeNo={}", trade.getContainerId(), trade.getTradeNo());
            return null;
        }
        var easyPayOrder = optional.get();
        String buyer = null;
        var normal = normalPayOrderManager.findById(trade.getContainerId()).orElse(null);
        if (normal != null) {
            buyer = normal.getBuyerId();
            if (easyPayOrder.getTradeNo() == null) {
                easyPayOrder.setTradeNo(normal.getOrderNo());
            }
            if (easyPayOrder.getOrderId() == null) {
                easyPayOrder.setOrderId(normal.getId());
            }
        }
        easyPayOrder.setStatus(1)
                .setEndTime(trade.getPayTime() != null ? trade.getPayTime() : OffsetDateTime.now(ZoneOffset.UTC))
                .setApiTradeNo(trade.getOutOrderNo())
                .setBuyer(buyer);
        easyPayOrderManager.updateById(easyPayOrder);
        return easyPayOrder;
    }

    /// 退款成功累加已退金额
    public void refundSuccess(PayTrade trade, long refundAmountFen) {
        if (trade.getContainerId() == null) {
            return;
        }
        var optional = easyPayOrderManager.findByOrderIdNotTenant(trade.getContainerId());
        if (optional.isEmpty()) {
            return;
        }
        var order = optional.get();
        BigDecimal add = BigDecimal.valueOf(refundAmountFen)
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        BigDecimal current = order.getRefundMoney() == null ? BigDecimal.ZERO : order.getRefundMoney();
        order.setRefundMoney(current.add(add));
        easyPayOrderManager.updateById(order);
    }

    /// 关单回写（协议层仍保留待付/失败语义，status 保持 0）
    public void payClose(PayTrade trade) {
        // 一期仅日志，协议 query 以内核状态补齐
        log.debug("易支付关单钩子, tradeNo={}", trade.getTradeNo());
    }
}
