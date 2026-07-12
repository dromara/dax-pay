package cn.daxpay.open.channel.ums.strategy;

import cn.daxpay.open.channel.ums.client.credential.UmsSdkCredential;
import cn.daxpay.open.channel.ums.service.direct.UmsDirectConfigAssembler;
import cn.daxpay.open.channel.ums.service.payment.refund.UmsRefundService;
import cn.daxpay.open.payment.core.strategy.refund.AbsRefundStrategy;
import cn.daxpay.open.payment.core.trade.runtime.bo.RefundResultBo;
import cn.daxpay.open.payment.core.trade.order.entity.PayRefundOrder;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 银联商务直连退款策略
@Slf4j
@Service
@RequiredArgsConstructor
public class UmsDirectRefundStrategy extends AbsRefundStrategy {

    private final UmsRefundService umsRefundService;
    private final UmsDirectConfigAssembler umsDirectConfigAssembler;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.UMS_QRCODE;
    }

    @Override
    public RefundResultBo doRefund(PayRefundOrder refundOrder) {
        UmsSdkCredential credential = umsDirectConfigAssembler.buildConfig(
                refundOrder.getMchNo(), refundOrder.getChannelMchNo(), refundOrder.getCapability());
        return umsRefundService.refund(refundOrder, credential);
    }
}
