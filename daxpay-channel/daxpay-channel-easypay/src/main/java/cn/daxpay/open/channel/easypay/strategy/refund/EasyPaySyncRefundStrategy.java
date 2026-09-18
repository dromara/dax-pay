package cn.daxpay.open.channel.easypay.strategy.refund;

import cn.daxpay.open.channel.easypay.client.credential.EasyPaySdkCredential;
import cn.daxpay.open.channel.easypay.service.config.EasyPayConfigAssembler;
import cn.daxpay.open.channel.easypay.service.payment.EasyPayRefundSyncService;
import cn.daxpay.open.payment.strategy.refund.AbsSyncRefundStrategy;
import cn.daxpay.open.payment.trade.runtime.bo.RefundResultBo;
import cn.daxpay.open.payment.trade.order.entity.RefundOrder;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 易支付退款同步策略
///
/// 从退款订单读取通道路由参数(channelMchNo / capability),
/// 组装通道凭证(委托 [EasyPayConfigAssembler]), 同步执行委托给 [EasyPayRefundSyncService]。
@Slf4j
@Service
@RequiredArgsConstructor
public class EasyPaySyncRefundStrategy extends AbsSyncRefundStrategy {

    private final EasyPayRefundSyncService easyPayRefundSyncService;

    private final EasyPayConfigAssembler easyPayConfigAssembler;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.EASY_PAY;
    }

    @Override
    public RefundResultBo doSync(RefundOrder refundOrder) {
        // 组装通道调用凭证
        EasyPaySdkCredential credential = easyPayConfigAssembler.buildConfig(
                refundOrder.getMchNo(), refundOrder.getChannelMchNo(), refundOrder.getCapability());
        return easyPayRefundSyncService.sync(refundOrder, credential);
    }
}
