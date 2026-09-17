package cn.daxpay.open.channel.sheng.strategy.refund;

import cn.daxpay.open.channel.sheng.client.credential.ShengSdkCredential;
import cn.daxpay.open.channel.sheng.service.config.ShengConfigAssembler;
import cn.daxpay.open.channel.sheng.service.payment.ShengRefundSyncService;
import cn.daxpay.open.payment.strategy.refund.AbsSyncRefundStrategy;
import cn.daxpay.open.payment.trade.runtime.bo.RefundResultBo;
import cn.daxpay.open.payment.trade.order.entity.RefundOrder;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 盛付通退款同步策略
///
/// 从退款订单读取通道路由参数(channelMchNo / capability),
/// 组装通道凭证(委托 [ShengConfigAssembler]), 同步执行委托给 [ShengRefundSyncService]。
@Slf4j
@Service
@RequiredArgsConstructor
public class ShengPaySyncRefundStrategy extends AbsSyncRefundStrategy {

    private final ShengRefundSyncService shengRefundSyncService;
    private final ShengConfigAssembler shengConfigAssembler;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.SHENG_PAY;
    }

    @Override
    public RefundResultBo doSync(RefundOrder refundOrder) {
        // 组装通道调用凭证
        ShengSdkCredential credential = shengConfigAssembler.buildConfig(
                refundOrder.getMchNo(), refundOrder.getChannelMchNo(), refundOrder.getCapability());
        return shengRefundSyncService.sync(refundOrder, credential);
    }
}
