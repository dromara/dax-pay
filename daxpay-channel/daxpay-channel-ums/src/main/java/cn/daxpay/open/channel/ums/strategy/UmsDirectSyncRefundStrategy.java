package cn.daxpay.open.channel.ums.strategy;

import cn.daxpay.open.channel.ums.client.credential.UmsSdkCredential;
import cn.daxpay.open.channel.ums.service.direct.UmsDirectConfigAssembler;
import cn.daxpay.open.channel.ums.service.payment.refund.UmsRefundSyncService;
import cn.daxpay.open.payment.strategy.refund.AbsSyncRefundStrategy;
import cn.daxpay.open.payment.trade.runtime.bo.RefundResultBo;
import cn.daxpay.open.payment.trade.order.entity.PayRefundOrder;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 银联商务直连退款同步策略
@Slf4j
@Service
@RequiredArgsConstructor
public class UmsDirectSyncRefundStrategy extends AbsSyncRefundStrategy {

    private final UmsRefundSyncService umsRefundSyncService;
    private final UmsDirectConfigAssembler umsDirectConfigAssembler;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.UMS_QRCODE;
    }

    @Override
    public RefundResultBo doSync(PayRefundOrder refundOrder) {
        UmsSdkCredential credential = umsDirectConfigAssembler.buildConfig(
                refundOrder.getMchNo(), refundOrder.getChannelMchNo(), refundOrder.getCapability());
        return umsRefundSyncService.sync(refundOrder, credential);
    }
}
