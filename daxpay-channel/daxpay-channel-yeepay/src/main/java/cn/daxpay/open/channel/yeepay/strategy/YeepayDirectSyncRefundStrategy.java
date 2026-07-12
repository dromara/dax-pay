package cn.daxpay.open.channel.yeepay.strategy;

import cn.daxpay.open.channel.yeepay.client.credential.YeepaySdkCredential;
import cn.daxpay.open.channel.yeepay.service.direct.YeepayDirectConfigAssembler;
import cn.daxpay.open.channel.yeepay.service.payment.refund.YeepayRefundSyncService;
import cn.daxpay.open.payment.core.strategy.refund.AbsSyncRefundStrategy;
import cn.daxpay.open.payment.core.trade.runtime.bo.RefundResultBo;
import cn.daxpay.open.payment.core.trade.order.entity.PayRefundOrder;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 易宝直连退款同步策略
@Slf4j
@Service
@RequiredArgsConstructor
public class YeepayDirectSyncRefundStrategy extends AbsSyncRefundStrategy {

    private final YeepayRefundSyncService yeepayRefundSyncService;
    private final YeepayDirectConfigAssembler yeepayDirectConfigAssembler;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.YEE_PAY;
    }

    @Override
    public RefundResultBo doSync(PayRefundOrder refundOrder) {
        YeepaySdkCredential credential = yeepayDirectConfigAssembler.buildConfig(
                refundOrder.getMchNo(), refundOrder.getChannelMchNo(), refundOrder.getCapability());
        return yeepayRefundSyncService.sync(refundOrder, credential);
    }
}
