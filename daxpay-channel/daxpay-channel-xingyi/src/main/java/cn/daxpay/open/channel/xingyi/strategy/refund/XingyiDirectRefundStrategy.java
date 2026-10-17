package cn.daxpay.open.channel.xingyi.strategy.refund;

import cn.daxpay.open.channel.xingyi.client.credential.XingyiSdkCredential;
import cn.daxpay.open.channel.xingyi.service.direct.XingyiDirectConfigAssembler;
import cn.daxpay.open.channel.xingyi.service.payment.refund.XingyiRefundService;
import cn.daxpay.open.payment.strategy.refund.AbsRefundStrategy;
import cn.daxpay.open.payment.trade.runtime.bo.RefundResultBo;
import cn.daxpay.open.payment.trade.order.entity.RefundOrder;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 星驿付直连退款策略
@Slf4j
@Service
@RequiredArgsConstructor
public class XingyiDirectRefundStrategy extends AbsRefundStrategy {

    private final XingyiRefundService xingyiRefundService;

    private final XingyiDirectConfigAssembler xingyiDirectConfigAssembler;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.XINGYI_PAY;
    }

    @Override
    public RefundResultBo doRefund(RefundOrder refundOrder) {
        XingyiSdkCredential credential = xingyiDirectConfigAssembler.buildConfig(
                refundOrder.getMchNo(), refundOrder.getChannelMchNo(), refundOrder.getCapability());
        return xingyiRefundService.refund(refundOrder, credential);
    }
}
