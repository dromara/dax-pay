package cn.daxpay.open.channel.fuyou.strategy.refund;

import cn.daxpay.open.channel.fuyou.client.credential.FuyouDirectSdkCredential;
import cn.daxpay.open.channel.fuyou.service.direct.FuyouDirectConfigAssembler;
import cn.daxpay.open.channel.fuyou.service.payment.FuyouDirectRefundSyncService;
import cn.daxpay.open.payment.strategy.refund.AbsSyncRefundStrategy;
import cn.daxpay.open.payment.trade.runtime.bo.RefundResultBo;
import cn.daxpay.open.payment.trade.order.entity.RefundOrder;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 富友直连退款同步策略
@Slf4j
@Service
@RequiredArgsConstructor
public class FuyouDirectSyncRefundStrategy extends AbsSyncRefundStrategy {

    private final FuyouDirectRefundSyncService fuyouDirectRefundSyncService;
    private final FuyouDirectConfigAssembler fuyouDirectConfigAssembler;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.FUYOU_DIRECT;
    }

    @Override
    public RefundResultBo doSync(RefundOrder refundOrder) {
        FuyouDirectSdkCredential credential = fuyouDirectConfigAssembler.buildConfig(
                refundOrder.getMchNo(), refundOrder.getChannelMchNo(), refundOrder.getCapability());
        return fuyouDirectRefundSyncService.sync(refundOrder, credential);
    }
}
