package cn.daxpay.open.channel.fuyou.strategy.refund;

import cn.daxpay.open.channel.fuyou.client.credential.FuyouDirectSdkCredential;
import cn.daxpay.open.channel.fuyou.service.direct.FuyouDirectConfigAssembler;
import cn.daxpay.open.channel.fuyou.service.payment.FuyouDirectRefundService;
import cn.daxpay.open.payment.strategy.refund.AbsRefundStrategy;
import cn.daxpay.open.payment.trade.runtime.bo.RefundResultBo;
import cn.daxpay.open.payment.trade.order.entity.RefundOrder;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 富友直连退款策略
@Slf4j
@Service
@RequiredArgsConstructor
public class FuyouDirectRefundStrategy extends AbsRefundStrategy {

    private final FuyouDirectRefundService fuyouDirectRefundService;
    private final FuyouDirectConfigAssembler fuyouDirectConfigAssembler;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.FUYOU_DIRECT;
    }

    @Override
    public RefundResultBo doRefund(RefundOrder refundOrder) {
        FuyouDirectSdkCredential credential = fuyouDirectConfigAssembler.buildConfig(
                refundOrder.getMchNo(), refundOrder.getChannelMchNo(), refundOrder.getCapability());
        return fuyouDirectRefundService.refund(refundOrder, credential);
    }
}
