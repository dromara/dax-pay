package cn.daxpay.open.channel.lakala.strategy;

import cn.daxpay.open.channel.lakala.client.credential.LakalaSdkCredential;
import cn.daxpay.open.channel.lakala.service.isv.LakalaIsvConfigAssembler;
import cn.daxpay.open.channel.lakala.service.payment.LakalaRefundSyncService;
import cn.daxpay.open.payment.strategy.refund.AbsSyncRefundStrategy;
import cn.daxpay.open.payment.trade.runtime.bo.RefundResultBo;
import cn.daxpay.open.payment.trade.order.entity.PayRefundOrder;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 拉卡拉服务商退款同步策略
///
/// 从退款订单读取通道路由参数(channelMchNo / capability),
/// 组装通道凭证(委托 [LakalaIsvConfigAssembler]), 同步执行委托给 [LakalaRefundSyncService]。
@Slf4j
@Service
@RequiredArgsConstructor
public class LakalaSyncRefundStrategy extends AbsSyncRefundStrategy {

    private final LakalaRefundSyncService lakalaRefundSyncService;
    private final LakalaIsvConfigAssembler lakalaIsvConfigAssembler;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.LAKALA_PAY;
    }

    @Override
    public RefundResultBo doSync(PayRefundOrder refundOrder) {
        // 组装通道调用凭证
        LakalaSdkCredential credential = lakalaIsvConfigAssembler.buildConfig(
                refundOrder.getMchNo(), refundOrder.getChannelMchNo(), refundOrder.getCapability());
        return lakalaRefundSyncService.sync(refundOrder, credential);
    }
}
