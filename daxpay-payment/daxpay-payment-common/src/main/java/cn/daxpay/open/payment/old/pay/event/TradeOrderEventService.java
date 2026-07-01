package cn.daxpay.open.payment.old.pay.event;

import cn.daxpay.open.platform.core.code.DaxPayCode;
import cn.daxpay.open.payment.old.pay.dao.order.pay.PayOrderManager;
import cn.daxpay.open.payment.old.pay.entity.order.pay.PayOrder;
import cn.daxpay.open.payment.common.context.PaymentAssistService;
import cn.daxpay.open.payment.old.pay.service.trade.pay.PayCloseService;
import cn.daxpay.open.payment.old.pay.service.trade.pay.PaySyncService;
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
