package cn.daxpay.open.channel.douyin.strategy;

import cn.daxpay.open.channel.douyin.client.credential.DouyinSdkCredential;
import cn.daxpay.open.channel.douyin.service.direct.DouyinDirectConfigAssembler;
import cn.daxpay.open.channel.douyin.service.payment.refund.DouyinRefundSyncService;
import cn.daxpay.open.payment.core.strategy.refund.AbsSyncRefundStrategy;
import cn.daxpay.open.payment.core.trade.runtime.bo.RefundResultBo;
import cn.daxpay.open.payment.core.trade.order.entity.PayRefundOrder;
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
    public RefundResultBo doSync(PayRefundOrder refundOrder) {
        DouyinSdkCredential credential = douyinDirectConfigAssembler.buildConfig(
                refundOrder.getMchNo(), refundOrder.getChannelMchNo(), refundOrder.getCapability());
        return douyinRefundSyncService.sync(refundOrder, credential);
    }
}
