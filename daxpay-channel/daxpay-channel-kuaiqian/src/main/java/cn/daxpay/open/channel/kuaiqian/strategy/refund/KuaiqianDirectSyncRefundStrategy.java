package cn.daxpay.open.channel.kuaiqian.strategy.refund;

import cn.daxpay.open.channel.kuaiqian.service.payment.refund.KuaiqianRefundSyncService;
import cn.daxpay.open.payment.strategy.refund.AbsSyncRefundStrategy;
import cn.daxpay.open.payment.trade.runtime.bo.RefundResultBo;
import cn.daxpay.open.payment.trade.order.entity.RefundOrder;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 快钱直连退款同步策略
///
/// 快钱当面付线无退款查询接口, 恒软失败(退款单保持处理中, 终态以退款回调为准)。
@Slf4j
@Service
@RequiredArgsConstructor
public class KuaiqianDirectSyncRefundStrategy extends AbsSyncRefundStrategy {

    private final KuaiqianRefundSyncService kuaiqianRefundSyncService;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.KUAIQIAN_PAY;
    }

    @Override
    public RefundResultBo doSync(RefundOrder refundOrder) {
        return kuaiqianRefundSyncService.sync();
    }
}
