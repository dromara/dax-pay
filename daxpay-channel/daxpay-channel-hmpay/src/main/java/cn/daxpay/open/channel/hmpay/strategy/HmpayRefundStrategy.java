package cn.daxpay.open.channel.hmpay.strategy;

import cn.daxpay.open.channel.hmpay.client.credential.HmpaySdkCredential;
import cn.daxpay.open.channel.hmpay.service.isv.HmpayIsvConfigAssembler;
import cn.daxpay.open.channel.hmpay.service.payment.HmpayRefundService;
import cn.daxpay.open.payment.core.strategy.refund.AbsRefundStrategy;
import cn.daxpay.open.payment.core.trade.runtime.bo.RefundResultBo;
import cn.daxpay.open.payment.core.trade.order.entity.PayRefundOrder;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 河马付服务商退款策略
@Slf4j
@Service
@RequiredArgsConstructor
public class HmpayRefundStrategy extends AbsRefundStrategy {

    private final HmpayRefundService hmpayRefundService;
    private final HmpayIsvConfigAssembler hmpayIsvConfigAssembler;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.HM_PAY;
    }

    @Override
    public RefundResultBo doRefund(PayRefundOrder refundOrder) {
        HmpaySdkCredential credential = hmpayIsvConfigAssembler.buildConfig(
                refundOrder.getMchNo(), refundOrder.getChannelMchNo(), refundOrder.getCapability());
        return hmpayRefundService.refund(refundOrder, credential);
    }
}
