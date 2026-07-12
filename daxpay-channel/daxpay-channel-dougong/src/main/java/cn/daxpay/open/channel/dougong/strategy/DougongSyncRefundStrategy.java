package cn.daxpay.open.channel.dougong.strategy;

import cn.daxpay.open.channel.dougong.client.credential.DougongSdkCredential;
import cn.daxpay.open.channel.dougong.service.isv.DougongIsvConfigAssembler;
import cn.daxpay.open.channel.dougong.service.payment.DougongRefundSyncService;
import cn.daxpay.open.payment.core.strategy.refund.AbsSyncRefundStrategy;
import cn.daxpay.open.payment.core.trade.runtime.bo.RefundResultBo;
import cn.daxpay.open.payment.core.trade.order.entity.PayRefundOrder;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 斗拱服务商退款同步策略
@Slf4j
@Service
@RequiredArgsConstructor
public class DougongSyncRefundStrategy extends AbsSyncRefundStrategy {

    private final DougongRefundSyncService dougongRefundSyncService;
    private final DougongIsvConfigAssembler dougongIsvConfigAssembler;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.DOUGONG_PAY;
    }

    @Override
    public RefundResultBo doSync(PayRefundOrder refundOrder) {
        DougongSdkCredential credential = dougongIsvConfigAssembler.buildConfig(
                refundOrder.getMchNo(), refundOrder.getChannelMchNo(), refundOrder.getCapability());
        return dougongRefundSyncService.sync(refundOrder, credential);
    }
}
