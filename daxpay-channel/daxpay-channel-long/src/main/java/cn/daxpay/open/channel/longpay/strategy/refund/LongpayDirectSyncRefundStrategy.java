package cn.daxpay.open.channel.longpay.strategy.refund;

import cn.daxpay.open.channel.longpay.service.payment.refund.LongpayRefundSyncService;
import cn.daxpay.open.payment.strategy.refund.AbsSyncRefundStrategy;
import cn.daxpay.open.payment.trade.runtime.bo.RefundResultBo;
import cn.daxpay.open.payment.trade.order.entity.RefundOrder;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 建行龙支付直连退款同步策略
///
/// 建行龙支付退款未对接, 恒软失败(见 [LongpayRefundSyncService])。
@Slf4j
@Service
@RequiredArgsConstructor
public class LongpayDirectSyncRefundStrategy extends AbsSyncRefundStrategy {

    private final LongpayRefundSyncService longpayRefundSyncService;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.LONG_PAY;
    }

    @Override
    public RefundResultBo doSync(RefundOrder refundOrder) {
        return longpayRefundSyncService.sync();
    }
}
