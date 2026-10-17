package cn.daxpay.open.channel.sand.strategy.refund;

import cn.daxpay.open.channel.sand.client.credential.SandSdkCredential;
import cn.daxpay.open.channel.sand.service.direct.SandDirectConfigAssembler;
import cn.daxpay.open.channel.sand.service.payment.refund.SandRefundService;
import cn.daxpay.open.payment.strategy.refund.AbsRefundStrategy;
import cn.daxpay.open.payment.trade.runtime.bo.RefundResultBo;
import cn.daxpay.open.payment.trade.order.entity.RefundOrder;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 杉德直连退款策略
@Slf4j
@Service
@RequiredArgsConstructor
public class SandDirectRefundStrategy extends AbsRefundStrategy {

    private final SandRefundService sandRefundService;
    private final SandDirectConfigAssembler sandDirectConfigAssembler;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.SAND_PAY;
    }

    @Override
    public RefundResultBo doRefund(RefundOrder refundOrder) {
        SandSdkCredential credential = sandDirectConfigAssembler.buildConfig(
                refundOrder.getMchNo(), refundOrder.getChannelMchNo(), refundOrder.getCapability());
        return sandRefundService.refund(refundOrder, credential);
    }
}
