package cn.daxpay.open.channel.llianpay.strategy.refund;

import cn.daxpay.open.channel.llianpay.service.payment.refund.LlianpayRefundSyncService;
import cn.daxpay.open.payment.strategy.refund.AbsSyncRefundStrategy;
import cn.daxpay.open.payment.trade.runtime.bo.RefundResultBo;
import cn.daxpay.open.payment.trade.order.entity.RefundOrder;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 连连直连退款同步策略
///
/// 连连连收单线无退款查询接口, 恒软失败(退款单保持处理中, 终态以退款回调为准)。
@Slf4j
@Service
@RequiredArgsConstructor
public class LlianpayDirectSyncRefundStrategy extends AbsSyncRefundStrategy {

    private final LlianpayRefundSyncService llianpayRefundSyncService;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.LLIAN_PAY;
    }

    @Override
    public RefundResultBo doSync(RefundOrder refundOrder) {
        return llianpayRefundSyncService.sync();
    }
}
