package cn.daxpay.open.channel.youzan.strategy.sync;

import cn.daxpay.open.channel.youzan.client.credential.YouzanSdkCredential;
import cn.daxpay.open.channel.youzan.service.direct.YouzanDirectConfigAssembler;
import cn.daxpay.open.channel.youzan.service.payment.sync.YouzanSyncService;
import cn.daxpay.open.payment.strategy.pay.PayStrategyContext;
import cn.daxpay.open.payment.strategy.sync.AbsSyncPayOrderStrategy;
import cn.daxpay.open.payment.trade.runtime.bo.PaySyncResultBo;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 有赞直连支付同步策略
///
/// 有赞统收单线无订单查询接口, 恒软失败(订单保持原状态, 终态以支付回调为准)。
@Slf4j
@Service
@RequiredArgsConstructor
public class YouzanDirectSyncStrategy extends AbsSyncPayOrderStrategy {

    private final YouzanSyncService youzanSyncService;
    private final YouzanDirectConfigAssembler youzanDirectConfigAssembler;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.YOUZAN_PAY;
    }

    @Override
    public PaySyncResultBo doSync(PayStrategyContext context) {
        // 直接从 trade 读取路由参数, 不再需要 container 中间层
        YouzanSdkCredential credential = youzanDirectConfigAssembler.buildConfig(
                context.getTrade().getMchNo(), context.getChannelMchNo(), context.getCapability());
        return youzanSyncService.sync();
    }
}
