package cn.daxpay.open.channel.wechat.strategy.isv;

import cn.daxpay.open.channel.wechat.client.credential.WechatSdkCredential;
import cn.daxpay.open.channel.wechat.service.isv.WechatIsvConfigAssembler;
import cn.daxpay.open.channel.wechat.service.payment.isv.WechatIsvRefundSyncService;
import cn.daxpay.open.payment.strategy.refund.AbsSyncRefundStrategy;
import cn.daxpay.open.payment.trade.runtime.bo.RefundResultBo;
import cn.daxpay.open.payment.trade.order.entity.PayRefundOrder;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 微信服务商退款同步策略
///
/// 微信服务商模式(ProductEnum.WECHAT_ISV)下的退款同步策略。
/// 组装服务商通道凭证(委托 [WechatIsvConfigAssembler]), 同步执行委托给 [WechatRefundSyncService]。
@Slf4j
@Service
@RequiredArgsConstructor
public class WechatIsvSyncRefundStrategy extends AbsSyncRefundStrategy {

    private final WechatIsvRefundSyncService wechatIsvRefundSyncService;
    private final WechatIsvConfigAssembler wechatIsvConfigAssembler;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.WECHAT_ISV;
    }

    @Override
    public RefundResultBo doSync(PayRefundOrder refundOrder) {
        // 组装服务商通道调用凭证
        WechatSdkCredential credential = wechatIsvConfigAssembler.buildConfig(
                refundOrder.getMchNo(), refundOrder.getChannelMchNo(), refundOrder.getCapability(),
                refundOrder.getChannelAppId());
        return wechatIsvRefundSyncService.sync(refundOrder, credential);
    }
}
