package cn.daxpay.open.channel.fubei.strategy.refund;

import cn.daxpay.open.channel.fubei.service.payment.refund.FubeiRefundSyncService;
import cn.daxpay.open.payment.strategy.refund.AbsSyncRefundStrategy;
import cn.daxpay.open.payment.trade.runtime.bo.RefundResultBo;
import cn.daxpay.open.payment.trade.order.entity.RefundOrder;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 付呗直连退款同步策略
///
/// 付呗无退款状态查询接口(退款为同步受理终态), 恒软失败(正常退款单在受理时已落终态, 不进入本链路)。
@Slf4j
@Service
@RequiredArgsConstructor
public class FubeiDirectSyncRefundStrategy extends AbsSyncRefundStrategy {

    private final FubeiRefundSyncService fubeiRefundSyncService;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.FUBEI_PAY;
    }

    @Override
    public RefundResultBo doSync(RefundOrder refundOrder) {
        return fubeiRefundSyncService.sync();
    }
}
