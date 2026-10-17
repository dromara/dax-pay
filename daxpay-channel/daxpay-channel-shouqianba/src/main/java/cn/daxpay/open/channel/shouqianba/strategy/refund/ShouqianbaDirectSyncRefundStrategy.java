package cn.daxpay.open.channel.shouqianba.strategy.refund;

import cn.daxpay.open.channel.shouqianba.service.payment.refund.ShouqianbaRefundSyncService;
import cn.daxpay.open.payment.strategy.refund.AbsSyncRefundStrategy;
import cn.daxpay.open.payment.trade.runtime.bo.RefundResultBo;
import cn.daxpay.open.payment.trade.order.entity.RefundOrder;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 收钱吧直连退款同步策略
///
/// 收钱吧退款为同步终态无退款查询接口, 恒软失败(对齐参考实现能力)。
@Slf4j
@Service
@RequiredArgsConstructor
public class ShouqianbaDirectSyncRefundStrategy extends AbsSyncRefundStrategy {

    private final ShouqianbaRefundSyncService shouqianbaRefundSyncService;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.SHOUQIANBA;
    }

    @Override
    public RefundResultBo doSync(RefundOrder refundOrder) {
        return shouqianbaRefundSyncService.sync();
    }
}
