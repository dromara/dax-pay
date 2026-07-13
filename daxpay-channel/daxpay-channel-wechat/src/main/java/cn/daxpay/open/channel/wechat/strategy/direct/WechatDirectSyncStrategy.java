package cn.daxpay.open.channel.wechat.strategy.direct;

import cn.daxpay.open.channel.wechat.client.credential.WechatSdkCredential;
import cn.daxpay.open.channel.wechat.service.direct.WechatDirectConfigAssembler;
import cn.daxpay.open.channel.wechat.service.payment.sync.WechatSyncService;
import cn.daxpay.open.payment.strategy.pay.PayStrategyContext;
import cn.daxpay.open.payment.strategy.sync.AbsSyncPayOrderStrategy;
import cn.daxpay.open.payment.trade.runtime.bo.PaySyncResultBo;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 微信直连支付同步策略
///
/// 微信直连模式(ProductEnum.WECHAT_PAY)下的支付同步策略。
/// 从 trade 读取通道路由参数(channelMchNo / capability),
/// 组装通道凭证(委托 [WechatDirectConfigAssembler]), 同步执行委托给 [WechatSyncService]。
@Slf4j
@Service
@RequiredArgsConstructor
public class WechatDirectSyncStrategy extends AbsSyncPayOrderStrategy {

    private final WechatSyncService wechatSyncService;
    private final WechatDirectConfigAssembler wechatDirectConfigAssembler;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.WECHAT_PAY;
    }

    @Override
    public PaySyncResultBo doSync(PayStrategyContext context) {
        // 直接从 trade 读取路由参数, 不再需要 container 中间层
        // 组装通道调用凭证
        WechatSdkCredential credential = wechatDirectConfigAssembler.buildConfig(
                context.getTrade().getMchNo(), context.getChannelMchNo(), context.getCapability());

        return wechatSyncService.sync(context.getTrade(), credential);
    }
}
