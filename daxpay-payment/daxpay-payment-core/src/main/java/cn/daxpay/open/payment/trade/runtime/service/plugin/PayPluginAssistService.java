package cn.daxpay.open.payment.trade.runtime.service.plugin;

import cn.daxpay.open.payment.strategy.plugin.AbsPayPluginStrategy;
import cn.daxpay.open.payment.trade.order.entity.RefundOrder;
import cn.daxpay.open.payment.trade.order.entity.PayTrade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/// # 支付插件辅助服务
///
/// 向所有 [AbsPayPluginStrategy] 广播支付/退款生命周期事件。
/// 单策略异常不阻断其他策略与主流程。
///
/// @author xxm
/// @since 2026/03/22
@Slf4j
@Service
@RequiredArgsConstructor
public class PayPluginAssistService {

    private final List<AbsPayPluginStrategy> absPayPluginStrategies;

    /// 支付成功
    public void paySuccess(PayTrade trade) {
        for (var strategy : absPayPluginStrategies) {
            try {
                strategy.paySuccess(trade);
            } catch (Exception e) {
                log.error("插件支付成功处理失败: {}", e.getMessage(), e);
            }
        }
    }

    /// 支付失败
    public void payFail(PayTrade trade) {
        for (var strategy : absPayPluginStrategies) {
            try {
                strategy.payFail(trade);
            } catch (Exception e) {
                log.error("插件支付失败处理失败: {}", e.getMessage(), e);
            }
        }
    }

    /// 支付关闭
    public void payClose(PayTrade trade) {
        for (var strategy : absPayPluginStrategies) {
            try {
                strategy.payClose(trade);
            } catch (Exception e) {
                log.error("插件支付关闭处理失败: {}", e.getMessage(), e);
            }
        }
    }

    /// 退款成功
    public void refundSuccess(PayTrade trade, RefundOrder refundOrder) {
        for (var strategy : absPayPluginStrategies) {
            try {
                strategy.refundSuccess(trade, refundOrder);
            } catch (Exception e) {
                log.error("插件退款成功处理失败: {}", e.getMessage(), e);
            }
        }
    }

    /// 退款关闭
    public void refundClose(PayTrade trade, RefundOrder refundOrder) {
        for (var strategy : absPayPluginStrategies) {
            try {
                strategy.refundClose(trade, refundOrder);
            } catch (Exception e) {
                log.error("插件退款关闭处理失败: {}", e.getMessage(), e);
            }
        }
    }
}
