package cn.daxpay.open.channel.sheng.strategy.sync;

import cn.daxpay.open.channel.sheng.client.credential.ShengSdkCredential;
import cn.daxpay.open.channel.sheng.service.config.ShengConfigAssembler;
import cn.daxpay.open.channel.sheng.service.payment.ShengSyncService;
import cn.daxpay.open.payment.strategy.pay.PayStrategyContext;
import cn.daxpay.open.payment.strategy.sync.AbsSyncPayOrderStrategy;
import cn.daxpay.open.payment.trade.runtime.bo.PaySyncResultBo;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 盛付通支付同步策略
///
/// 从 trade 读取通道路由参数(channelMchNo / capability),
/// 组装通道凭证(委托 [ShengConfigAssembler]), 同步执行委托给 [ShengSyncService]。
@Slf4j
@Service
@RequiredArgsConstructor
public class ShengPaySyncStrategy extends AbsSyncPayOrderStrategy {

    private final ShengSyncService shengSyncService;
    private final ShengConfigAssembler shengConfigAssembler;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.SHENG_PAY;
    }

    @Override
    public PaySyncResultBo doSync(PayStrategyContext context) {
        // 直接从 trade 读取路由参数, 不再需要 container 中间层
        // 组装通道调用凭证
        ShengSdkCredential credential = shengConfigAssembler.buildConfig(
                context.getTrade().getMchNo(), context.getChannelMchNo(), context.getCapability());

        return shengSyncService.sync(context.getTrade(), credential);
    }
}
