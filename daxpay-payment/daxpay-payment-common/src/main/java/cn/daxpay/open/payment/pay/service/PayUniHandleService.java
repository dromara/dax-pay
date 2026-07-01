package cn.daxpay.open.payment.pay.service;

import cn.daxpay.open.payment.common.enums.NormalPayOrderStatusEnum;
import cn.daxpay.open.payment.common.enums.PayFundStatusEnum;
import cn.daxpay.open.payment.pay.order.dao.NormalPayOrderManager;
import cn.daxpay.open.payment.pay.order.entity.NormalPayOrder;
import cn.daxpay.open.payment.pay.order.dao.PayTradeManager;
import cn.daxpay.open.payment.pay.order.entity.PayTrade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Objects;

/// # 交易统一处理服务
///
/// 支付成功/失败/关闭后的统一处理逻辑
@Slf4j
@Service
@RequiredArgsConstructor
public class PayUniHandleService {

    private final PayTradeManager payTradeManager;
    private final NormalPayOrderManager payNormalOrderManager;

    /// 支付成功后续处理（同步冗余时间线到容器）
    public void paySuccess(PayTrade trade) {
        NormalPayOrder normalOrder = payNormalOrderManager.findById(trade.getContainerId())
                .orElse(null);
        if (normalOrder != null) {
            normalOrder.setStatus(NormalPayOrderStatusEnum.PAID.getCode());
            normalOrder.setPayTime(trade.getPayTime());
            payNormalOrderManager.updateById(normalOrder);
        }
        payTradeManager.updateById(trade);
    }

    /// 支付失败处理（同步冗余时间线到容器）
    public void payFail(PayTrade trade, NormalPayOrder normalOrder, String errMsg) {
        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
        trade.setStatus(PayFundStatusEnum.FAIL.getCode());
        trade.setErrorMsg(errMsg);
        trade.setCloseTime(now);
        normalOrder.setStatus(NormalPayOrderStatusEnum.CLOSED.getCode());
        normalOrder.setCloseTime(now);
        payTradeManager.updateById(trade);
        payNormalOrderManager.updateById(normalOrder);
    }

    /// 支付关闭处理（同步冗余时间线到容器）
    public void payClose(PayTrade trade, NormalPayOrder normalOrder) {
        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
        trade.setStatus(PayFundStatusEnum.CLOSE.getCode());
        trade.setCloseTime(now);
        normalOrder.setStatus(NormalPayOrderStatusEnum.CLOSED.getCode());
        normalOrder.setCloseTime(now);
        payTradeManager.updateById(trade);
        payNormalOrderManager.updateById(normalOrder);
    }
}
