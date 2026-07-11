package cn.daxpay.open.payment.core.trade.service;

import cn.daxpay.open.payment.common.enums.GatewayOrderStatusEnum;
import cn.daxpay.open.payment.common.enums.NormalPayOrderStatusEnum;
import cn.daxpay.open.payment.common.enums.PayFundStatusEnum;
import cn.daxpay.open.payment.common.enums.PayTradeTypeEnum;
import cn.daxpay.open.payment.core.trade.dao.GatewayPayOrderManager;
import cn.daxpay.open.payment.core.trade.dao.NormalPayOrderManager;
import cn.daxpay.open.payment.core.trade.dao.PayTradeManager;
import cn.daxpay.open.payment.core.trade.entity.GatewayPayOrder;
import cn.daxpay.open.payment.core.trade.entity.NormalPayOrder;
import cn.daxpay.open.payment.core.trade.entity.PayTrade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Objects;

/// # 交易统一处理服务
///
/// 支付成功/失败/关闭后的统一处理逻辑, 按 trade_type 回写对应业务容器。
@Slf4j
@Service
@RequiredArgsConstructor
public class PayUniHandleService {

    private final PayTradeManager payTradeManager;
    private final NormalPayOrderManager payNormalOrderManager;
    private final GatewayPayOrderManager gatewayPayOrderManager;

    /// 支付发起后处理
    /// 不论是否完成都更新交易单; 仅资金状态为 SUCCESS 时同步容器为 PAID。
    public void payAfterHandel(PayTrade trade) {
        payTradeManager.updateById(trade);
        if (Objects.equals(trade.getStatus(), PayFundStatusEnum.SUCCESS.getCode())) {
            this.markContainerPaid(trade);
        }
    }

    /// 支付成功后续处理
    public void paySuccess(PayTrade trade) {
        this.markContainerPaid(trade);
        payTradeManager.updateById(trade);
    }

    /// 支付失败处理
    public void payFail(PayTrade trade, String errMsg) {
        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
        trade.setStatus(PayFundStatusEnum.FAIL.getCode());
        trade.setErrorMsg(errMsg);
        trade.setCloseTime(now);
        payTradeManager.updateById(trade);
        this.markContainerClosed(trade, now, false);
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
        this.markContainerClosed(trade, now, false);
    }

    /// 支付超时关闭: 资金态 CLOSE, 容器态 EXPIRED
    public void payTimeout(PayTrade trade) {
        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
        trade.setStatus(PayFundStatusEnum.CLOSE.getCode());
        trade.setCloseTime(now);
        payTradeManager.updateById(trade);
        this.markContainerClosed(trade, now, true);
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

    private void markContainerClosed(PayTrade trade, OffsetDateTime now, boolean expired) {
        if (isGateway(trade)) {
            GatewayPayOrder order = gatewayPayOrderManager.findById(trade.getContainerId()).orElse(null);
            if (order != null) {
                order.setStatus(expired
                        ? GatewayOrderStatusEnum.EXPIRED.getCode()
                        : GatewayOrderStatusEnum.CLOSED.getCode());
                order.setCloseTime(now);
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
            payNormalOrderManager.updateById(normalOrder);
        }
    }

    private boolean isGateway(PayTrade trade) {
        return Objects.equals(trade.getTradeType(), PayTradeTypeEnum.GATEWAY.getCode());
    }
}
