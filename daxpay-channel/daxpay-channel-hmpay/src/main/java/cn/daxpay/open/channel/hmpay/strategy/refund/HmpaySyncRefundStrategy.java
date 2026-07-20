package cn.daxpay.open.channel.hmpay.strategy.refund;

import cn.daxpay.open.channel.hmpay.client.credential.HmpaySdkCredential;
import cn.daxpay.open.channel.hmpay.service.isv.HmpayIsvConfigAssembler;
import cn.daxpay.open.channel.hmpay.service.payment.HmpayRefundSyncService;
import cn.daxpay.open.payment.strategy.refund.AbsSyncRefundStrategy;
import cn.daxpay.open.payment.trade.runtime.bo.RefundResultBo;
import cn.daxpay.open.payment.trade.order.entity.RefundOrder;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 河马付服务商退款同步策略
@Slf4j
@Service
@RequiredArgsConstructor
public class HmpaySyncRefundStrategy extends AbsSyncRefundStrategy {

    private final HmpayRefundSyncService hmpayRefundSyncService;
    private final HmpayIsvConfigAssembler hmpayIsvConfigAssembler;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.HM_PAY;
    }

    @Override
    public RefundResultBo doSync(RefundOrder refundOrder) {
        HmpaySdkCredential credential = hmpayIsvConfigAssembler.buildConfig(
                refundOrder.getMchNo(), refundOrder.getChannelMchNo(), refundOrder.getCapability());
        return hmpayRefundSyncService.sync(refundOrder, credential);
    }
}
