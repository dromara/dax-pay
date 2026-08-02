package cn.daxpay.open.channel.union.strategy.pay;

import cn.daxpay.open.channel.union.client.credential.UnionSdkCredential;
import cn.daxpay.open.channel.union.client.enums.UnionPayMethod;
import cn.daxpay.open.channel.union.service.direct.UnionDirectConfigAssembler;
import cn.daxpay.open.channel.union.service.payment.close.UnionCloseService;
import cn.daxpay.open.channel.union.strategy.UnionStrategySupport;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import cn.daxpay.open.platform.core.enums.pay.pay.CloseTypeEnum;
import cn.daxpay.open.payment.strategy.pay.AbsPayCloseStrategy;
import cn.daxpay.open.payment.strategy.pay.PayStrategyContext;
import cn.daxpay.open.payment.trade.order.entity.PayTrade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 云闪付关单策略
///
/// 关单支付方式由订单 capability(与 method 同码) 经 [UnionStrategySupport#resolveMethod] 解析。
@Slf4j
@Service
@RequiredArgsConstructor
public class UnionPayCloseStrategy extends AbsPayCloseStrategy {

    private final UnionCloseService unionCloseService;
    private final UnionDirectConfigAssembler unionDirectConfigAssembler;

    @Override
    public CloseTypeEnum doClose(PayStrategyContext context, boolean useCancel) {
        PayTrade trade = context.getTrade();
        UnionSdkCredential credential = unionDirectConfigAssembler.buildConfig(
                trade.getMchNo(), context.getChannelMchNo(), context.getCapability());
        UnionPayMethod method = UnionStrategySupport.resolveMethod(context.getCapability());
        return unionCloseService.close(trade, credential, useCancel, method);
    }

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.UNION_PAY;
    }
}
