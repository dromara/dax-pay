package cn.daxpay.open.channel.ysep.strategy.sync;

import cn.daxpay.open.channel.ysep.client.credential.YsepSdkCredential;
import cn.daxpay.open.channel.ysep.service.config.YsepConfigAssembler;
import cn.daxpay.open.channel.ysep.service.payment.YsepSyncService;
import cn.daxpay.open.payment.strategy.pay.PayStrategyContext;
import cn.daxpay.open.payment.strategy.sync.AbsSyncPayOrderStrategy;
import cn.daxpay.open.payment.trade.runtime.bo.PaySyncResultBo;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 银盛支付同步策略(ysep_pay)
///
/// 从 trade 读取通道路由参数, 组装通道凭证(委托 [YsepConfigAssembler]),
/// 同步执行委托给 [YsepSyncService](走 ysepay.online.trade.order.query)。
@Slf4j
@Service
@RequiredArgsConstructor
public class YsepSyncStrategy extends AbsSyncPayOrderStrategy {

    private final YsepSyncService ysepSyncService;
    private final YsepConfigAssembler ysepConfigAssembler;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.YSEP_PAY;
    }

    @Override
    public PaySyncResultBo doSync(PayStrategyContext context) {
        // 直接从 trade 读取路由参数, 组装通道调用凭证
        YsepSdkCredential credential = ysepConfigAssembler.buildConfig(
                context.getTrade().getMchNo(), context.getChannelMchNo(), context.getCapability());

        return ysepSyncService.sync(context.getTrade(), credential, ProductEnum.YSEP_PAY);
    }
}
