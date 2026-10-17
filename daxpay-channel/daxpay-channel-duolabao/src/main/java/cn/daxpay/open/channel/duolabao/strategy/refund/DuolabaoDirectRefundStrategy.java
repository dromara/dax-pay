package cn.daxpay.open.channel.duolabao.strategy.refund;

import cn.daxpay.open.channel.duolabao.client.credential.DuolabaoSdkCredential;
import cn.daxpay.open.channel.duolabao.service.direct.DuolabaoDirectConfigAssembler;
import cn.daxpay.open.channel.duolabao.service.payment.refund.DuolabaoRefundService;
import cn.daxpay.open.payment.strategy.refund.AbsRefundStrategy;
import cn.daxpay.open.payment.trade.runtime.bo.RefundResultBo;
import cn.daxpay.open.payment.trade.order.entity.RefundOrder;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 哆啦宝直连退款策略
@Slf4j
@Service
@RequiredArgsConstructor
public class DuolabaoDirectRefundStrategy extends AbsRefundStrategy {

    private final DuolabaoRefundService duolabaoRefundService;

    private final DuolabaoDirectConfigAssembler duolabaoDirectConfigAssembler;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.DUOLABAO_PAY;
    }

    @Override
    public RefundResultBo doRefund(RefundOrder refundOrder) {
        DuolabaoSdkCredential credential = duolabaoDirectConfigAssembler.buildConfig(
                refundOrder.getMchNo(), refundOrder.getChannelMchNo(), refundOrder.getCapability());
        return duolabaoRefundService.refund(refundOrder, credential);
    }
}
