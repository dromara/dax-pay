package cn.daxpay.open.channel.lakala.strategy.refund;

import cn.daxpay.open.channel.lakala.client.credential.MossSdkCredential;
import cn.daxpay.open.channel.lakala.service.isv.LakalaMossConfigAssembler;
import cn.daxpay.open.channel.lakala.service.moss.LakalaMossRefundService;
import cn.daxpay.open.payment.strategy.refund.AbsRefundStrategy;
import cn.daxpay.open.payment.trade.runtime.bo.RefundResultBo;
import cn.daxpay.open.payment.trade.order.entity.RefundOrder;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 拉卡拉 MOSS 退款策略
///
/// 从退款订单读取通道路由参数(channelMchNo / capability),
/// 组装通道凭证(委托 [LakalaMossConfigAssembler]), 退款执行委托给 [LakalaMossRefundService]。
@Slf4j
@Service
@RequiredArgsConstructor
public class LakalaMossRefundStrategy extends AbsRefundStrategy {

    private final LakalaMossRefundService lakalaMossRefundService;
    private final LakalaMossConfigAssembler lakalaMossConfigAssembler;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.LAKALA_MOSS;
    }

    @Override
    public RefundResultBo doRefund(RefundOrder refundOrder) {
        // 组装通道调用凭证
        MossSdkCredential credential = lakalaMossConfigAssembler.buildConfig(
                refundOrder.getMchNo(), refundOrder.getChannelMchNo(), refundOrder.getCapability());
        return lakalaMossRefundService.refund(refundOrder, credential);
    }
}
