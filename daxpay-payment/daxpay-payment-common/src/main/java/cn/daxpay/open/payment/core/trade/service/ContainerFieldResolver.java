package cn.daxpay.open.payment.core.trade.service;

import cn.daxpay.open.payment.common.enums.PayTradeTypeEnum;
import cn.daxpay.open.payment.core.trade.dao.GatewayPayOrderManager;
import cn.daxpay.open.payment.core.trade.dao.NormalPayOrderManager;
import cn.daxpay.open.payment.core.trade.entity.GatewayPayOrder;
import cn.daxpay.open.payment.core.trade.entity.NormalPayOrder;
import cn.daxpay.open.payment.core.trade.entity.PayTrade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.Objects;

/// # 容器字段解析器
///
/// 通道层(close/sync/refund 服务)需要读取容器的 relationOrderNo / tradeProduct 等字段时,
/// 通过本类凭 trade.containerId + tradeType 定位容器, 屏蔽容器多态。
@Service
@RequiredArgsConstructor
public class ContainerFieldResolver {

    private final NormalPayOrderManager payNormalOrderManager;
    private final GatewayPayOrderManager gatewayPayOrderManager;

    /// 获取通道关联订单号(富友等通道 close/sync/refund 需要)
    public String getRelationOrderNo(PayTrade trade) {
        var opt = resolveContainer(trade);
        if (opt.isEmpty()) {
            return null;
        }
        Object container = opt.get();
        if (container instanceof NormalPayOrder n) {
            return n.getRelationOrderNo();
        }
        if (container instanceof GatewayPayOrder g) {
            return g.getRelationOrderNo();
        }
        return null;
    }

    /// 获取通道方记录的支付产品(富友等通道 close/sync/refund 需要)
    public String getTradeProduct(PayTrade trade) {
        var opt = resolveContainer(trade);
        if (opt.isEmpty()) {
            return null;
        }
        Object container = opt.get();
        if (container instanceof NormalPayOrder n) {
            return n.getTradeProduct();
        }
        if (container instanceof GatewayPayOrder g) {
            return g.getTradeProduct();
        }
        return null;
    }

    /// 获取支付完成时间(退款需原支付日期)
    public OffsetDateTime getPayTime(PayTrade trade) {
        var opt = resolveContainer(trade);
        if (opt.isEmpty()) {
            return null;
        }
        Object container = opt.get();
        if (container instanceof NormalPayOrder n) {
            return n.getPayTime();
        }
        if (container instanceof GatewayPayOrder g) {
            return g.getPayTime();
        }
        return null;
    }

    private java.util.Optional<Object> resolveContainer(PayTrade trade) {
        if (trade.getContainerId() == null) {
            return java.util.Optional.empty();
        }
        if (Objects.equals(trade.getTradeType(), PayTradeTypeEnum.GATEWAY.getCode())) {
            return gatewayPayOrderManager.findById(trade.getContainerId())
                    .map(g -> (Object) g);
        }
        return payNormalOrderManager.findById(trade.getContainerId())
                .map(n -> (Object) n);
    }
}
