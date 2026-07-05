package cn.daxpay.open.channel.douyin.strategy;

import cn.daxpay.open.channel.douyin.client.credential.DouyinSdkCredential;
import cn.daxpay.open.channel.douyin.service.direct.DouyinDirectConfigAssembler;
import cn.daxpay.open.channel.douyin.service.payment.refund.DouyinRefundService;
import cn.daxpay.open.payment.core.strategy.refund.AbsRefundStrategy;
import cn.daxpay.open.payment.core.trade.bo.RefundResultBo;
import cn.daxpay.open.payment.core.trade.entity.PayRefundOrder;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 抖音直连退款策略
///
/// 抖音直连模式(ProductEnum.DOUYIN_PAY)下的退款策略。
/// 组装通道凭证(委托 [DouyinDirectConfigAssembler]), 退款执行委托给 [DouyinRefundService]。
@Slf4j
@Service
@RequiredArgsConstructor
public class DouyinDirectRefundStrategy extends AbsRefundStrategy {

    private final DouyinRefundService douyinRefundService;
    private final DouyinDirectConfigAssembler douyinDirectConfigAssembler;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.DOUYIN_PAY;
    }

    @Override
    public RefundResultBo doRefund(PayRefundOrder refundOrder) {
        DouyinSdkCredential credential = douyinDirectConfigAssembler.buildConfig(
                refundOrder.getMchNo(), refundOrder.getChannelMchNo(), refundOrder.getCapability());
        return douyinRefundService.refund(refundOrder, credential);
    }
}
