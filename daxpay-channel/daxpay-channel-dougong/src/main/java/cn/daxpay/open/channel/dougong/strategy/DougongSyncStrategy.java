package cn.daxpay.open.channel.dougong.strategy;

import cn.daxpay.open.channel.dougong.client.credential.DougongSdkCredential;
import cn.daxpay.open.channel.dougong.service.isv.DougongIsvConfigAssembler;
import cn.daxpay.open.channel.dougong.service.payment.DougongSyncService;
import cn.daxpay.open.payment.core.strategy.pay.PayStrategyContext;
import cn.daxpay.open.payment.core.strategy.sync.AbsSyncPayOrderStrategy;
import cn.daxpay.open.payment.core.trade.bo.PaySyncResultBo;
import cn.daxpay.open.payment.core.trade.entity.NormalPayOrder;
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
        NormalPayOrder normalOrder = context.getContainer();
        String channelMchNo = normalOrder != null ? normalOrder.getChannelMchNo() : null;
        String capability = normalOrder != null ? normalOrder.getCapability() : null;

        DougongSdkCredential credential = dougongIsvConfigAssembler.buildConfig(
                context.getTrade().getMchNo(), channelMchNo, capability);

        return dougongSyncService.sync(context.getTrade(), credential);
    }
}
