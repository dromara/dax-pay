package cn.daxpay.open.channel.epaylinks.strategy.refund;

import cn.daxpay.open.channel.epaylinks.service.payment.refund.EpaylinksRefundSyncService;
import cn.daxpay.open.payment.strategy.refund.AbsSyncRefundStrategy;
import cn.daxpay.open.payment.trade.runtime.bo.RefundResultBo;
import cn.daxpay.open.payment.trade.order.entity.RefundOrder;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 易票联直连退款同步策略
///
/// 易票联无退款查询接口, 恒软失败(退款终态在发起时已确定为同步成功语义)。
@Slf4j
@Service
@RequiredArgsConstructor
public class EpaylinksDirectSyncRefundStrategy extends AbsSyncRefundStrategy {

    private final EpaylinksRefundSyncService epaylinksRefundSyncService;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.EPAY_LINKS;
    }

    @Override
    public RefundResultBo doSync(RefundOrder refundOrder) {
        return epaylinksRefundSyncService.sync();
    }
}
