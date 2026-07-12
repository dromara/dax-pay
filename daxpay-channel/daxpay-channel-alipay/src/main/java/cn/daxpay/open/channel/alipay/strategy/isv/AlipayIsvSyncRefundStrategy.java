package cn.daxpay.open.channel.alipay.strategy.isv;

import cn.daxpay.open.channel.alipay.client.credential.AlipaySdkCredential;
import cn.daxpay.open.channel.alipay.service.isv.AlipayIsvConfigAssembler;
import cn.daxpay.open.channel.alipay.service.payment.refund.AlipayRefundSyncService;
import cn.daxpay.open.payment.core.strategy.refund.AbsSyncRefundStrategy;
import cn.daxpay.open.payment.core.trade.runtime.bo.RefundResultBo;
import cn.daxpay.open.payment.core.trade.order.entity.PayRefundOrder;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 支付宝服务商退款同步策略
///
/// 支付宝服务商模式(ProductEnum.ALIPAY_ISV)下的退款同步策略。
/// 组装通道凭证(委托 [AlipayIsvConfigAssembler], 含应用授权令牌), 同步执行委托给 [AlipayRefundSyncService]。
@Slf4j
@Service
@RequiredArgsConstructor
public class AlipayIsvSyncRefundStrategy extends AbsSyncRefundStrategy {

    private final AlipayRefundSyncService alipayRefundSyncService;
    private final AlipayIsvConfigAssembler alipayIsvConfigAssembler;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.ALIPAY_ISV;
    }

    @Override
    public RefundResultBo doSync(PayRefundOrder refundOrder) {
        // 组装服务商模式通道调用凭证(含应用授权令牌)
        AlipaySdkCredential credential = alipayIsvConfigAssembler.buildConfig(refundOrder.getMchNo());
        return alipayRefundSyncService.sync(refundOrder, credential);
    }
}
