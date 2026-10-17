package cn.daxpay.open.channel.epaylinks.strategy.refund;

import cn.daxpay.open.channel.epaylinks.client.credential.EpaylinksSdkCredential;
import cn.daxpay.open.channel.epaylinks.service.direct.EpaylinksDirectConfigAssembler;
import cn.daxpay.open.channel.epaylinks.service.payment.refund.EpaylinksRefundService;
import cn.daxpay.open.payment.strategy.refund.AbsRefundStrategy;
import cn.daxpay.open.payment.trade.runtime.bo.RefundResultBo;
import cn.daxpay.open.payment.trade.order.entity.RefundOrder;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 易票联直连退款策略
///
/// 易票联退款为同步受理即终态语义(Refund/V2, 无退款查询/退款回调),
/// 需原支付的易票联平台交易号(transactionNo, 来自支付回调/查单回执)。
@Slf4j
@Service
@RequiredArgsConstructor
public class EpaylinksDirectRefundStrategy extends AbsRefundStrategy {

    private final EpaylinksRefundService epaylinksRefundService;
    private final EpaylinksDirectConfigAssembler epaylinksDirectConfigAssembler;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.EPAY_LINKS;
    }

    @Override
    public RefundResultBo doRefund(RefundOrder refundOrder) {
        EpaylinksSdkCredential credential = epaylinksDirectConfigAssembler.buildConfig(
                refundOrder.getMchNo(), refundOrder.getChannelMchNo(), refundOrder.getCapability());
        return epaylinksRefundService.refund(refundOrder, credential);
    }
}
