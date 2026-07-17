package cn.daxpay.open.channel.alipay.strategy.direct;

import cn.daxpay.open.channel.alipay.client.credential.AlipaySdkCredential;
import cn.daxpay.open.channel.alipay.service.direct.AlipayDirectConfigAssembler;
import cn.daxpay.open.channel.alipay.service.payment.refund.AlipayRefundSyncService;
import cn.daxpay.open.payment.strategy.refund.AbsSyncRefundStrategy;
import cn.daxpay.open.payment.trade.runtime.bo.RefundResultBo;
import cn.daxpay.open.payment.trade.order.entity.RefundOrder;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 支付宝直连退款同步策略
///
/// 支付宝直连模式(ProductEnum.ALIPAY)下的退款同步策略。
/// 组装通道凭证(委托 [AlipayDirectConfigAssembler]), 同步执行委托给 [AlipayRefundSyncService]。
@Slf4j
@Service
@RequiredArgsConstructor
public class AlipayDirectSyncRefundStrategy extends AbsSyncRefundStrategy {

    private final AlipayRefundSyncService alipayRefundSyncService;
    private final AlipayDirectConfigAssembler alipayDirectConfigAssembler;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.ALIPAY;
    }

    @Override
    public RefundResultBo doSync(RefundOrder refundOrder) {
        // 组装直连通道调用凭证
        AlipaySdkCredential credential = alipayDirectConfigAssembler.buildConfig(
                refundOrder.getMchNo(), refundOrder.getChannelMchNo(), refundOrder.getCapability());
        return alipayRefundSyncService.sync(refundOrder, credential);
    }
}
