package cn.daxpay.open.payment.trade.order.service;

import cn.daxpay.open.payment.trade.enums.PayTradeTypeEnum;
import cn.daxpay.open.payment.trade.order.dao.GatewayPayOrderManager;
import cn.daxpay.open.payment.trade.order.dao.NormalPayOrderManager;
import cn.daxpay.open.payment.trade.order.dao.PayTradeManager;
import cn.daxpay.open.payment.trade.order.entity.GatewayPayOrder;
import cn.daxpay.open.payment.trade.order.entity.NormalPayOrder;
import cn.daxpay.open.payment.trade.order.entity.PayTrade;
import cn.daxpay.open.payment.trade.order.result.GatewayPayOrderResult;
import cn.daxpay.open.payment.trade.order.result.NormalPayOrderResult;
import cn.daxpay.open.payment.trade.order.result.PayTradeResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;

/// # 交易订单详情组装器
///
/// 列表场景仅单表 Convert；详情场景在此按 tradeType 补对侧字段。
/// - 容器详情 → 补资金凭证字段
/// - 资金详情 → 按 [PayTrade#getTradeType] 加载容器补业务字段
///
/// 供运营端 / 商户端共享（下沉自 payment-admin）
@Component
@RequiredArgsConstructor
public class TradeOrderDetailAssembler {

    private final PayTradeManager payTradeManager;
    private final NormalPayOrderManager normalPayOrderManager;
    private final GatewayPayOrderManager gatewayPayOrderManager;

    /// 普通支付详情：补资金凭证字段
    public void fillFundOnNormal(NormalPayOrderResult result, Long containerId) {
        if (result == null || containerId == null) {
            return;
        }
        PayTrade trade = payTradeManager.findByContainerId(containerId, PayTradeTypeEnum.NORMAL.getCode())
                .orElse(null);
        fillFundOnContainerResult(result, trade);
    }

    /// 网关支付详情：补资金凭证字段
    public void fillFundOnGateway(GatewayPayOrderResult result, Long containerId) {
        if (result == null || containerId == null) {
            return;
        }
        PayTrade trade = payTradeManager.findByContainerId(containerId, PayTradeTypeEnum.GATEWAY.getCode())
                .orElse(null);
        fillFundOnGatewayResult(result, trade);
    }

    /// 资金交易详情：按 tradeType 补容器业务字段
    public void fillContainerOnTrade(PayTradeResult result, PayTrade trade) {
        if (result == null || trade == null || trade.getContainerId() == null) {
            return;
        }
        String tradeType = trade.getTradeType();
        if (Objects.equals(tradeType, PayTradeTypeEnum.GATEWAY.getCode())) {
            GatewayPayOrder order = gatewayPayOrderManager.findById(trade.getContainerId()).orElse(null);
            if (order != null) {
                fillFromGateway(result, order);
            }
            return;
        }
        // 默认按普通支付容器解析（含 historical normal；未知类型尝试 normal）
        NormalPayOrder order = normalPayOrderManager.findById(trade.getContainerId()).orElse(null);
        if (order != null) {
            fillFromNormal(result, order);
        }
    }

    private void fillFundOnContainerResult(NormalPayOrderResult result, PayTrade trade) {
        if (trade == null) {
            return;
        }
        result.setTradeNo(trade.getTradeNo());
        result.setOutOrderNo(trade.getOutOrderNo());
        result.setFundStatus(trade.getStatus());
        result.setRefundableBalance(trade.getRefundableBalance());
    }

    private void fillFundOnGatewayResult(GatewayPayOrderResult result, PayTrade trade) {
        if (trade == null) {
            return;
        }
        result.setTradeNo(trade.getTradeNo());
        result.setOutOrderNo(trade.getOutOrderNo());
        result.setFundStatus(trade.getStatus());
        result.setRefundableBalance(trade.getRefundableBalance());
        result.setRelationOrderNo(trade.getRelationOrderNo());
    }

    private void fillFromNormal(PayTradeResult result, NormalPayOrder order) {
        result.setContainerOrderNo(order.getOrderNo());
        result.setBizOrderNo(order.getBizOrderNo());
        // title 不再从容器覆盖: 统一使用 PayTrade 冗余的 title(列表/详情同源)
        result.setContainerStatus(order.getStatus());
        result.setProduct(order.getProduct());
        result.setChannel(order.getChannel());
        result.setMethod(order.getMethod());
        result.setChannelAppId(order.getChannelAppId());
        result.setBuyerId(order.getBuyerId());
        result.setOpenid(order.getOpenid());
        result.setAuthCode(order.getAuthCode());
        result.setTradeProduct(order.getTradeProduct());
        result.setTradeWay(order.getTradeWay());
        result.setBankType(order.getBankType());
        result.setErrorMsg(order.getErrorMsg());
        result.setPayBody(order.getPayBody());
        result.setPayBodyType(order.getPayBodyType());
        result.setProvider(order.getProvider());
        result.setLimitPay(order.getLimitPay());
        result.setTransOrderNo(order.getTransOrderNo());
        result.setPromotionType(order.getPromotionType());
        result.setExpiredTime(order.getExpiredTime());
    }

    private void fillFromGateway(PayTradeResult result, GatewayPayOrder order) {
        result.setContainerOrderNo(order.getOrderNo());
        result.setBizOrderNo(order.getBizOrderNo());
        // title 不再从容器覆盖: 统一使用 PayTrade 冗余的 title(列表/详情同源)
        result.setContainerStatus(order.getStatus());
        result.setProduct(order.getProduct());
        result.setChannel(order.getChannel());
        result.setMethod(order.getMethod());
        result.setChannelAppId(order.getChannelAppId());
        result.setBuyerId(order.getBuyerId());
        result.setOpenid(order.getOpenid());
        result.setTradeProduct(order.getTradeProduct());
        result.setTradeWay(order.getTradeWay());
        result.setBankType(order.getBankType());
        result.setErrorMsg(order.getErrorMsg());
        result.setPayBody(order.getPayBody());
        result.setPayBodyType(order.getPayBodyType());
        result.setProvider(order.getProvider());
        result.setLimitPay(order.getLimitPay());
        result.setTransOrderNo(order.getTransOrderNo());
        result.setPromotionType(order.getPromotionType());
        result.setExpiredTime(order.getExpiredTime());
    }
}
