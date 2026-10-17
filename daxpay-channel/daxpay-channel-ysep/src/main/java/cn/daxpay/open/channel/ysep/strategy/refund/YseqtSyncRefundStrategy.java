package cn.daxpay.open.channel.ysep.strategy.refund;

import cn.daxpay.open.channel.ysep.service.payment.YsepRefundSyncService;
import cn.daxpay.open.payment.strategy.refund.AbsSyncRefundStrategy;
import cn.daxpay.open.payment.trade.order.entity.RefundOrder;
import cn.daxpay.open.payment.trade.runtime.bo.RefundResultBo;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 银盛e企通退款同步策略(yseqt_pay)
///
/// 银盛e企通退款接口同步返回终态(COM000/COM004 即成功), 无独立退款查询接口,
/// 同步恒软失败(退款单终态以退款接口同步结果为准, 对齐快钱直连降级模式)。
@Slf4j
@Service
@RequiredArgsConstructor
public class YseqtSyncRefundStrategy extends AbsSyncRefundStrategy {

    private final YsepRefundSyncService ysepRefundSyncService;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.YSEQT_PAY;
    }

    @Override
    public RefundResultBo doSync(RefundOrder refundOrder) {
        return ysepRefundSyncService.sync();
    }
}
