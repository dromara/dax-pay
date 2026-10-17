package cn.daxpay.open.channel.fubei.strategy.sync;

import cn.daxpay.open.channel.fubei.client.credential.FubeiSdkCredential;
import cn.daxpay.open.channel.fubei.service.direct.FubeiDirectConfigAssembler;
import cn.daxpay.open.channel.fubei.service.payment.sync.FubeiSyncService;
import cn.daxpay.open.payment.strategy.pay.PayStrategyContext;
import cn.daxpay.open.payment.strategy.sync.AbsSyncPayOrderStrategy;
import cn.daxpay.open.payment.trade.runtime.bo.PaySyncResultBo;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 付呗直连支付同步策略
@Slf4j
@Service
@RequiredArgsConstructor
public class FubeiDirectSyncStrategy extends AbsSyncPayOrderStrategy {

    private final FubeiSyncService fubeiSyncService;
    private final FubeiDirectConfigAssembler fubeiDirectConfigAssembler;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.FUBEI_PAY;
    }

    @Override
    public PaySyncResultBo doSync(PayStrategyContext context) {
        // 直接从 trade 读取路由参数, 不再需要 container 中间层
        FubeiSdkCredential credential = fubeiDirectConfigAssembler.buildConfig(
                context.getTrade().getMchNo(), context.getChannelMchNo(), context.getCapability());
        return fubeiSyncService.sync(context.getTrade(), credential);
    }
}
