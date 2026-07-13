package cn.daxpay.open.channel.vbill.strategy;

import cn.daxpay.open.channel.vbill.client.credential.VbillSdkCredential;
import cn.daxpay.open.channel.vbill.service.isv.VbillIsvConfigAssembler;
import cn.daxpay.open.channel.vbill.service.payment.VbillRefundService;
import cn.daxpay.open.payment.strategy.refund.AbsRefundStrategy;
import cn.daxpay.open.payment.trade.runtime.bo.RefundResultBo;
import cn.daxpay.open.payment.trade.order.entity.PayRefundOrder;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 随行付服务商退款策略
///
/// 从退款订单读取通道路由参数(channelMchNo / capability),
/// 组装通道凭证(委托 [VbillIsvConfigAssembler]), 退款执行委托给 [VbillRefundService]。
@Slf4j
@Service
@RequiredArgsConstructor
public class VbillRefundStrategy extends AbsRefundStrategy {

    private final VbillRefundService vbillRefundService;
    private final VbillIsvConfigAssembler vbillIsvConfigAssembler;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.VBILL_PAY;
    }

    @Override
    public RefundResultBo doRefund(PayRefundOrder refundOrder) {
        // 组装通道调用凭证
        VbillSdkCredential credential = vbillIsvConfigAssembler.buildConfig(
                refundOrder.getMchNo(), refundOrder.getChannelMchNo(), refundOrder.getCapability());
        return vbillRefundService.refund(refundOrder, credential);
    }
}
