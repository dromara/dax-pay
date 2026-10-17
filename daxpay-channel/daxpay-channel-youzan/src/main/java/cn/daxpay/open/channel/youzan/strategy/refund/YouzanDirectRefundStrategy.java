package cn.daxpay.open.channel.youzan.strategy.refund;

import cn.daxpay.open.channel.youzan.client.credential.YouzanSdkCredential;
import cn.daxpay.open.channel.youzan.service.direct.YouzanDirectConfigAssembler;
import cn.daxpay.open.channel.youzan.service.payment.refund.YouzanRefundService;
import cn.daxpay.open.payment.strategy.refund.AbsRefundStrategy;
import cn.daxpay.open.payment.trade.runtime.bo.RefundResultBo;
import cn.daxpay.open.payment.trade.order.entity.RefundOrder;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 有赞直连退款策略
@Slf4j
@Service
@RequiredArgsConstructor
public class YouzanDirectRefundStrategy extends AbsRefundStrategy {

    private final YouzanRefundService youzanRefundService;
    private final YouzanDirectConfigAssembler youzanDirectConfigAssembler;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.YOUZAN_PAY;
    }

    @Override
    public RefundResultBo doRefund(RefundOrder refundOrder) {
        YouzanSdkCredential credential = youzanDirectConfigAssembler.buildConfig(
                refundOrder.getMchNo(), refundOrder.getChannelMchNo(), refundOrder.getCapability());
        return youzanRefundService.refund(refundOrder, credential);
    }
}
