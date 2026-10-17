package cn.daxpay.open.channel.longpay.strategy.refund;

import cn.daxpay.open.channel.longpay.service.payment.refund.LongpayRefundService;
import cn.daxpay.open.payment.strategy.refund.AbsRefundStrategy;
import cn.daxpay.open.payment.trade.runtime.bo.RefundResultBo;
import cn.daxpay.open.payment.trade.order.entity.RefundOrder;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 建行龙支付直连退款策略
///
/// 建行龙支付聚合线退款未对接(需经商户服务平台或外联平台, 见 [LongpayRefundService]),
/// 本策略恒返回退款失败并给出明确原因。
@Slf4j
@Service
@RequiredArgsConstructor
public class LongpayDirectRefundStrategy extends AbsRefundStrategy {

    private final LongpayRefundService longpayRefundService;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.LONG_PAY;
    }

    @Override
    public RefundResultBo doRefund(RefundOrder refundOrder) {
        return longpayRefundService.refund(refundOrder);
    }
}
