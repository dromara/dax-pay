package cn.daxpay.open.channel.fuyou.strategy.refund;

import cn.daxpay.open.channel.fuyou.client.credential.FuyouSdkCredential;
import cn.daxpay.open.channel.fuyou.service.isv.FuyouIsvConfigAssembler;
import cn.daxpay.open.channel.fuyou.service.payment.FuyouRefundService;
import cn.daxpay.open.payment.strategy.refund.AbsRefundStrategy;
import cn.daxpay.open.payment.trade.runtime.bo.RefundResultBo;
import cn.daxpay.open.payment.trade.order.entity.RefundOrder;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 富友服务商退款策略
@Slf4j
@Service
@RequiredArgsConstructor
public class FuyouRefundStrategy extends AbsRefundStrategy {

    private final FuyouRefundService fuyouRefundService;
    private final FuyouIsvConfigAssembler fuyouIsvConfigAssembler;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.FUYOU_PAY;
    }

    @Override
    public RefundResultBo doRefund(RefundOrder refundOrder) {
        FuyouSdkCredential credential = fuyouIsvConfigAssembler.buildConfig(
                refundOrder.getMchNo(), refundOrder.getChannelMchNo(), refundOrder.getCapability());
        return fuyouRefundService.refund(refundOrder, credential);
    }
}
