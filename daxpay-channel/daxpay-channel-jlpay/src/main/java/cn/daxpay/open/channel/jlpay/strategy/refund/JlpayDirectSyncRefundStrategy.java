package cn.daxpay.open.channel.jlpay.strategy.refund;

import cn.daxpay.open.channel.jlpay.service.payment.refund.JlpayRefundSyncService;
import cn.daxpay.open.payment.strategy.refund.AbsSyncRefundStrategy;
import cn.daxpay.open.payment.trade.runtime.bo.RefundResultBo;
import cn.daxpay.open.payment.trade.order.entity.RefundOrder;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 嘉联直连退款同步策略
///
/// 嘉联无退款状态查询接口(退款为同步终态, 受理即完成), 恒软失败;
/// 退款单正常不会进入处理中状态, 本策略仅兜底对齐策略族签名。
@Slf4j
@Service
@RequiredArgsConstructor
public class JlpayDirectSyncRefundStrategy extends AbsSyncRefundStrategy {

    private final JlpayRefundSyncService jlpayRefundSyncService;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.JL_PAY;
    }

    @Override
    public RefundResultBo doSync(RefundOrder refundOrder) {
        return jlpayRefundSyncService.sync();
    }
}
