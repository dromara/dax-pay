package cn.daxpay.open.channel.cpcn.strategy.refund;

import cn.daxpay.open.channel.cpcn.client.credential.CpcnSdkCredential;
import cn.daxpay.open.channel.cpcn.service.direct.CpcnDirectConfigAssembler;
import cn.daxpay.open.channel.cpcn.service.payment.refund.CpcnRefundService;
import cn.daxpay.open.payment.strategy.refund.AbsRefundStrategy;
import cn.daxpay.open.payment.trade.runtime.bo.RefundResultBo;
import cn.daxpay.open.payment.trade.order.entity.RefundOrder;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 中金直连退款策略
@Slf4j
@Service
@RequiredArgsConstructor
public class CpcnDirectRefundStrategy extends AbsRefundStrategy {

    private final CpcnRefundService cpcnRefundService;
    private final CpcnDirectConfigAssembler cpcnDirectConfigAssembler;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.CPCN_PAY;
    }

    @Override
    public RefundResultBo doRefund(RefundOrder refundOrder) {
        CpcnSdkCredential credential = cpcnDirectConfigAssembler.buildConfig(
                refundOrder.getMchNo(), refundOrder.getChannelMchNo(), refundOrder.getCapability());
        return cpcnRefundService.refund(refundOrder, credential);
    }
}
