package org.dromara.daxpay.payment.pay.service;

import org.dromara.daxpay.payment.common.enums.NormalOrderStatusEnum;
import org.dromara.daxpay.payment.common.enums.PayFundStatusEnum;
import org.dromara.daxpay.payment.pay.order.dao.PayNormalOrderManager;
import org.dromara.daxpay.payment.pay.order.entity.PayNormalOrder;
import org.dromara.daxpay.payment.pay.order.dao.PayTradeManager;
import org.dromara.daxpay.payment.pay.order.entity.PayTrade;
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
    private final PayNormalOrderManager payNormalOrderManager;

    /// 支付成功后续处理
    public void paySuccess(PayTrade trade) {
        PayNormalOrder normalOrder = payNormalOrderManager.findById(trade.getContainerId())
                .orElse(null);
        if (normalOrder != null) {
            normalOrder.setStatus(NormalOrderStatusEnum.PAID.getCode());
            payNormalOrderManager.updateById(normalOrder);
        }
        payTradeManager.updateById(trade);
    }

    /// 支付失败处理
    public void payFail(PayTrade trade, PayNormalOrder normalOrder, String errMsg) {
        trade.setStatus(PayFundStatusEnum.FAIL.getCode());
        trade.setErrorMsg(errMsg);
        trade.setCloseTime(OffsetDateTime.now(ZoneOffset.UTC));
        normalOrder.setStatus(NormalOrderStatusEnum.CLOSED.getCode());
        payTradeManager.updateById(trade);
        payNormalOrderManager.updateById(normalOrder);
    }

    /// 支付关闭处理
    public void payClose(PayTrade trade, PayNormalOrder normalOrder) {
        trade.setStatus(PayFundStatusEnum.CLOSE.getCode());
        trade.setCloseTime(OffsetDateTime.now(ZoneOffset.UTC));
        normalOrder.setStatus(NormalOrderStatusEnum.CLOSED.getCode());
        payTradeManager.updateById(trade);
        payNormalOrderManager.updateById(normalOrder);
    }
}
