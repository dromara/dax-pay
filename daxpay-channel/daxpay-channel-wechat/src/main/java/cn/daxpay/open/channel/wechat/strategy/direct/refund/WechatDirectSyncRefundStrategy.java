package cn.daxpay.open.channel.wechat.strategy.direct.refund;

import cn.daxpay.open.channel.wechat.client.credential.WechatSdkCredential;
import cn.daxpay.open.channel.wechat.service.direct.WechatDirectConfigAssembler;
import cn.daxpay.open.channel.wechat.service.payment.refund.WechatRefundSyncService;
import cn.daxpay.open.payment.strategy.refund.AbsSyncRefundStrategy;
import cn.daxpay.open.payment.trade.runtime.bo.RefundResultBo;
import cn.daxpay.open.payment.trade.order.entity.RefundOrder;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 微信直连退款同步策略
///
/// 微信直连模式(ProductEnum.WECHAT_PAY)下的退款同步策略。
/// 组装通道凭证(委托 [WechatDirectConfigAssembler]), 同步执行委托给 [WechatRefundSyncService]。
@Slf4j
@Service
@RequiredArgsConstructor
public class WechatDirectSyncRefundStrategy extends AbsSyncRefundStrategy {

    private final WechatRefundSyncService wechatRefundSyncService;
    private final WechatDirectConfigAssembler wechatDirectConfigAssembler;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.WECHAT_PAY;
    }

    @Override
    public RefundResultBo doSync(RefundOrder refundOrder) {
        // 组装直连通道调用凭证
        WechatSdkCredential credential = wechatDirectConfigAssembler.buildConfig(
                refundOrder.getMchNo(), refundOrder.getChannelMchNo(), refundOrder.getCapability(),
                refundOrder.getChannelAppId());
        return wechatRefundSyncService.sync(refundOrder, credential);
    }
}
