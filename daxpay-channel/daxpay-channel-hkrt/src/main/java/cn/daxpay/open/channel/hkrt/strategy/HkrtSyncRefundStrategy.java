package cn.daxpay.open.channel.hkrt.strategy;

import cn.daxpay.open.channel.hkrt.client.credential.HkrtSdkCredential;
import cn.daxpay.open.channel.hkrt.service.isv.HkrtIsvConfigAssembler;
import cn.daxpay.open.channel.hkrt.service.payment.HkrtRefundSyncService;
import cn.daxpay.open.payment.core.strategy.refund.AbsSyncRefundStrategy;
import cn.daxpay.open.payment.core.trade.bo.RefundResultBo;
import cn.daxpay.open.payment.core.trade.entity.PayRefundOrder;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 海科融通服务商退款同步策略
///
/// 从退款订单读取通道路由参数(channelMchNo / capability),
/// 组装通道凭证(委托 [HkrtIsvConfigAssembler]), 同步执行委托给 [HkrtRefundSyncService]。
@Slf4j
@Service
@RequiredArgsConstructor
public class HkrtSyncRefundStrategy extends AbsSyncRefundStrategy {

    private final HkrtRefundSyncService hkrtRefundSyncService;
    private final HkrtIsvConfigAssembler hkrtIsvConfigAssembler;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.HKRT_PAY;
    }

    @Override
    public RefundResultBo doSync(PayRefundOrder refundOrder) {
        // 组装通道调用凭证
        HkrtSdkCredential credential = hkrtIsvConfigAssembler.buildConfig(
                refundOrder.getMchNo(), refundOrder.getChannelMchNo(), refundOrder.getCapability());
        return hkrtRefundSyncService.sync(refundOrder, credential);
    }
}
