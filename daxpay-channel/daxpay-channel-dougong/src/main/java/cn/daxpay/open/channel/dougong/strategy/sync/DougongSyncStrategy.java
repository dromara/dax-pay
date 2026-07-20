package cn.daxpay.open.channel.dougong.strategy.sync;

import cn.daxpay.open.channel.dougong.client.credential.DougongSdkCredential;
import cn.daxpay.open.channel.dougong.service.isv.DougongIsvConfigAssembler;
import cn.daxpay.open.channel.dougong.service.payment.DougongSyncService;
import cn.daxpay.open.payment.strategy.pay.PayStrategyContext;
import cn.daxpay.open.payment.strategy.sync.AbsSyncPayOrderStrategy;
import cn.daxpay.open.payment.trade.runtime.bo.PaySyncResultBo;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 斗拱服务商支付同步策略
@Slf4j
@Service
@RequiredArgsConstructor
public class DougongSyncStrategy extends AbsSyncPayOrderStrategy {

    private final DougongSyncService dougongSyncService;
    private final DougongIsvConfigAssembler dougongIsvConfigAssembler;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.DOUGONG_PAY;
    }

    @Override
    public PaySyncResultBo doSync(PayStrategyContext context) {
        // 直接从 trade 读取路由参数, 不再需要 container 中间层
        DougongSdkCredential credential = dougongIsvConfigAssembler.buildConfig(
                context.getTrade().getMchNo(), context.getChannelMchNo(), context.getCapability());

        return dougongSyncService.sync(context.getTrade(), credential);
    }
}
