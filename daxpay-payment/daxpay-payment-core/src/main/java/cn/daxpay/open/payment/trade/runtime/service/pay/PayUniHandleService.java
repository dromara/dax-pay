package cn.daxpay.open.payment.trade.runtime.service.pay;

import cn.daxpay.open.payment.trade.enums.GatewayOrderStatusEnum;
import cn.daxpay.open.payment.trade.enums.NormalPayOrderStatusEnum;
import cn.daxpay.open.payment.trade.enums.PayFundStatusEnum;
import cn.daxpay.open.payment.trade.enums.PayTradeTypeEnum;
import cn.daxpay.open.payment.trade.runtime.bo.PaySyncResultBo;
import cn.daxpay.open.payment.trade.runtime.bo.PayTradeResultBo;
import cn.daxpay.open.payment.trade.order.dao.GatewayPayOrderManager;
import cn.daxpay.open.payment.trade.order.dao.NormalPayOrderManager;
import cn.daxpay.open.payment.trade.order.dao.PayTradeManager;
import cn.daxpay.open.payment.trade.order.entity.GatewayPayOrder;
import cn.daxpay.open.payment.trade.order.entity.NormalPayOrder;
import cn.daxpay.open.payment.trade.order.entity.PayTrade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Objects;

/// # 交易统一处理服务
///
/// 支付成功/失败/关闭后的统一处理逻辑, 按 trade_type 回写对应业务容器。
/// 通道回执字段统一写容器, trade 仅保留资金态(payBody/outOrderNo 等)。
@Slf4j
@Service
@RequiredArgsConstructor
public class PayUniHandleService {

    private final PayTradeManager payTradeManager;
    private final NormalPayOrderManager payNormalOrderManager;
    private final GatewayPayOrderManager gatewayPayOrderManager;

    /// 支付发起后处理
    /// 不论是否完成都更新交易单; 仅资金状态为 SUCCESS 时同步容器为 PAID。
    /// 回执字段(transOrderNo/buyerId 等)写容器。
    public void payAfterHandel(PayTrade trade, PayTradeResultBo result) {
        payTradeManager.updateById(trade);
        if (isGateway(trade)) {
            GatewayPayOrder order = gatewayPayOrderManager.findById(trade.getContainerId()).orElse(null);
            if (order != null) {
                applyGatewayReceipts(order, result);
                if (Objects.equals(trade.getStatus(), PayFundStatusEnum.SUCCESS.getCode())) {
                    order.setStatus(GatewayOrderStatusEnum.PAID.getCode());
                    order.setPayTime(trade.getPayTime());
                }
                gatewayPayOrderManager.updateById(order);
            }
            return;
        }
        NormalPayOrder order = payNormalOrderManager.findById(trade.getContainerId()).orElse(null);
        if (order != null) {
            applyNormalReceipts(order, result);
            if (Objects.equals(trade.getStatus(), PayFundStatusEnum.SUCCESS.getCode())) {
                order.setStatus(NormalPayOrderStatusEnum.PAID.getCode());
                order.setPayTime(trade.getPayTime());
            }
            payNormalOrderManager.updateById(order);
        }
    }

    /// 支付成功后续处理(同步/回调路径), 含通道回执写容器
    public void paySuccess(PayTrade trade, PaySyncResultBo syncResult) {
        payTradeManager.updateById(trade);
        if (isGateway(trade)) {
            GatewayPayOrder order = gatewayPayOrderManager.findById(trade.getContainerId()).orElse(null);
            if (order != null) {
                order.setStatus(GatewayOrderStatusEnum.PAID.getCode());
                order.setPayTime(trade.getPayTime());
                applyGatewaySyncReceipts(order, syncResult);
                gatewayPayOrderManager.updateById(order);
            }
            return;
        }
        NormalPayOrder order = payNormalOrderManager.findById(trade.getContainerId()).orElse(null);
        if (order != null) {
            order.setStatus(NormalPayOrderStatusEnum.PAID.getCode());
            order.setPayTime(trade.getPayTime());
            applyNormalSyncReceipts(order, syncResult);
            payNormalOrderManager.updateById(order);
        }
    }

    /// 支付成功后续处理(回调路径, 无回执详情)
    public void paySuccess(PayTrade trade) {
        payTradeManager.updateById(trade);
        markContainerPaid(trade);
    }

    /// 支付失败处理
    public void payFail(PayTrade trade, String errMsg) {
        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
        trade.setStatus(PayFundStatusEnum.FAIL.getCode());
        trade.setCloseTime(now);
        payTradeManager.updateById(trade);
        this.markContainerClosed(trade, now, false, errMsg);
    }

    /// 支付关闭处理
    /// @param useCancel true=撤销(资金态置 CANCEL), false=关闭(资金态置 CLOSE)
    public void payClose(PayTrade trade, boolean useCancel) {
        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
        trade.setStatus(useCancel
                ? PayFundStatusEnum.CANCEL.getCode()
                : PayFundStatusEnum.CLOSE.getCode());
        trade.setCloseTime(now);
        payTradeManager.updateById(trade);
        this.markContainerClosed(trade, now, false, null);
    }

    /// 支付超时关闭: 资金态 CLOSE, 容器态 EXPIRED
    public void payTimeout(PayTrade trade) {
        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
        trade.setStatus(PayFundStatusEnum.CLOSE.getCode());
        trade.setCloseTime(now);
        payTradeManager.updateById(trade);
        this.markContainerClosed(trade, now, true, null);
    }

