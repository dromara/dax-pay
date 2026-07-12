package cn.daxpay.open.channel.alipay.strategy.direct;

import cn.daxpay.open.channel.alipay.client.credential.AlipaySdkCredential;
import cn.daxpay.open.channel.alipay.service.direct.AlipayDirectConfigAssembler;
import cn.daxpay.open.channel.alipay.service.payment.sync.AlipaySyncService;
import cn.daxpay.open.payment.core.strategy.pay.PayStrategyContext;
import cn.daxpay.open.payment.core.trade.runtime.bo.PaySyncResultBo;
import cn.daxpay.open.payment.core.strategy.sync.AbsSyncPayOrderStrategy;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 支付宝直连支付同步策略
///
/// 支付宝直连模式(ProductEnum.ALIPAY)下的支付同步策略。
/// 从 trade 读取通道路由参数(channelMchNo / capability),
/// 组装通道凭证(委托 [AlipayDirectConfigAssembler]), 同步执行委托给 [AlipaySyncService]。
@Slf4j
@Service
@RequiredArgsConstructor
public class AlipayDirectSyncStrategy extends AbsSyncPayOrderStrategy {

    private final AlipaySyncService alipaySyncService;
    private final AlipayDirectConfigAssembler alipayDirectConfigAssembler;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.ALIPAY;
    }

    @Override
    public PaySyncResultBo doSync(PayStrategyContext context) {
        // 直接从 trade 读取路由参数, 不再需要 container 中间层
        // 组装通道调用凭证
        AlipaySdkCredential credential = alipayDirectConfigAssembler.buildConfig(
                context.getTrade().getMchNo(), context.getChannelMchNo(), context.getCapability());

        return alipaySyncService.sync(context.getTrade(), credential);
    }
}
