package cn.daxpay.open.channel.jeepay.strategy.refund;

import cn.daxpay.open.channel.jeepay.client.credential.JeepaySdkCredential;
import cn.daxpay.open.channel.jeepay.service.config.JeepayConfigAssembler;
import cn.daxpay.open.channel.jeepay.service.payment.JeepayRefundSyncService;
import cn.daxpay.open.payment.strategy.refund.AbsSyncRefundStrategy;
import cn.daxpay.open.payment.trade.runtime.bo.RefundResultBo;
import cn.daxpay.open.payment.trade.order.entity.RefundOrder;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # Jeepay退款同步策略
///
/// 从退款订单读取通道路由参数(channelMchNo / capability),
/// 组装通道凭证(委托 [JeepayConfigAssembler]), 同步执行委托给 [JeepayRefundSyncService]。
@Slf4j
@Service
@RequiredArgsConstructor
public class JeepaySyncRefundStrategy extends AbsSyncRefundStrategy {

    private final JeepayRefundSyncService jeepayRefundSyncService;

    private final JeepayConfigAssembler jeepayConfigAssembler;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.JEE_PAY;
    }

    @Override
    public RefundResultBo doSync(RefundOrder refundOrder) {
        // 组装通道调用凭证
        JeepaySdkCredential credential = jeepayConfigAssembler.buildConfig(
                refundOrder.getMchNo(), refundOrder.getChannelMchNo(), refundOrder.getCapability());
        return jeepayRefundSyncService.sync(refundOrder, credential);
    }
}
