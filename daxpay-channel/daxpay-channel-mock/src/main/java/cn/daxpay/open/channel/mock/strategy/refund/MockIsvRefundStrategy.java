package cn.daxpay.open.channel.mock.strategy.refund;

import cn.daxpay.open.channel.mock.service.MockRefundService;
import cn.daxpay.open.payment.strategy.refund.AbsRefundStrategy;
import cn.daxpay.open.payment.trade.order.entity.RefundOrder;
import cn.daxpay.open.payment.trade.runtime.bo.RefundResultBo;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # Mock 模拟通道服务商退款策略
///
/// 模拟支付服务商产品(ProductEnum.MOCK_ISV)下的退款策略, 形态照抄直连壳 [MockRefundStrategy],
/// 退款执行委托给共用的 [MockRefundService](凭证由 Service 内按订单 product 二选一组装)。
/// 服务商支付开关(payEnable)不拦截退款(仅拦新支付)。
@Slf4j
@Service
@RequiredArgsConstructor
public class MockIsvRefundStrategy extends AbsRefundStrategy {

    private final MockRefundService mockRefundService;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.MOCK_ISV;
    }

    @Override
    public RefundResultBo doRefund(RefundOrder refundOrder) {
        return mockRefundService.refund(refundOrder);
    }
}
