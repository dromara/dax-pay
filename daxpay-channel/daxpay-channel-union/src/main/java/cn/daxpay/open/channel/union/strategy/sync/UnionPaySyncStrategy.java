package cn.daxpay.open.channel.union.strategy.sync;

import cn.daxpay.open.channel.union.client.credential.UnionSdkCredential;
import cn.daxpay.open.channel.union.client.enums.UnionPayMethod;
import cn.daxpay.open.channel.union.service.direct.UnionDirectConfigAssembler;
import cn.daxpay.open.channel.union.service.payment.sync.UnionSyncService;
import cn.daxpay.open.channel.union.strategy.UnionStrategySupport;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import cn.daxpay.open.payment.strategy.pay.PayStrategyContext;
import cn.daxpay.open.payment.strategy.sync.AbsSyncPayOrderStrategy;
import cn.daxpay.open.payment.trade.runtime.bo.PaySyncResultBo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 云闪付支付同步策略
///
/// 查单支付方式由订单 capability(与 method 同码) 经 [UnionStrategySupport#resolveMethod] 解析。
@Slf4j
@Service
@RequiredArgsConstructor
public class UnionPaySyncStrategy extends AbsSyncPayOrderStrategy {

    private final UnionSyncService unionSyncService;
    private final UnionDirectConfigAssembler unionDirectConfigAssembler;

    @Override
    public PaySyncResultBo doSync(PayStrategyContext context) {
        UnionSdkCredential credential = unionDirectConfigAssembler.buildConfig(
                context.getTrade().getMchNo(), context.getChannelMchNo(), context.getCapability());
        UnionPayMethod method = UnionStrategySupport.resolveMethod(context.getCapability());
        return unionSyncService.sync(context.getTrade(), credential, method);
    }

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.UNION_PAY;
    }
}
