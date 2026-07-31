package cn.daxpay.open.channel.ums.strategy.pay;

import cn.daxpay.open.channel.ums.client.credential.UmsSdkCredential;
import cn.daxpay.open.channel.ums.client.enums.UmsPayMethod;
import cn.daxpay.open.channel.ums.service.direct.UmsDirectConfigAssembler;
import cn.daxpay.open.channel.ums.service.payment.close.UmsCloseService;
import cn.daxpay.open.channel.ums.strategy.UmsStrategySupport;
import cn.daxpay.open.payment.strategy.pay.AbsPayCloseStrategy;
import cn.daxpay.open.payment.strategy.pay.PayStrategyContext;
import cn.daxpay.open.payment.trade.order.entity.PayTrade;
import cn.daxpay.open.platform.core.enums.pay.pay.CloseTypeEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/// # 银联商务支付关闭策略基类
///
/// 关单支付方式由所属产品决定(经 [UmsStrategySupport#resolveCloseSyncMethod]):
/// 扫码类走 bills 关单, H5 类走 netpay 关单。子类只需实现 getProduct。
@Slf4j
@RequiredArgsConstructor
public abstract class AbsUmsCloseStrategy extends AbsPayCloseStrategy {

    protected final UmsCloseService umsCloseService;
    protected final UmsDirectConfigAssembler umsDirectConfigAssembler;

    @Override
    public CloseTypeEnum doClose(PayStrategyContext context, boolean useCancel) {
        // 直接从 trade 读取路由参数, 不再需要 container 中间层
        PayTrade trade = context.getTrade();
        UmsSdkCredential credential = umsDirectConfigAssembler.buildConfig(
                trade.getMchNo(), context.getChannelMchNo(), context.getCapability());
        // 支付方式按所属产品决定(扫码类走 bills 关单, H5 类走 netpay 关单)
        UmsPayMethod method = UmsStrategySupport.resolveCloseSyncMethod(getProduct());
        return umsCloseService.close(trade, credential, useCancel, method);
    }
}
