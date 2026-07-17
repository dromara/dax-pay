package cn.daxpay.open.channel.yeepay.strategy;

import cn.daxpay.open.channel.yeepay.client.credential.YeepaySdkCredential;
import cn.daxpay.open.channel.yeepay.service.direct.YeepayDirectConfigAssembler;
import cn.daxpay.open.channel.yeepay.service.payment.refund.YeepayRefundService;
import cn.daxpay.open.payment.strategy.refund.AbsRefundStrategy;
import cn.daxpay.open.payment.trade.runtime.bo.RefundResultBo;
import cn.daxpay.open.payment.trade.order.entity.RefundOrder;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 易宝直连退款策略
@Slf4j
@Service
@RequiredArgsConstructor
public class YeepayDirectRefundStrategy extends AbsRefundStrategy {

    private final YeepayRefundService yeepayRefundService;
    private final YeepayDirectConfigAssembler yeepayDirectConfigAssembler;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.YEE_PAY;
    }

    @Override
    public RefundResultBo doRefund(RefundOrder refundOrder) {
        YeepaySdkCredential credential = yeepayDirectConfigAssembler.buildConfig(
                refundOrder.getMchNo(), refundOrder.getChannelMchNo(), refundOrder.getCapability());
        return yeepayRefundService.refund(refundOrder, credential);
    }
}
