package cn.daxpay.open.channel.yisheng.strategy.refund;

import cn.daxpay.open.channel.yisheng.client.credential.YishengSdkCredential;
import cn.daxpay.open.channel.yisheng.service.direct.YishengDirectConfigAssembler;
import cn.daxpay.open.channel.yisheng.service.payment.refund.YishengRefundService;
import cn.daxpay.open.payment.strategy.refund.AbsRefundStrategy;
import cn.daxpay.open.payment.trade.runtime.bo.RefundResultBo;
import cn.daxpay.open.payment.trade.order.entity.RefundOrder;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 易生直连退款策略
@Slf4j
@Service
@RequiredArgsConstructor
public class YishengDirectRefundStrategy extends AbsRefundStrategy {

    private final YishengRefundService yishengRefundService;
    private final YishengDirectConfigAssembler yishengDirectConfigAssembler;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.YISHENG_PAY;
    }

    @Override
    public RefundResultBo doRefund(RefundOrder refundOrder) {
        YishengSdkCredential credential = yishengDirectConfigAssembler.buildConfig(
                refundOrder.getMchNo(), refundOrder.getChannelMchNo(), refundOrder.getCapability());
        return yishengRefundService.refund(refundOrder, credential);
    }
}
