package cn.daxpay.open.channel.sand.strategy.sync;

import cn.daxpay.open.channel.sand.client.credential.SandSdkCredential;
import cn.daxpay.open.channel.sand.service.direct.SandDirectConfigAssembler;
import cn.daxpay.open.channel.sand.service.payment.sync.SandSyncService;
import cn.daxpay.open.payment.strategy.pay.PayStrategyContext;
import cn.daxpay.open.payment.strategy.sync.AbsSyncPayOrderStrategy;
import cn.daxpay.open.payment.trade.runtime.bo.PaySyncResultBo;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 杉德直连支付同步策略
@Slf4j
@Service
@RequiredArgsConstructor
public class SandDirectSyncStrategy extends AbsSyncPayOrderStrategy {

    private final SandSyncService sandSyncService;
    private final SandDirectConfigAssembler sandDirectConfigAssembler;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.SAND_PAY;
    }

    @Override
    public PaySyncResultBo doSync(PayStrategyContext context) {
        // 直接从 trade 读取路由参数, 不再需要 container 中间层
        SandSdkCredential credential = sandDirectConfigAssembler.buildConfig(
                context.getTrade().getMchNo(), context.getChannelMchNo(), context.getCapability());
        return sandSyncService.sync(context.getTrade(), credential);
    }
}
