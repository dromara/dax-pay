package cn.daxpay.open.payment.core.trade.runtime.service;

import cn.daxpay.open.payment.common.enums.PayTradeTypeEnum;
import cn.daxpay.open.payment.core.trade.order.dao.NormalPayOrderManager;
import cn.daxpay.open.payment.core.trade.order.entity.NormalPayOrder;
import cn.daxpay.open.payment.core.trade.order.entity.PayTrade;
import cn.daxpay.open.payment.gateway.dao.GatewayPayOrderManager;
import cn.daxpay.open.payment.gateway.entity.GatewayPayOrder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

/// # 业务容器字段解析
///
/// 资金凭证 [PayTrade] 上的回执字段(关联订单号、支付厂商等)落在业务容器
/// ([NormalPayOrder] / [GatewayPayOrder])，通道关单/退款/同步时按 tradeType 回表读取。
@Service
@RequiredArgsConstructor
public class ContainerFieldResolver {

    private final NormalPayOrderManager normalPayOrderManager;
    private final GatewayPayOrderManager gatewayPayOrderManager;

    /// 读取容器关联订单号(通道侧 relation / mchnt_order_no 等)
    public String getRelationOrderNo(PayTrade trade) {
        if (isGateway(trade)) {
            return gatewayPayOrderManager.findById(trade.getContainerId())
                    .map(GatewayPayOrder::getRelationOrderNo)
                    .orElse(null);
        }
        return normalPayOrderManager.findById(trade.getContainerId())
                .map(NormalPayOrder::getRelationOrderNo)
                .orElse(null);
    }

    /// 读取容器支付厂商/产品回执(通道侧 order_type 等)
    public String getTradeProduct(PayTrade trade) {
        if (isGateway(trade)) {
            return gatewayPayOrderManager.findById(trade.getContainerId())
                    .map(GatewayPayOrder::getTradeProduct)
                    .orElse(null);
        }
        return normalPayOrderManager.findById(trade.getContainerId())
                .map(NormalPayOrder::getTradeProduct)
                .orElse(null);
    }

    private boolean isGateway(PayTrade trade) {
        return Objects.equals(trade.getTradeType(), PayTradeTypeEnum.GATEWAY.getCode());
    }
}
