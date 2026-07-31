package cn.daxpay.open.channel.ums.strategy.sync;

import cn.daxpay.open.channel.ums.client.credential.UmsSdkCredential;
import cn.daxpay.open.channel.ums.client.enums.UmsPayMethod;
import cn.daxpay.open.channel.ums.service.direct.UmsDirectConfigAssembler;
import cn.daxpay.open.channel.ums.service.payment.sync.UmsSyncService;
import cn.daxpay.open.channel.ums.strategy.UmsStrategySupport;
import cn.daxpay.open.payment.strategy.pay.PayStrategyContext;
import cn.daxpay.open.payment.strategy.sync.AbsSyncPayOrderStrategy;
import cn.daxpay.open.payment.trade.runtime.bo.PaySyncResultBo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/// # 银联商务支付同步策略基类
///
/// 查单支付方式由所属产品决定(经 [UmsStrategySupport#resolveCloseSyncMethod]):
/// 扫码类走 bills 查询, H5 类走 netpay 查询。子类只需实现 getProduct。
@Slf4j
@RequiredArgsConstructor
public abstract class AbsUmsSyncStrategy extends AbsSyncPayOrderStrategy {

    protected final UmsSyncService umsSyncService;
    protected final UmsDirectConfigAssembler umsDirectConfigAssembler;

    @Override
    public PaySyncResultBo doSync(PayStrategyContext context) {
        // 直接从 trade 读取路由参数, 不再需要 container 中间层
        UmsSdkCredential credential = umsDirectConfigAssembler.buildConfig(
                context.getTrade().getMchNo(), context.getChannelMchNo(), context.getCapability());
        // 支付方式按所属产品决定(扫码类走 bills 查询, H5 类走 netpay 查询)
        UmsPayMethod method = UmsStrategySupport.resolveCloseSyncMethod(getProduct());
        return umsSyncService.sync(context.getTrade(), credential, method);
    }
}
