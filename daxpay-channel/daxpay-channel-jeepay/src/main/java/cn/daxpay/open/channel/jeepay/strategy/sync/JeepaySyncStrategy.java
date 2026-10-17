package cn.daxpay.open.channel.jeepay.strategy.sync;

import cn.daxpay.open.channel.jeepay.client.credential.JeepaySdkCredential;
import cn.daxpay.open.channel.jeepay.service.config.JeepayConfigAssembler;
import cn.daxpay.open.channel.jeepay.service.payment.JeepaySyncService;
import cn.daxpay.open.payment.strategy.pay.PayStrategyContext;
import cn.daxpay.open.payment.strategy.sync.AbsSyncPayOrderStrategy;
import cn.daxpay.open.payment.trade.runtime.bo.PaySyncResultBo;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # Jeepay支付同步策略
///
/// 从 trade 读取通道路由参数(channelMchNo / capability),
/// 组装通道凭证(委托 [JeepayConfigAssembler]), 同步执行委托给 [JeepaySyncService]。
@Slf4j
@Service
@RequiredArgsConstructor
public class JeepaySyncStrategy extends AbsSyncPayOrderStrategy {

    private final JeepaySyncService jeepaySyncService;

    private final JeepayConfigAssembler jeepayConfigAssembler;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.JEE_PAY;
    }

    @Override
    public PaySyncResultBo doSync(PayStrategyContext context) {
        // 直接从 trade 读取路由参数
        // 组装通道调用凭证
        JeepaySdkCredential credential = jeepayConfigAssembler.buildConfig(
                context.getTrade().getMchNo(), context.getChannelMchNo(), context.getCapability());

        return jeepaySyncService.sync(context.getTrade(), credential);
    }
}
