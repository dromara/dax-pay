package org.dromara.daxpay.payment.old.pay.event;

import org.dromara.daxpay.platform.core.code.DaxPayCode;
import org.dromara.daxpay.payment.old.pay.dao.order.pay.PayOrderManager;
import org.dromara.daxpay.payment.old.pay.entity.order.pay.PayOrder;
import org.dromara.daxpay.payment.old.pay.service.assist.PaymentAssistService;
import org.dromara.daxpay.payment.old.pay.service.trade.pay.PayCloseService;
import org.dromara.daxpay.payment.old.pay.service.trade.pay.PaySyncService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

/// # 订单交易相关的延时事件
///
@Slf4j
@Service
@RequiredArgsConstructor
public class TradeOrderEventService {
    private final PaymentAssistService paymentAssistService;

    private final PayOrderManager payOrderManager;

    private final PaySyncService paySyncService;

    private final PayCloseService payCloseService;
}
