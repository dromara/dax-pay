package cn.daxpay.open.channel.mock.strategy.refund;

import cn.daxpay.open.channel.mock.service.MockRefundSyncService;
import cn.daxpay.open.payment.strategy.refund.AbsSyncRefundStrategy;
import cn.daxpay.open.payment.trade.order.entity.RefundOrder;
import cn.daxpay.open.payment.trade.runtime.bo.RefundResultBo;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # Mock 模拟通道服务商退款同步策略
///
/// 模拟支付服务商产品(ProductEnum.MOCK_ISV)下的退款同步策略, 形态照抄直连壳
/// [MockRefundSyncStrategy], 查询执行委托给共用的 [MockRefundSyncService],
/// 用于处理中退款单的状态纠正。
@Slf4j
@Service
@RequiredArgsConstructor
public class MockIsvRefundSyncStrategy extends AbsSyncRefundStrategy {

    private final MockRefundSyncService mockRefundSyncService;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.MOCK_ISV;
    }

    @Override
    public RefundResultBo doSync(RefundOrder refundOrder) {
        return mockRefundSyncService.sync(refundOrder);
    }
}
