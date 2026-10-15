package cn.daxpay.open.channel.fuyou.strategy.sync;

import cn.daxpay.open.channel.fuyou.client.credential.FuyouDirectSdkCredential;
import cn.daxpay.open.channel.fuyou.service.direct.FuyouDirectConfigAssembler;
import cn.daxpay.open.channel.fuyou.service.payment.FuyouDirectSyncService;
import cn.daxpay.open.payment.strategy.pay.PayStrategyContext;
import cn.daxpay.open.payment.strategy.sync.AbsSyncPayOrderStrategy;
import cn.daxpay.open.payment.trade.runtime.bo.PaySyncResultBo;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 富友直连支付同步策略
@Slf4j
@Service
@RequiredArgsConstructor
public class FuyouDirectSyncStrategy extends AbsSyncPayOrderStrategy {

    private final FuyouDirectSyncService fuyouDirectSyncService;
    private final FuyouDirectConfigAssembler fuyouDirectConfigAssembler;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.FUYOU_DIRECT;
    }

    @Override
    public PaySyncResultBo doSync(PayStrategyContext context) {
        // 直接从 trade 读取路由参数, 不再需要 container 中间层
        FuyouDirectSdkCredential credential = fuyouDirectConfigAssembler.buildConfig(
                context.getTrade().getMchNo(), context.getChannelMchNo(), context.getCapability());
        return fuyouDirectSyncService.sync(context.getTrade(), credential);
    }
}
