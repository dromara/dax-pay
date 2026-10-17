package cn.daxpay.open.channel.yisheng.strategy.sync;

import cn.daxpay.open.channel.yisheng.client.credential.YishengSdkCredential;
import cn.daxpay.open.channel.yisheng.service.direct.YishengDirectConfigAssembler;
import cn.daxpay.open.channel.yisheng.service.payment.sync.YishengSyncService;
import cn.daxpay.open.payment.strategy.pay.PayStrategyContext;
import cn.daxpay.open.payment.strategy.sync.AbsSyncPayOrderStrategy;
import cn.daxpay.open.payment.trade.runtime.bo.PaySyncResultBo;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 易生直连支付同步策略
@Slf4j
@Service
@RequiredArgsConstructor
public class YishengDirectSyncStrategy extends AbsSyncPayOrderStrategy {

    private final YishengSyncService yishengSyncService;
    private final YishengDirectConfigAssembler yishengDirectConfigAssembler;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.YISHENG_PAY;
    }

    @Override
    public PaySyncResultBo doSync(PayStrategyContext context) {
        // 直接从 trade 读取路由参数, 不再需要 container 中间层
        YishengSdkCredential credential = yishengDirectConfigAssembler.buildConfig(
                context.getTrade().getMchNo(), context.getChannelMchNo(), context.getCapability());
        return yishengSyncService.sync(context.getTrade(), credential);
    }
}
