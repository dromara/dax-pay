package cn.daxpay.open.channel.union.strategy.sync;

import cn.daxpay.open.channel.union.client.credential.UnionSdkCredential;
import cn.daxpay.open.channel.union.client.enums.UnionPayMethod;
import cn.daxpay.open.channel.union.service.direct.UnionDirectConfigAssembler;
import cn.daxpay.open.channel.union.service.payment.sync.UnionSyncService;
import cn.daxpay.open.channel.union.strategy.UnionStrategySupport;
import cn.daxpay.open.payment.strategy.pay.PayStrategyContext;
import cn.daxpay.open.payment.strategy.sync.AbsSyncPayOrderStrategy;
import cn.daxpay.open.payment.trade.runtime.bo.PaySyncResultBo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/// # 云闪付支付同步策略基类
///
/// 查单支付方式由所属产品决定。子类只需实现 getProduct。
@Slf4j
@RequiredArgsConstructor
public abstract class AbsUnionSyncStrategy extends AbsSyncPayOrderStrategy {

    protected final UnionSyncService unionSyncService;
    protected final UnionDirectConfigAssembler unionDirectConfigAssembler;

    @Override
    public PaySyncResultBo doSync(PayStrategyContext context) {
        UnionSdkCredential credential = unionDirectConfigAssembler.buildConfig(
                context.getTrade().getMchNo(), context.getChannelMchNo(), context.getCapability());
        UnionPayMethod method = UnionStrategySupport.resolveMethod(getProduct());
        return unionSyncService.sync(context.getTrade(), credential, method);
    }
}
