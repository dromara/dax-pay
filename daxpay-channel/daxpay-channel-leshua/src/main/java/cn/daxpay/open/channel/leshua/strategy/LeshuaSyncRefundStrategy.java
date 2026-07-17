package cn.daxpay.open.channel.leshua.strategy;

import cn.daxpay.open.channel.leshua.client.credential.LeshuaSdkCredential;
import cn.daxpay.open.channel.leshua.service.isv.LeshuaIsvConfigAssembler;
import cn.daxpay.open.channel.leshua.service.payment.LeshuaRefundSyncService;
import cn.daxpay.open.payment.strategy.refund.AbsSyncRefundStrategy;
import cn.daxpay.open.payment.trade.runtime.bo.RefundResultBo;
import cn.daxpay.open.payment.trade.order.entity.RefundOrder;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 乐刷服务商退款同步策略
///
/// 组装通道凭证(委托 [LeshuaIsvConfigAssembler]), 同步执行委托给 [LeshuaRefundSyncService]。
@Slf4j
@Service
@RequiredArgsConstructor
public class LeshuaSyncRefundStrategy extends AbsSyncRefundStrategy {

    private final LeshuaRefundSyncService leshuaRefundSyncService;
    private final LeshuaIsvConfigAssembler leshuaIsvConfigAssembler;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.LESHUA_PAY;
    }

    @Override
    public RefundResultBo doSync(RefundOrder refundOrder) {
        LeshuaSdkCredential credential = leshuaIsvConfigAssembler.buildConfig(
                refundOrder.getMchNo(), refundOrder.getChannelMchNo(), refundOrder.getCapability());
        return leshuaRefundSyncService.sync(refundOrder, credential);
    }
}
