package cn.daxpay.open.channel.fuyou.strategy;

import cn.daxpay.open.channel.fuyou.client.credential.FuyouSdkCredential;
import cn.daxpay.open.channel.fuyou.service.isv.FuyouIsvConfigAssembler;
import cn.daxpay.open.channel.fuyou.service.payment.FuyouSyncService;
import cn.daxpay.open.payment.strategy.pay.PayStrategyContext;
import cn.daxpay.open.payment.strategy.sync.AbsSyncPayOrderStrategy;
import cn.daxpay.open.payment.trade.runtime.bo.PaySyncResultBo;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 富友服务商支付同步策略
@Slf4j
@Service
@RequiredArgsConstructor
public class FuyouSyncStrategy extends AbsSyncPayOrderStrategy {

    private final FuyouSyncService fuyouSyncService;
    private final FuyouIsvConfigAssembler fuyouIsvConfigAssembler;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.FUYOU_PAY;
    }

    @Override
    public PaySyncResultBo doSync(PayStrategyContext context) {
        // 直接从 trade 读取路由参数, 不再需要 container 中间层
        FuyouSdkCredential credential = fuyouIsvConfigAssembler.buildConfig(
                context.getTrade().getMchNo(), context.getChannelMchNo(), context.getCapability());
        return fuyouSyncService.sync(context.getTrade(), credential);
    }
}
