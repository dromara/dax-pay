package cn.daxpay.open.channel.ysep.strategy.refund;

import cn.daxpay.open.channel.ysep.client.credential.YsepSdkCredential;
import cn.daxpay.open.channel.ysep.service.config.YsepConfigAssembler;
import cn.daxpay.open.channel.ysep.service.payment.YsepRefundService;
import cn.daxpay.open.payment.strategy.refund.AbsRefundStrategy;
import cn.daxpay.open.payment.trade.order.entity.RefundOrder;
import cn.daxpay.open.payment.trade.runtime.bo.RefundResultBo;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 银盛退款策略(ysep_pay)
///
/// 从退款订单读取通道路由参数, 组装通道凭证(委托 [YsepConfigAssembler]),
/// 退款执行委托给 [YsepRefundService](走 ysepay.online.trade.refund)。
@Slf4j
@Service
@RequiredArgsConstructor
public class YsepRefundStrategy extends AbsRefundStrategy {

    private final YsepRefundService ysepRefundService;
    private final YsepConfigAssembler ysepConfigAssembler;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.YSEP_PAY;
    }

    @Override
    public RefundResultBo doRefund(RefundOrder refundOrder) {
        // 组装通道调用凭证
        YsepSdkCredential credential = ysepConfigAssembler.buildConfig(
                refundOrder.getMchNo(), refundOrder.getChannelMchNo(), refundOrder.getCapability());
        return ysepRefundService.refund(refundOrder, credential, ProductEnum.YSEP_PAY);
    }
}
