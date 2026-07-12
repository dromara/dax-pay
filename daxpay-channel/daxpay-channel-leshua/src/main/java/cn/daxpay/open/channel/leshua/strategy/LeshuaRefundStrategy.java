package cn.daxpay.open.channel.leshua.strategy;

import cn.daxpay.open.channel.leshua.client.credential.LeshuaSdkCredential;
import cn.daxpay.open.channel.leshua.service.isv.LeshuaIsvConfigAssembler;
import cn.daxpay.open.channel.leshua.service.payment.LeshuaRefundService;
import cn.daxpay.open.payment.core.strategy.refund.AbsRefundStrategy;
import cn.daxpay.open.payment.core.trade.runtime.bo.RefundResultBo;
import cn.daxpay.open.payment.core.trade.order.entity.PayRefundOrder;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 乐刷服务商退款策略
///
/// 组装通道凭证(委托 [LeshuaIsvConfigAssembler]), 退款执行委托给 [LeshuaRefundService]。
@Slf4j
@Service
@RequiredArgsConstructor
public class LeshuaRefundStrategy extends AbsRefundStrategy {

    private final LeshuaRefundService leshuaRefundService;
    private final LeshuaIsvConfigAssembler leshuaIsvConfigAssembler;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.LESHUA_PAY;
    }

    @Override
    public RefundResultBo doRefund(PayRefundOrder refundOrder) {
        LeshuaSdkCredential credential = leshuaIsvConfigAssembler.buildConfig(
                refundOrder.getMchNo(), refundOrder.getChannelMchNo(), refundOrder.getCapability());
        return leshuaRefundService.refund(refundOrder, credential);
    }
}
