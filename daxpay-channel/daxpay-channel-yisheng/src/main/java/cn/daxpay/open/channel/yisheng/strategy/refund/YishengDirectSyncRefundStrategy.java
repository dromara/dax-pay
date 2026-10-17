package cn.daxpay.open.channel.yisheng.strategy.refund;

import cn.daxpay.open.channel.yisheng.service.payment.refund.YishengRefundSyncService;
import cn.daxpay.open.payment.strategy.refund.AbsSyncRefundStrategy;
import cn.daxpay.open.payment.trade.runtime.bo.RefundResultBo;
import cn.daxpay.open.payment.trade.order.entity.RefundOrder;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 易生直连退款同步策略
///
/// 易生退款为同步终态无退款查询接口, 恒软失败(对齐参考实现能力)。
@Slf4j
@Service
@RequiredArgsConstructor
public class YishengDirectSyncRefundStrategy extends AbsSyncRefundStrategy {

    private final YishengRefundSyncService yishengRefundSyncService;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.YISHENG_PAY;
    }

    @Override
    public RefundResultBo doSync(RefundOrder refundOrder) {
        return yishengRefundSyncService.sync();
    }
}
