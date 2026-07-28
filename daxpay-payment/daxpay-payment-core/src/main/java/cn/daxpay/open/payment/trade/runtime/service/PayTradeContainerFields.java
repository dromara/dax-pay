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

    /// 容器侧通道路由凭证字段(支付/退款回调组装通道凭证用)
    ///
    /// [NormalPayOrder] / [GatewayPayOrder] 同名字段集合, 按 tradeType 分发到对应容器读取,
    /// 供回调服务组装通道调用凭证(直连/服务商)。
    public record CredentialFields(String product, String channelMchNo, String capability) {}

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

    /// 按 tradeType 查一次容器, 返回通道路由凭证字段(product/channelMchNo/capability)
    ///
    /// 容器记录不存在(含跨容器误查)时返回 null, 调用方据此报"无法解析通道凭证"。
    /// 当前仅支持 NORMAL/GATEWAY 容器, 其他 tradeType 按普通容器兜底(与 [resolve] 对齐)。
    public CredentialFields resolveCredentialFields(PayTrade trade) {
        if (isGateway(trade)) {
            return gatewayPayOrderManager.findById(trade.getContainerId())
                    .map(order -> new CredentialFields(
                            order.getProduct(), order.getChannelMchNo(), order.getCapability()))
                    .orElse(null);
        }
        return normalPayOrderManager.findById(trade.getContainerId())
                .map(order -> new CredentialFields(
                        order.getProduct(), order.getChannelMchNo(), order.getCapability()))
                .orElse(null);
    }

    /// 判断资金凭证是否为网关支付类型
    private boolean isGateway(PayTrade trade) {
        return Objects.equals(trade.getTradeType(), PayTradeTypeEnum.GATEWAY.getCode());
    }
}
