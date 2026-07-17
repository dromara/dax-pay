package cn.daxpay.open.channel.adapay.strategy;

import cn.daxpay.open.channel.adapay.client.credential.AdapaySdkCredential;
import cn.daxpay.open.channel.adapay.service.direct.AdapayDirectConfigAssembler;
import cn.daxpay.open.channel.adapay.service.payment.refund.AdapayRefundSyncService;
import cn.daxpay.open.payment.strategy.refund.AbsSyncRefundStrategy;
import cn.daxpay.open.payment.trade.runtime.bo.RefundResultBo;
import cn.daxpay.open.payment.trade.order.entity.RefundOrder;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # Adapay 直连退款同步策略
@Slf4j
@Service
@RequiredArgsConstructor
public class AdapayDirectSyncRefundStrategy extends AbsSyncRefundStrategy {

    private final AdapayRefundSyncService adapayRefundSyncService;
    private final AdapayDirectConfigAssembler adapayDirectConfigAssembler;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.ADA_PAY;
    }

    @Override
    public RefundResultBo doSync(RefundOrder refundOrder) {
        AdapaySdkCredential credential = adapayDirectConfigAssembler.buildConfig(
                refundOrder.getMchNo(), refundOrder.getChannelMchNo(), refundOrder.getCapability());
        return adapayRefundSyncService.sync(refundOrder, credential);
    }
}
