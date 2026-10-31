package cn.daxpay.open.channel.mock.strategy.refund;

import cn.daxpay.open.channel.mock.service.MockRefundService;
import cn.daxpay.open.payment.strategy.refund.AbsRefundStrategy;
import cn.daxpay.open.payment.trade.order.entity.RefundOrder;
import cn.daxpay.open.payment.trade.runtime.bo.RefundResultBo;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # Mock 模拟通道退款策略
///
/// 模拟支付产品(ProductEnum.MOCK_PAY)下的退款策略。
/// mock 零资料接入无通道凭证可组装, 退款执行委托给 [MockRefundService]。
@Slf4j
@Service
@RequiredArgsConstructor
public class MockRefundStrategy extends AbsRefundStrategy {

    private final MockRefundService mockRefundService;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.MOCK_PAY;
    }

    @Override
    public RefundResultBo doRefund(RefundOrder refundOrder) {
        return mockRefundService.refund(refundOrder);
    }
}
