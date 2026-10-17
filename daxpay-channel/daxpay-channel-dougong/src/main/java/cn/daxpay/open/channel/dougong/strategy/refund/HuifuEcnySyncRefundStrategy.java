package cn.daxpay.open.channel.dougong.strategy.refund;

import cn.daxpay.open.channel.dougong.client.credential.DougongSdkCredential;
import cn.daxpay.open.channel.dougong.service.isv.DougongIsvConfigAssembler;
import cn.daxpay.open.channel.dougong.service.payment.DougongRefundSyncService;
import cn.daxpay.open.payment.strategy.refund.AbsSyncRefundStrategy;
import cn.daxpay.open.payment.trade.runtime.bo.RefundResultBo;
import cn.daxpay.open.payment.trade.order.entity.RefundOrder;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 汇付数字人民币退款同步策略
///
/// 复用斗拱退款同步服务: 汇付退款查询按原汇付退款流水号查询, 与底层支付方式无关。
@Slf4j
@Service
@RequiredArgsConstructor
public class HuifuEcnySyncRefundStrategy extends AbsSyncRefundStrategy {

    private final DougongRefundSyncService dougongRefundSyncService;
    private final DougongIsvConfigAssembler dougongIsvConfigAssembler;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.HUIFU_ECNY;
    }

    @Override
    public RefundResultBo doSync(RefundOrder refundOrder) {
        DougongSdkCredential credential = dougongIsvConfigAssembler.buildConfig(
                refundOrder.getMchNo(), refundOrder.getChannelMchNo(), refundOrder.getCapability());
        return dougongRefundSyncService.sync(refundOrder, credential);
    }
}
