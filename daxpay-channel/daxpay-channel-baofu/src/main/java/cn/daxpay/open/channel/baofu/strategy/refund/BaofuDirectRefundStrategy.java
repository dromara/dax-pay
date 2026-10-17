package cn.daxpay.open.channel.baofu.strategy.refund;

import cn.daxpay.open.channel.baofu.client.credential.BaofuSdkCredential;
import cn.daxpay.open.channel.baofu.service.direct.BaofuDirectConfigAssembler;
import cn.daxpay.open.channel.baofu.service.payment.refund.BaofuRefundService;
import cn.daxpay.open.payment.strategy.refund.AbsRefundStrategy;
import cn.daxpay.open.payment.trade.runtime.bo.RefundResultBo;
import cn.daxpay.open.payment.trade.order.entity.RefundOrder;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 宝付直连退款策略
///
/// 宝付退款为同步终态(受理成功即成功), 退款结果在 [BaofuRefundService] 内同步落定。
@Slf4j
@Service
@RequiredArgsConstructor
public class BaofuDirectRefundStrategy extends AbsRefundStrategy {

    private final BaofuRefundService baofuRefundService;
    private final BaofuDirectConfigAssembler baofuDirectConfigAssembler;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.BAOFU_PAY;
    }

    @Override
    public RefundResultBo doRefund(RefundOrder refundOrder) {
        BaofuSdkCredential credential = baofuDirectConfigAssembler.buildConfig(
                refundOrder.getMchNo(), refundOrder.getChannelMchNo(), refundOrder.getCapability());
        return baofuRefundService.refund(refundOrder, credential);
    }
}
