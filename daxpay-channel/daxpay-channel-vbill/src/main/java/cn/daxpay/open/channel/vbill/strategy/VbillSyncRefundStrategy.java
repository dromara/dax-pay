package cn.daxpay.open.channel.vbill.strategy;

import cn.daxpay.open.channel.vbill.client.credential.VbillSdkCredential;
import cn.daxpay.open.channel.vbill.service.isv.VbillIsvConfigAssembler;
import cn.daxpay.open.channel.vbill.service.payment.VbillRefundSyncService;
import cn.daxpay.open.payment.strategy.refund.AbsSyncRefundStrategy;
import cn.daxpay.open.payment.trade.runtime.bo.RefundResultBo;
import cn.daxpay.open.payment.trade.order.entity.RefundOrder;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 随行付服务商退款同步策略
///
/// 从退款订单读取通道路由参数(channelMchNo / capability),
/// 组装通道凭证(委托 [VbillIsvConfigAssembler]), 同步执行委托给 [VbillRefundSyncService]。
@Slf4j
@Service
@RequiredArgsConstructor
public class VbillSyncRefundStrategy extends AbsSyncRefundStrategy {

    private final VbillRefundSyncService vbillRefundSyncService;
    private final VbillIsvConfigAssembler vbillIsvConfigAssembler;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.VBILL_PAY;
    }

    @Override
    public RefundResultBo doSync(RefundOrder refundOrder) {
        // 组装通道调用凭证
        VbillSdkCredential credential = vbillIsvConfigAssembler.buildConfig(
                refundOrder.getMchNo(), refundOrder.getChannelMchNo(), refundOrder.getCapability());
        return vbillRefundSyncService.sync(refundOrder, credential);
    }
}
