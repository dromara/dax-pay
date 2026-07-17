package cn.daxpay.open.plugin.easypay.strategy;

import cn.daxpay.open.payment.strategy.plugin.AbsPayPluginStrategy;
import cn.daxpay.open.payment.trade.order.entity.RefundOrder;
import cn.daxpay.open.payment.trade.order.entity.PayTrade;
import cn.daxpay.open.platform.core.enums.pay.trade.TradeSourceEnum;
import cn.daxpay.open.plugin.easypay.service.order.EasyPayOrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

/// # 易支付插件生命周期策略
///
/// 仅处理来源为易支付协议（TradeSourceEnum.EASY_PAY）的交易钩子
@Slf4j
@Service
@RequiredArgsConstructor
public class EasyPayPluginStrategy implements AbsPayPluginStrategy {

    private final EasyPayOrderService easyPayOrderService;

    /// 支付成功：回写协议单状态
    @Override
    public void paySuccess(PayTrade trade) {
        if (!Objects.equals(TradeSourceEnum.EASY_PAY.getCode(), trade.getSource())) {
            return;
        }
        easyPayOrderService.paySuccess(trade);
    }

    /// 关单：一期仅日志
    @Override
    public void payClose(PayTrade trade) {
        if (!Objects.equals(TradeSourceEnum.EASY_PAY.getCode(), trade.getSource())) {
            return;
        }
        easyPayOrderService.payClose(trade);
    }

    /// 退款成功：累加协议单已退金额
    @Override
    public void refundSuccess(PayTrade trade, RefundOrder refundOrder) {
        if (!Objects.equals(TradeSourceEnum.EASY_PAY.getCode(), trade.getSource())) {
            return;
        }
        long amount = refundOrder.getAmount() == null ? 0L : refundOrder.getAmount();
        easyPayOrderService.refundSuccess(trade, amount);
    }
}
