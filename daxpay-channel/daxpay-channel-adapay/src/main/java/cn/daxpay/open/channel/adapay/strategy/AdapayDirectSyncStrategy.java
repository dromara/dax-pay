package cn.daxpay.open.channel.adapay.strategy;

import cn.daxpay.open.channel.adapay.client.credential.AdapaySdkCredential;
import cn.daxpay.open.channel.adapay.service.direct.AdapayDirectConfigAssembler;
import cn.daxpay.open.channel.adapay.service.payment.sync.AdapaySyncService;
import cn.daxpay.open.payment.core.strategy.pay.PayStrategyContext;
import cn.daxpay.open.payment.core.strategy.sync.AbsSyncPayOrderStrategy;
import cn.daxpay.open.payment.core.trade.bo.PaySyncResultBo;
import cn.daxpay.open.payment.core.trade.entity.NormalPayOrder;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # Adapay 直连支付同步策略
@Slf4j
@Service
@RequiredArgsConstructor
public class AdapayDirectSyncStrategy extends AbsSyncPayOrderStrategy {

    private final AdapaySyncService adapaySyncService;
    private final AdapayDirectConfigAssembler adapayDirectConfigAssembler;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.ADA_PAY;
    }

    @Override
    public PaySyncResultBo doSync(PayStrategyContext context) {
        NormalPayOrder normalOrder = context.getContainer();
        String channelMchNo = normalOrder != null ? normalOrder.getChannelMchNo() : null;
        String capability = normalOrder != null ? normalOrder.getCapability() : null;

        AdapaySdkCredential credential = adapayDirectConfigAssembler.buildConfig(
                context.getTrade().getMchNo(), channelMchNo, capability);
        return adapaySyncService.sync(context.getTrade(), credential);
    }
}
