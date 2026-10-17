package cn.daxpay.open.channel.ysep.strategy.refund;

import cn.daxpay.open.channel.ysep.service.payment.YsepRefundSyncService;
import cn.daxpay.open.payment.strategy.refund.AbsSyncRefundStrategy;
import cn.daxpay.open.payment.trade.order.entity.RefundOrder;
import cn.daxpay.open.payment.trade.runtime.bo.RefundResultBo;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 银盛退款同步策略(ysep_pay)
///
/// 银盛开放平台退款接口同步返回终态, 无独立退款查询接口, 同步恒软失败
/// (退款单保持终态以退款接口同步结果为准, 对齐快钱直连降级模式)。
@Slf4j
@Service
@RequiredArgsConstructor
public class YsepSyncRefundStrategy extends AbsSyncRefundStrategy {

    private final YsepRefundSyncService ysepRefundSyncService;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.YSEP_PAY;
    }

    @Override
    public RefundResultBo doSync(RefundOrder refundOrder) {
        return ysepRefundSyncService.sync();
    }
}
