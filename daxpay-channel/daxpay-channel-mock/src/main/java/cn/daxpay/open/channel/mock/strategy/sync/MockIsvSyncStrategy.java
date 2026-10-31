package cn.daxpay.open.channel.mock.strategy.sync;

import cn.daxpay.open.channel.mock.service.MockSyncService;
import cn.daxpay.open.payment.strategy.pay.PayStrategyContext;
import cn.daxpay.open.payment.strategy.sync.AbsSyncPayOrderStrategy;
import cn.daxpay.open.payment.trade.runtime.bo.PaySyncResultBo;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # Mock 模拟通道服务商支付同步策略
///
/// 模拟支付服务商产品(ProductEnum.MOCK_ISV)下的支付同步策略, 形态照抄直连壳
/// [MockSyncStrategy], 查询执行委托给共用的 [MockSyncService],
/// 用于挂起订单的状态纠正(确认页推进/超时关单链路)。
@Slf4j
@Service
@RequiredArgsConstructor
public class MockIsvSyncStrategy extends AbsSyncPayOrderStrategy {

    private final MockSyncService mockSyncService;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.MOCK_ISV;
    }

    @Override
    public PaySyncResultBo doSync(PayStrategyContext context) {
        return mockSyncService.sync(context.getTrade());
    }
}
