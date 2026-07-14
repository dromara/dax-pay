package cn.daxpay.open.payment.trade.runtime.service;

import cn.daxpay.open.payment.trade.enums.PayTradeTypeEnum;
import cn.daxpay.open.payment.trade.order.dao.GatewayPayOrderManager;
import cn.daxpay.open.payment.trade.order.dao.NormalPayOrderManager;
import cn.daxpay.open.payment.trade.order.entity.PayTrade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

/// # 业务容器字段读取
///
/// 资金凭证 [PayTrade] 上的回执字段(关联订单号、支付厂商等)落在业务容器
/// ([NormalPayOrder] / [GatewayPayOrder])，通道关单/退款/同步时按 tradeType **一次回表**读取。
@Service
@RequiredArgsConstructor
public class PayTradeContainerFields {

    private final NormalPayOrderManager normalPayOrderManager;
    private final GatewayPayOrderManager gatewayPayOrderManager;

    /// 容器侧通道常用回执字段
    public record Fields(String relationOrderNo, String tradeProduct) {}

    /// 按 tradeType 查一次容器，同时返回关联订单号与支付厂商/产品回执
    public Fields resolve(PayTrade trade) {
        if (isGateway(trade)) {
            return gatewayPayOrderManager.findById(trade.getContainerId())
                    .map(order -> new Fields(order.getRelationOrderNo(), order.getTradeProduct()))
                    .orElse(new Fields(null, null));
        }
        return normalPayOrderManager.findById(trade.getContainerId())
                .map(order -> new Fields(order.getRelationOrderNo(), order.getTradeProduct()))
                .orElse(new Fields(null, null));
    }

    private boolean isGateway(PayTrade trade) {
        return Objects.equals(trade.getTradeType(), PayTradeTypeEnum.GATEWAY.getCode());
    }
}
