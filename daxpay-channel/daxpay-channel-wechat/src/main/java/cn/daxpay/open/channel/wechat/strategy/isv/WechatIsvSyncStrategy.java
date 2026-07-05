package cn.daxpay.open.channel.wechat.strategy.isv;

import cn.daxpay.open.channel.wechat.client.credential.WechatSdkCredential;
import cn.daxpay.open.channel.wechat.service.isv.WechatIsvConfigAssembler;
import cn.daxpay.open.channel.wechat.service.payment.isv.WechatIsvSyncService;
import cn.daxpay.open.payment.core.strategy.pay.PayStrategyContext;
import cn.daxpay.open.payment.core.strategy.sync.AbsSyncPayOrderStrategy;
import cn.daxpay.open.payment.core.trade.bo.PaySyncResultBo;
import cn.daxpay.open.payment.core.trade.entity.NormalPayOrder;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 微信服务商支付同步策略
///
/// 微信服务商模式(ProductEnum.WECHAT_ISV)下的支付同步策略。
/// 从上下文容器读取通道路由参数(channelMchNo / capability),
/// 组装服务商通道凭证(委托 [WechatIsvConfigAssembler]), 同步执行委托给 [WechatSyncService]。
@Slf4j
@Service
@RequiredArgsConstructor
public class WechatIsvSyncStrategy extends AbsSyncPayOrderStrategy {

    private final WechatIsvSyncService wechatIsvSyncService;
    private final WechatIsvConfigAssembler wechatIsvConfigAssembler;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.WECHAT_ISV;
    }

    @Override
    public PaySyncResultBo doSync(PayStrategyContext context) {
        // 从上下文容器读取通道路由参数, 用于凭证解析
        NormalPayOrder normalOrder = context.getContainer();
        String channelMchNo = normalOrder != null ? normalOrder.getChannelMchNo() : null;
        String capability = normalOrder != null ? normalOrder.getCapability() : null;

        // 组装服务商通道调用凭证
        WechatSdkCredential credential = wechatIsvConfigAssembler.buildConfig(
                context.getTrade().getMchNo(), channelMchNo, capability);

        return wechatIsvSyncService.sync(context.getTrade(), credential);
    }
}
