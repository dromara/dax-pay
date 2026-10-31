package cn.daxpay.open.channel.mock.strategy;

import cn.daxpay.open.channel.mock.service.MockCloseService;
import cn.daxpay.open.payment.strategy.pay.AbsPayCloseStrategy;
import cn.daxpay.open.payment.strategy.pay.PayStrategyContext;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import cn.daxpay.open.platform.core.enums.pay.pay.CloseTypeEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # Mock 模拟通道服务商支付关闭策略
///
/// 模拟支付服务商产品(ProductEnum.MOCK_ISV)下的支付关闭策略, 形态照抄直连壳 [MockCloseStrategy],
/// 关闭执行委托给共用的 [MockCloseService]; mock 侧不区分关闭/撤销, useCancel 仅透传决定返回的关闭类型。
@Slf4j
@Service
@RequiredArgsConstructor
public class MockIsvCloseStrategy extends AbsPayCloseStrategy {

    private final MockCloseService mockCloseService;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.MOCK_ISV;
    }

    @Override
    public CloseTypeEnum doClose(PayStrategyContext context, boolean useCancel) {
        return mockCloseService.close(context.getTrade(), useCancel);
    }
}
