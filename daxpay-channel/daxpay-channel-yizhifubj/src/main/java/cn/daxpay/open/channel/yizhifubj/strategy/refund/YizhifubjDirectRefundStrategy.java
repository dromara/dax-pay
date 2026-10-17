package cn.daxpay.open.channel.yizhifubj.strategy.refund;

import cn.daxpay.open.channel.yizhifubj.client.credential.YizhifubjSdkCredential;
import cn.daxpay.open.channel.yizhifubj.service.direct.YizhifubjDirectConfigAssembler;
import cn.daxpay.open.channel.yizhifubj.service.payment.refund.YizhifubjRefundService;
import cn.daxpay.open.payment.strategy.refund.AbsRefundStrategy;
import cn.daxpay.open.payment.trade.runtime.bo.RefundResultBo;
import cn.daxpay.open.payment.trade.order.entity.RefundOrder;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 一直付直连退款策略
@Slf4j
@Service
@RequiredArgsConstructor
public class YizhifubjDirectRefundStrategy extends AbsRefundStrategy {

    private final YizhifubjRefundService yizhifubjRefundService;
    private final YizhifubjDirectConfigAssembler yizhifubjDirectConfigAssembler;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.YIZHIFU_PAY;
    }

    @Override
    public RefundResultBo doRefund(RefundOrder refundOrder) {
        YizhifubjSdkCredential credential = yizhifubjDirectConfigAssembler.buildConfig(
                refundOrder.getMchNo(), refundOrder.getChannelMchNo(), refundOrder.getCapability());
        return yizhifubjRefundService.refund(refundOrder, credential);
    }
}
