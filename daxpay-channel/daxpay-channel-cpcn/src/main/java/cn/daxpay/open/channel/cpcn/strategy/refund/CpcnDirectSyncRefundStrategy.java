package cn.daxpay.open.channel.cpcn.strategy.refund;

import cn.daxpay.open.channel.cpcn.service.payment.refund.CpcnRefundSyncService;
import cn.daxpay.open.payment.strategy.refund.AbsSyncRefundStrategy;
import cn.daxpay.open.payment.trade.runtime.bo.RefundResultBo;
import cn.daxpay.open.payment.trade.order.entity.RefundOrder;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 中金直连退款同步策略
///
/// 中金线无退款查询接口, 恒软失败(退款单保持处理中, 终态以退款回调为准)。
@Slf4j
@Service
@RequiredArgsConstructor
public class CpcnDirectSyncRefundStrategy extends AbsSyncRefundStrategy {

    private final CpcnRefundSyncService cpcnRefundSyncService;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.CPCN_PAY;
    }

    @Override
    public RefundResultBo doSync(RefundOrder refundOrder) {
        return cpcnRefundSyncService.sync();
    }
}
