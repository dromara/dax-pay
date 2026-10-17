package cn.daxpay.open.channel.jlpay.strategy.refund;

import cn.daxpay.open.channel.jlpay.client.credential.JlpaySdkCredential;
import cn.daxpay.open.channel.jlpay.service.direct.JlpayDirectConfigAssembler;
import cn.daxpay.open.channel.jlpay.service.payment.refund.JlpayRefundService;
import cn.daxpay.open.payment.strategy.refund.AbsRefundStrategy;
import cn.daxpay.open.payment.trade.runtime.bo.RefundResultBo;
import cn.daxpay.open.payment.trade.order.entity.RefundOrder;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 嘉联直连退款策略
@Slf4j
@Service
@RequiredArgsConstructor
public class JlpayDirectRefundStrategy extends AbsRefundStrategy {

    private final JlpayRefundService jlpayRefundService;
    private final JlpayDirectConfigAssembler jlpayDirectConfigAssembler;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.JL_PAY;
    }

    @Override
    public RefundResultBo doRefund(RefundOrder refundOrder) {
        JlpaySdkCredential credential = jlpayDirectConfigAssembler.buildConfig(
                refundOrder.getMchNo(), refundOrder.getChannelMchNo(), refundOrder.getCapability());
        return jlpayRefundService.refund(refundOrder, credential);
    }
}
