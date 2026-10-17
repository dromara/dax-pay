package cn.daxpay.open.channel.helipay.strategy.refund;

import cn.daxpay.open.channel.helipay.service.payment.HelipayRefundSyncService;
import cn.daxpay.open.payment.strategy.refund.AbsSyncRefundStrategy;
import cn.daxpay.open.payment.trade.runtime.bo.RefundResultBo;
import cn.daxpay.open.payment.trade.order.entity.RefundOrder;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 合利宝直连退款同步策略
///
/// 合利宝扫码产品线无退款状态查询接口(退款为同步结果模式), 恒软失败
/// (退款终态已在退款受理时落定, 本路径仅兜底人工/对账场景触发的退款同步)。
@Slf4j
@Service
@RequiredArgsConstructor
public class HelipayDirectSyncRefundStrategy extends AbsSyncRefundStrategy {

    private final HelipayRefundSyncService helipayRefundSyncService;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.HELI_PAY;
    }

    @Override
    public RefundResultBo doSync(RefundOrder refundOrder) {
        return helipayRefundSyncService.sync();
    }
}
