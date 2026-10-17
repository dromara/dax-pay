package cn.daxpay.open.channel.llianpay.strategy.refund;

import cn.daxpay.open.channel.llianpay.client.credential.LlianpaySdkCredential;
import cn.daxpay.open.channel.llianpay.service.direct.LlianpayDirectConfigAssembler;
import cn.daxpay.open.channel.llianpay.service.payment.refund.LlianpayRefundService;
import cn.daxpay.open.payment.strategy.refund.AbsRefundStrategy;
import cn.daxpay.open.payment.trade.runtime.bo.RefundResultBo;
import cn.daxpay.open.payment.trade.order.entity.RefundOrder;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 连连直连退款策略
@Slf4j
@Service
@RequiredArgsConstructor
public class LlianpayDirectRefundStrategy extends AbsRefundStrategy {

    private final LlianpayRefundService llianpayRefundService;
    private final LlianpayDirectConfigAssembler llianpayDirectConfigAssembler;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.LLIAN_PAY;
    }

    @Override
    public RefundResultBo doRefund(RefundOrder refundOrder) {
        LlianpaySdkCredential credential = llianpayDirectConfigAssembler.buildConfig(
                refundOrder.getMchNo(), refundOrder.getChannelMchNo(), refundOrder.getCapability());
        return llianpayRefundService.refund(refundOrder, credential);
    }
}
