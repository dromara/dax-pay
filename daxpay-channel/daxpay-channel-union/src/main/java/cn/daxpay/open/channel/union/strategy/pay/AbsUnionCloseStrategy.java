package cn.daxpay.open.channel.union.strategy.pay;

import cn.daxpay.open.channel.union.client.credential.UnionSdkCredential;
import cn.daxpay.open.channel.union.client.enums.UnionPayMethod;
import cn.daxpay.open.channel.union.service.direct.UnionDirectConfigAssembler;
import cn.daxpay.open.channel.union.service.payment.close.UnionCloseService;
import cn.daxpay.open.channel.union.strategy.UnionStrategySupport;
import cn.daxpay.open.payment.strategy.pay.AbsPayCloseStrategy;
import cn.daxpay.open.payment.strategy.pay.PayStrategyContext;
import cn.daxpay.open.payment.trade.order.entity.PayTrade;
import cn.daxpay.open.platform.core.enums.pay.pay.CloseTypeEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/// # 云闪付支付关闭策略基类
///
/// 关单支付方式由所属产品决定(经 [UnionStrategySupport#resolveMethod])。子类只需实现 getProduct。
@Slf4j
@RequiredArgsConstructor
public abstract class AbsUnionCloseStrategy extends AbsPayCloseStrategy {

    protected final UnionCloseService unionCloseService;
    protected final UnionDirectConfigAssembler unionDirectConfigAssembler;

    @Override
    public CloseTypeEnum doClose(PayStrategyContext context, boolean useCancel) {
        PayTrade trade = context.getTrade();
        UnionSdkCredential credential = unionDirectConfigAssembler.buildConfig(
                trade.getMchNo(), context.getChannelMchNo(), context.getCapability());
        UnionPayMethod method = UnionStrategySupport.resolveMethod(getProduct());
        return unionCloseService.close(trade, credential, useCancel, method);
    }
}
