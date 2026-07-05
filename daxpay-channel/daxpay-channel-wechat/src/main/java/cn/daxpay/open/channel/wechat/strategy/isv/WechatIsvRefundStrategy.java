package cn.daxpay.open.channel.wechat.strategy.isv;

import cn.daxpay.open.channel.wechat.client.credential.WechatSdkCredential;
import cn.daxpay.open.channel.wechat.service.isv.WechatIsvConfigAssembler;
import cn.daxpay.open.channel.wechat.service.payment.isv.WechatIsvRefundService;
import cn.daxpay.open.payment.core.strategy.refund.AbsRefundStrategy;
import cn.daxpay.open.payment.core.trade.bo.RefundResultBo;
import cn.daxpay.open.payment.core.trade.entity.PayRefundOrder;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 微信服务商退款策略
///
/// 微信服务商模式(ProductEnum.WECHAT_ISV)下的退款策略。
/// 从退款订单读取通道路由参数(channelMchNo / capability),
/// 组装服务商通道凭证(委托 [WechatIsvConfigAssembler]), 退款执行委托给 [WechatRefundService]。
@Slf4j
@Service
@RequiredArgsConstructor
public class WechatIsvRefundStrategy extends AbsRefundStrategy {

    private final WechatIsvRefundService wechatIsvRefundService;
    private final WechatIsvConfigAssembler wechatIsvConfigAssembler;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.WECHAT_ISV;
    }

    @Override
    public RefundResultBo doRefund(PayRefundOrder refundOrder) {
        // 组装服务商通道调用凭证
        WechatSdkCredential credential = wechatIsvConfigAssembler.buildConfig(
                refundOrder.getMchNo(), refundOrder.getChannelMchNo(), refundOrder.getCapability());
        return wechatIsvRefundService.refund(refundOrder, credential);
    }
}