    /// 仅关闭网关容器(尚未创建 Trade 的预下单超时)
    public void gatewayOrderTimeout(GatewayPayOrder order) {
        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
        order.setStatus(GatewayOrderStatusEnum.EXPIRED.getCode());
        order.setCloseTime(now);
        gatewayPayOrderManager.updateById(order);
    }

    /// 仅关闭网关容器(商户主动关单且尚无 Trade)
    public void gatewayOrderClose(GatewayPayOrder order) {
        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
        order.setStatus(GatewayOrderStatusEnum.CLOSED.getCode());
        order.setCloseTime(now);
        gatewayPayOrderManager.updateById(order);
    }

    private void markContainerPaid(PayTrade trade) {
        if (isGateway(trade)) {
            GatewayPayOrder order = gatewayPayOrderManager.findById(trade.getContainerId()).orElse(null);
            if (order != null) {
                order.setStatus(GatewayOrderStatusEnum.PAID.getCode());
                order.setPayTime(trade.getPayTime());
                gatewayPayOrderManager.updateById(order);
            }
            return;
        }
        NormalPayOrder normalOrder = payNormalOrderManager.findById(trade.getContainerId()).orElse(null);
        if (normalOrder != null) {
            normalOrder.setStatus(NormalPayOrderStatusEnum.PAID.getCode());
            normalOrder.setPayTime(trade.getPayTime());
            payNormalOrderManager.updateById(normalOrder);
        }
    }

    private void markContainerClosed(PayTrade trade, OffsetDateTime now, boolean expired, String errMsg) {
        if (isGateway(trade)) {
            GatewayPayOrder order = gatewayPayOrderManager.findById(trade.getContainerId()).orElse(null);
            if (order != null) {
                order.setStatus(expired
                        ? GatewayOrderStatusEnum.EXPIRED.getCode()
                        : GatewayOrderStatusEnum.CLOSED.getCode());
                order.setCloseTime(now);
                if (errMsg != null) {
                    order.setErrorMsg(errMsg);
                }
                gatewayPayOrderManager.updateById(order);
            }
            return;
        }
        NormalPayOrder normalOrder = payNormalOrderManager.findById(trade.getContainerId()).orElse(null);
        if (normalOrder != null) {
            normalOrder.setStatus(expired
                    ? NormalPayOrderStatusEnum.EXPIRED.getCode()
                    : NormalPayOrderStatusEnum.CLOSED.getCode());
            normalOrder.setCloseTime(now);
            if (errMsg != null) {
                normalOrder.setErrorMsg(errMsg);
            }
            payNormalOrderManager.updateById(normalOrder);
        }
    }

    private void applyNormalReceipts(NormalPayOrder order, PayTradeResultBo result) {
        order.setTransOrderNo(result.getTransOrderNo());
        order.setRelationOrderNo(result.getRelationOrderNo());
        order.setBuyerId(result.getBuyerId());
        order.setBuyerLogonId(result.getBuyerLogonId());
        order.setTradeProduct(result.getTradeProduct());
        order.setTradeWay(result.getTradeWay());
        order.setBankType(result.getBankType());
        order.setPromotionType(result.getPromotionType());
        order.setErrorMsg(null);
    }

    private void applyGatewayReceipts(GatewayPayOrder order, PayTradeResultBo result) {
        order.setTransOrderNo(result.getTransOrderNo());
        order.setRelationOrderNo(result.getRelationOrderNo());
        order.setBuyerId(result.getBuyerId());
        order.setBuyerLogonId(result.getBuyerLogonId());
        order.setTradeProduct(result.getTradeProduct());
        order.setTradeWay(result.getTradeWay());
        order.setBankType(result.getBankType());
        order.setPromotionType(result.getPromotionType());
        order.setErrorMsg(null);
    }

    private void applyNormalSyncReceipts(NormalPayOrder order, PaySyncResultBo syncResult) {
        if (syncResult == null) {
            return;
        }
        if (Objects.nonNull(syncResult.getProvider())) {
            order.setProvider(syncResult.getProvider().getCode());
        }
        order.setBuyerId(syncResult.getBuyerId());
        order.setTradeProduct(syncResult.getTradeProduct());
        order.setTradeWay(syncResult.getTradeWay());
        order.setBankType(syncResult.getBankType());
        order.setPromotionType(syncResult.getPromotionType());
        order.setErrorMsg(null);
    }

    private void applyGatewaySyncReceipts(GatewayPayOrder order, PaySyncResultBo syncResult) {
        if (syncResult == null) {
            return;
        }
        if (Objects.nonNull(syncResult.getProvider())) {
            order.setProvider(syncResult.getProvider().getCode());
        }
        order.setBuyerId(syncResult.getBuyerId());
        order.setTradeProduct(syncResult.getTradeProduct());
        order.setTradeWay(syncResult.getTradeWay());
        order.setBankType(syncResult.getBankType());
        order.setPromotionType(syncResult.getPromotionType());
        order.setErrorMsg(null);
    }

    private boolean isGateway(PayTrade trade) {
        return Objects.equals(trade.getTradeType(), PayTradeTypeEnum.GATEWAY.getCode());
    }
}
