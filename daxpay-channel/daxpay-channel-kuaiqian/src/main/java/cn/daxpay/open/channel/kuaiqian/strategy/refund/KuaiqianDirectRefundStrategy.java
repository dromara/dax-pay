package cn.daxpay.open.channel.kuaiqian.strategy.refund;

import cn.daxpay.open.channel.kuaiqian.client.credential.KuaiqianSdkCredential;
import cn.daxpay.open.channel.kuaiqian.service.direct.KuaiqianDirectConfigAssembler;
import cn.daxpay.open.channel.kuaiqian.service.payment.refund.KuaiqianRefundService;
import cn.daxpay.open.payment.strategy.refund.AbsRefundStrategy;
import cn.daxpay.open.payment.trade.runtime.bo.RefundResultBo;
import cn.daxpay.open.payment.trade.order.entity.RefundOrder;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 快钱直连退款策略
@Slf4j
@Service
@RequiredArgsConstructor
public class KuaiqianDirectRefundStrategy extends AbsRefundStrategy {

    private final KuaiqianRefundService kuaiqianRefundService;
    private final KuaiqianDirectConfigAssembler kuaiqianDirectConfigAssembler;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.KUAIQIAN_PAY;
    }

    @Override
    public RefundResultBo doRefund(RefundOrder refundOrder) {
        KuaiqianSdkCredential credential = kuaiqianDirectConfigAssembler.buildConfig(
                refundOrder.getMchNo(), refundOrder.getChannelMchNo(), refundOrder.getCapability());
        return kuaiqianRefundService.refund(refundOrder, credential);
    }
}
