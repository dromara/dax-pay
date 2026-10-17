package cn.daxpay.open.channel.helipay.strategy.refund;

import cn.daxpay.open.channel.helipay.client.credential.HelipaySdkCredential;
import cn.daxpay.open.channel.helipay.service.config.HelipayConfigAssembler;
import cn.daxpay.open.channel.helipay.service.payment.HelipayRefundService;
import cn.daxpay.open.payment.strategy.refund.AbsRefundStrategy;
import cn.daxpay.open.payment.trade.runtime.bo.RefundResultBo;
import cn.daxpay.open.payment.trade.order.entity.RefundOrder;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 合利宝直连退款策略
///
/// 从退款订单读取通道路由参数(channelMchNo / capability),
/// 组装通道凭证(委托 [HelipayConfigAssembler]), 退款执行委托给 [HelipayRefundService]。
@Slf4j
@Service
@RequiredArgsConstructor
public class HelipayDirectRefundStrategy extends AbsRefundStrategy {

    private final HelipayRefundService helipayRefundService;

    private final HelipayConfigAssembler helipayConfigAssembler;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.HELI_PAY;
    }

    @Override
    public RefundResultBo doRefund(RefundOrder refundOrder) {
        // 组装通道调用凭证
        HelipaySdkCredential credential = helipayConfigAssembler.buildConfig(
                refundOrder.getMchNo(), refundOrder.getChannelMchNo(), refundOrder.getCapability());
        return helipayRefundService.refund(refundOrder, credential);
    }
}
