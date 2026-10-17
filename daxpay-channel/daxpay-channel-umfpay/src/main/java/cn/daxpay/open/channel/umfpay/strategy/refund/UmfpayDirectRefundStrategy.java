package cn.daxpay.open.channel.umfpay.strategy.refund;

import cn.daxpay.open.channel.umfpay.client.credential.UmfpaySdkCredential;
import cn.daxpay.open.channel.umfpay.service.direct.UmfpayDirectConfigAssembler;
import cn.daxpay.open.channel.umfpay.service.payment.refund.UmfpayRefundService;
import cn.daxpay.open.payment.strategy.refund.AbsRefundStrategy;
import cn.daxpay.open.payment.trade.runtime.bo.RefundResultBo;
import cn.daxpay.open.payment.trade.order.entity.RefundOrder;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 联动优势直连退款策略
@Slf4j
@Service
@RequiredArgsConstructor
public class UmfpayDirectRefundStrategy extends AbsRefundStrategy {

    private final UmfpayRefundService umfpayRefundService;
    private final UmfpayDirectConfigAssembler umfpayDirectConfigAssembler;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.UMF_PAY;
    }

    @Override
    public RefundResultBo doRefund(RefundOrder refundOrder) {
        UmfpaySdkCredential credential = umfpayDirectConfigAssembler.buildConfig(
                refundOrder.getMchNo(), refundOrder.getChannelMchNo(), refundOrder.getCapability());
        return umfpayRefundService.refund(refundOrder, credential);
    }
}
