package cn.daxpay.open.channel.dougong.strategy.refund;

import cn.daxpay.open.channel.dougong.client.credential.DougongSdkCredential;
import cn.daxpay.open.channel.dougong.service.isv.DougongIsvConfigAssembler;
import cn.daxpay.open.channel.dougong.service.payment.DougongRefundService;
import cn.daxpay.open.payment.strategy.refund.AbsRefundStrategy;
import cn.daxpay.open.payment.trade.runtime.bo.RefundResultBo;
import cn.daxpay.open.payment.trade.order.entity.RefundOrder;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 斗拱服务商退款策略
@Slf4j
@Service
@RequiredArgsConstructor
public class DougongRefundStrategy extends AbsRefundStrategy {

    private final DougongRefundService dougongRefundService;
    private final DougongIsvConfigAssembler dougongIsvConfigAssembler;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.DOUGONG_PAY;
    }

    @Override
    public RefundResultBo doRefund(RefundOrder refundOrder) {
        DougongSdkCredential credential = dougongIsvConfigAssembler.buildConfig(
                refundOrder.getMchNo(), refundOrder.getChannelMchNo(), refundOrder.getCapability());
        return dougongRefundService.refund(refundOrder, credential);
    }
}
