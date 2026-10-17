package cn.daxpay.open.channel.helipay.strategy.sync;

import cn.daxpay.open.channel.helipay.client.credential.HelipaySdkCredential;
import cn.daxpay.open.channel.helipay.service.config.HelipayConfigAssembler;
import cn.daxpay.open.channel.helipay.service.payment.HelipaySyncService;
import cn.daxpay.open.payment.strategy.pay.PayStrategyContext;
import cn.daxpay.open.payment.strategy.sync.AbsSyncPayOrderStrategy;
import cn.daxpay.open.payment.trade.runtime.bo.PaySyncResultBo;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 合利宝直连支付同步策略
///
/// 从 trade 读取通道路由参数(channelMchNo / capability),
/// 组装通道凭证(委托 [HelipayConfigAssembler]), 同步执行委托给 [HelipaySyncService]。
@Slf4j
@Service
@RequiredArgsConstructor
public class HelipayDirectSyncStrategy extends AbsSyncPayOrderStrategy {

    private final HelipaySyncService helipaySyncService;

    private final HelipayConfigAssembler helipayConfigAssembler;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.HELI_PAY;
    }

    @Override
    public PaySyncResultBo doSync(PayStrategyContext context) {
        // 直接从 trade 读取路由参数, 组装通道调用凭证
        HelipaySdkCredential credential = helipayConfigAssembler.buildConfig(
                context.getTrade().getMchNo(), context.getChannelMchNo(), context.getCapability());
        return helipaySyncService.sync(context.getTrade(), credential);
    }
}
