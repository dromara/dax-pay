package cn.daxpay.open.channel.douyin.strategy.refund;

import cn.daxpay.open.channel.douyin.client.credential.DouyinSdkCredential;
import cn.daxpay.open.channel.douyin.service.direct.DouyinDirectConfigAssembler;
import cn.daxpay.open.channel.douyin.service.payment.refund.DouyinRefundSyncService;
import cn.daxpay.open.payment.strategy.refund.AbsSyncRefundStrategy;
import cn.daxpay.open.payment.trade.runtime.bo.RefundResultBo;
import cn.daxpay.open.payment.trade.order.entity.RefundOrder;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 抖音直连退款同步策略
///
/// 抖音直连模式(ProductEnum.DOUYIN_PAY)下的退款同步策略。
/// 组装通道凭证(委托 [DouyinDirectConfigAssembler]), 同步执行委托给 [DouyinRefundSyncService]。
@Slf4j
@Service
@RequiredArgsConstructor
public class DouyinDirectSyncRefundStrategy extends AbsSyncRefundStrategy {

    private final DouyinRefundSyncService douyinRefundSyncService;
    private final DouyinDirectConfigAssembler douyinDirectConfigAssembler;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.DOUYIN_PAY;
    }

    @Override
    public RefundResultBo doSync(RefundOrder refundOrder) {
        // channelAppId 为退款单快照(继承原支付单, 保证退款同步与下单同一应用)
        DouyinSdkCredential credential = douyinDirectConfigAssembler.buildConfig(
                refundOrder.getMchNo(), refundOrder.getChannelMchNo(), refundOrder.getCapability(),
                refundOrder.getChannelAppId());
        return douyinRefundSyncService.sync(refundOrder, credential);
    }
}
