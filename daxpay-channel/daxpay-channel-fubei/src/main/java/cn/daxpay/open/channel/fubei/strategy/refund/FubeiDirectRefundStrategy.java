package cn.daxpay.open.channel.fubei.strategy.refund;

import cn.daxpay.open.channel.fubei.client.credential.FubeiSdkCredential;
import cn.daxpay.open.channel.fubei.service.direct.FubeiDirectConfigAssembler;
import cn.daxpay.open.channel.fubei.service.payment.refund.FubeiRefundService;
import cn.daxpay.open.payment.strategy.refund.AbsRefundStrategy;
import cn.daxpay.open.payment.trade.runtime.bo.RefundResultBo;
import cn.daxpay.open.payment.trade.order.entity.RefundOrder;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 付呗直连退款策略
@Slf4j
@Service
@RequiredArgsConstructor
public class FubeiDirectRefundStrategy extends AbsRefundStrategy {

    private final FubeiRefundService fubeiRefundService;
    private final FubeiDirectConfigAssembler fubeiDirectConfigAssembler;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.FUBEI_PAY;
    }

    @Override
    public RefundResultBo doRefund(RefundOrder refundOrder) {
        FubeiSdkCredential credential = fubeiDirectConfigAssembler.buildConfig(
                refundOrder.getMchNo(), refundOrder.getChannelMchNo(), refundOrder.getCapability());
        return fubeiRefundService.refund(refundOrder, credential);
    }
}
