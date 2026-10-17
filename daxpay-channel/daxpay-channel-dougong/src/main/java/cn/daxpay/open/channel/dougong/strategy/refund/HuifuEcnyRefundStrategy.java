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

/// # 汇付数字人民币退款策略
///
/// 复用斗拱退款服务: 汇付退款接口(scanpay/refund)按原汇付流水号(org_hf_seq_id)原路退款,
/// 与底层支付方式无关, 数币订单(D_NATIVE)同样适用。
@Slf4j
@Service
@RequiredArgsConstructor
public class HuifuEcnyRefundStrategy extends AbsRefundStrategy {

    private final DougongRefundService dougongRefundService;
    private final DougongIsvConfigAssembler dougongIsvConfigAssembler;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.HUIFU_ECNY;
    }

    @Override
    public RefundResultBo doRefund(RefundOrder refundOrder) {
        DougongSdkCredential credential = dougongIsvConfigAssembler.buildConfig(
                refundOrder.getMchNo(), refundOrder.getChannelMchNo(), refundOrder.getCapability());
        return dougongRefundService.refund(refundOrder, credential);
    }
}
