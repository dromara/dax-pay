package cn.daxpay.open.plugin.easypay.strategy;

import cn.daxpay.open.payment.strategy.plugin.AbsPayPluginStrategy;
import cn.daxpay.open.payment.trade.order.entity.PayRefundOrder;
import cn.daxpay.open.payment.trade.order.entity.PayTrade;
import cn.daxpay.open.platform.core.enums.pay.trade.TradeSourceEnum;
import cn.daxpay.open.plugin.easypay.service.order.EasyPayOrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

/// # 易支付插件生命周期策略
///
@Slf4j
@Service
@RequiredArgsConstructor
public class EasyPayPluginStrategy implements AbsPayPluginStrategy {

    private final EasyPayOrderService easyPayOrderService;

    @Override
    public void paySuccess(PayTrade trade) {
        if (!Objects.equals(TradeSourceEnum.EASY_PAY.getCode(), trade.getSource())) {
            return;
        }
        easyPayOrderService.paySuccess(trade);
    }

    @Override
    public void payClose(PayTrade trade) {
        if (!Objects.equals(TradeSourceEnum.EASY_PAY.getCode(), trade.getSource())) {
            return;
        }
        easyPayOrderService.payClose(trade);
    }

    @Override
    public void refundSuccess(PayTrade trade, PayRefundOrder refundOrder) {
        if (!Objects.equals(TradeSourceEnum.EASY_PAY.getCode(), trade.getSource())) {
            return;
        }
        long amount = refundOrder.getAmount() == null ? 0L : refundOrder.getAmount();
        easyPayOrderService.refundSuccess(trade, amount);
    }
}
