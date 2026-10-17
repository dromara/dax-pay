package cn.daxpay.open.channel.shouqianba.strategy.refund;

import cn.daxpay.open.channel.shouqianba.client.credential.ShouqianbaSdkCredential;
import cn.daxpay.open.channel.shouqianba.service.direct.ShouqianbaDirectConfigAssembler;
import cn.daxpay.open.channel.shouqianba.service.payment.refund.ShouqianbaRefundService;
import cn.daxpay.open.payment.strategy.refund.AbsRefundStrategy;
import cn.daxpay.open.payment.trade.runtime.bo.RefundResultBo;
import cn.daxpay.open.payment.trade.order.entity.RefundOrder;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 收钱吧直连退款策略
///
/// 退款为同步终态(REFUND_SUCCESS 即成功), 原支付收钱吧流水号 sn 缺失时(未收到回调/查单)
/// 通道侧会拒绝, 由子应用错误透传。
@Slf4j
@Service
@RequiredArgsConstructor
public class ShouqianbaDirectRefundStrategy extends AbsRefundStrategy {

    private final ShouqianbaRefundService shouqianbaRefundService;
    private final ShouqianbaDirectConfigAssembler shouqianbaDirectConfigAssembler;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.SHOUQIANBA;
    }

    @Override
    public RefundResultBo doRefund(RefundOrder refundOrder) {
        ShouqianbaSdkCredential credential = shouqianbaDirectConfigAssembler.buildConfig(
                refundOrder.getMchNo(), refundOrder.getChannelMchNo(), refundOrder.getCapability());
        return shouqianbaRefundService.refund(refundOrder, credential);
    }
}
