package cn.daxpay.open.channel.yeepay.strategy;

import cn.daxpay.open.channel.yeepay.client.credential.YeepaySdkCredential;
import cn.daxpay.open.channel.yeepay.service.direct.YeepayDirectConfigAssembler;
import cn.daxpay.open.channel.yeepay.service.payment.sync.YeepaySyncService;
import cn.daxpay.open.payment.core.strategy.pay.PayStrategyContext;
import cn.daxpay.open.payment.core.strategy.sync.AbsSyncPayOrderStrategy;
import cn.daxpay.open.payment.core.trade.bo.PaySyncResultBo;
import cn.daxpay.open.payment.core.trade.entity.NormalPayOrder;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 易宝直连支付同步策略
@Slf4j
@Service
@RequiredArgsConstructor
public class YeepayDirectSyncStrategy extends AbsSyncPayOrderStrategy {

    private final YeepaySyncService yeepaySyncService;
    private final YeepayDirectConfigAssembler yeepayDirectConfigAssembler;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.YEE_PAY;
    }

    @Override
    public PaySyncResultBo doSync(PayStrategyContext context) {
        NormalPayOrder normalOrder = context.getContainer();
        String channelMchNo = normalOrder != null ? normalOrder.getChannelMchNo() : null;
        String capability = normalOrder != null ? normalOrder.getCapability() : null;

        YeepaySdkCredential credential = yeepayDirectConfigAssembler.buildConfig(
                context.getTrade().getMchNo(), channelMchNo, capability);
        return yeepaySyncService.sync(context.getTrade(), credential);
    }
}
