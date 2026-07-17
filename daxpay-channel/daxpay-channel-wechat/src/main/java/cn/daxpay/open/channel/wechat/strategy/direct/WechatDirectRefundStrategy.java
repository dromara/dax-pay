package cn.daxpay.open.channel.wechat.strategy.direct;

import cn.daxpay.open.channel.wechat.client.credential.WechatSdkCredential;
import cn.daxpay.open.channel.wechat.service.direct.WechatDirectConfigAssembler;
import cn.daxpay.open.channel.wechat.service.payment.refund.WechatRefundService;
import cn.daxpay.open.payment.strategy.refund.AbsRefundStrategy;
import cn.daxpay.open.payment.trade.runtime.bo.RefundResultBo;
import cn.daxpay.open.payment.trade.order.entity.RefundOrder;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 微信直连退款策略
///
/// 微信直连模式(ProductEnum.WECHAT_PAY)下的退款策略。
/// 从退款订单读取通道路由参数(channelMchNo / capability),
/// 组装通道凭证(委托 [WechatDirectConfigAssembler]), 退款执行委托给 [WechatRefundService]。
@Slf4j
@Service
@RequiredArgsConstructor
public class WechatDirectRefundStrategy extends AbsRefundStrategy {

    private final WechatRefundService wechatRefundService;
    private final WechatDirectConfigAssembler wechatDirectConfigAssembler;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.WECHAT_PAY;
    }

    @Override
    public RefundResultBo doRefund(RefundOrder refundOrder) {
        // 组装直连通道调用凭证
        WechatSdkCredential credential = wechatDirectConfigAssembler.buildConfig(
                refundOrder.getMchNo(), refundOrder.getChannelMchNo(), refundOrder.getCapability(),
                refundOrder.getChannelAppId());
        return wechatRefundService.refund(refundOrder, credential);
    }
}
