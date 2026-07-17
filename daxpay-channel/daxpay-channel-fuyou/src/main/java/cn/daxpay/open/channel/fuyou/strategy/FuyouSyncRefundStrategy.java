package cn.daxpay.open.channel.fuyou.strategy;

import cn.daxpay.open.channel.fuyou.client.credential.FuyouSdkCredential;
import cn.daxpay.open.channel.fuyou.service.isv.FuyouIsvConfigAssembler;
import cn.daxpay.open.channel.fuyou.service.payment.FuyouRefundSyncService;
import cn.daxpay.open.payment.strategy.refund.AbsSyncRefundStrategy;
import cn.daxpay.open.payment.trade.runtime.bo.RefundResultBo;
import cn.daxpay.open.payment.trade.order.entity.RefundOrder;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 富友服务商退款同步策略
@Slf4j
@Service
@RequiredArgsConstructor
public class FuyouSyncRefundStrategy extends AbsSyncRefundStrategy {

    private final FuyouRefundSyncService fuyouRefundSyncService;
    private final FuyouIsvConfigAssembler fuyouIsvConfigAssembler;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.FUYOU_PAY;
    }

    @Override
    public RefundResultBo doSync(RefundOrder refundOrder) {
        FuyouSdkCredential credential = fuyouIsvConfigAssembler.buildConfig(
                refundOrder.getMchNo(), refundOrder.getChannelMchNo(), refundOrder.getCapability());
        return fuyouRefundSyncService.sync(refundOrder, credential);
    }
}
