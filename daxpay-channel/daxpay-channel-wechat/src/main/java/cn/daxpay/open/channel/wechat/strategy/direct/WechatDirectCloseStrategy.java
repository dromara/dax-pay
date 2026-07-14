package cn.daxpay.open.channel.wechat.strategy.direct;

import cn.daxpay.open.channel.wechat.client.credential.WechatSdkCredential;
import cn.daxpay.open.channel.wechat.service.direct.WechatDirectConfigAssembler;
import cn.daxpay.open.channel.wechat.service.payment.close.WechatCloseService;
import cn.daxpay.open.payment.strategy.pay.AbsPayCloseStrategy;
import cn.daxpay.open.payment.strategy.pay.PayStrategyContext;
import cn.daxpay.open.payment.trade.order.entity.PayTrade;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import cn.daxpay.open.platform.core.enums.pay.pay.CloseTypeEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 微信直连支付关闭策略
///
/// 微信直连模式(ProductEnum.WECHAT_PAY)下的支付关闭策略。
/// 从 trade 读取通道路由参数(channelMchNo / capability),
/// 组装通道凭证(委托 [WechatDirectConfigAssembler]), 关闭执行委托给 [WechatCloseService]。
///
/// 注意: 微信 V3 仅提供关单接口, 无撤销接口(useCancel 参数由 service 层忽略)。
@Slf4j
@Service
@RequiredArgsConstructor
public class WechatDirectCloseStrategy extends AbsPayCloseStrategy {

    private final WechatCloseService wechatCloseService;
    private final WechatDirectConfigAssembler wechatDirectConfigAssembler;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.WECHAT_PAY;
    }

    @Override
    public CloseTypeEnum doClose(PayStrategyContext context, boolean useCancel) {
        // 直接从 trade 读取路由参数, 不再需要 container 中间层
        PayTrade trade = context.getTrade();

        // 组装通道调用凭证
        WechatSdkCredential credential = wechatDirectConfigAssembler.buildConfig(
                trade.getMchNo(), context.getChannelMchNo(), context.getCapability(),
                context.getChannelAppId());

        return wechatCloseService.close(trade, credential, useCancel);
    }
}
