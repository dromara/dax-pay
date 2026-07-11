package cn.daxpay.open.channel.leshua.strategy;

import cn.daxpay.open.channel.leshua.client.credential.LeshuaSdkCredential;
import cn.daxpay.open.channel.leshua.service.isv.LeshuaIsvConfigAssembler;
import cn.daxpay.open.channel.leshua.service.payment.LeshuaSyncService;
import cn.daxpay.open.payment.core.strategy.pay.PayStrategyContext;
import cn.daxpay.open.payment.core.strategy.sync.AbsSyncPayOrderStrategy;
import cn.daxpay.open.payment.core.trade.bo.PaySyncResultBo;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 乐刷服务商支付同步策略
///
/// 组装通道凭证(委托 [LeshuaIsvConfigAssembler]), 同步执行委托给 [LeshuaSyncService]。
@Slf4j
@Service
@RequiredArgsConstructor
public class LeshuaSyncStrategy extends AbsSyncPayOrderStrategy {

    private final LeshuaSyncService leshuaSyncService;
    private final LeshuaIsvConfigAssembler leshuaIsvConfigAssembler;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.LESHUA_PAY;
    }

    @Override
    public PaySyncResultBo doSync(PayStrategyContext context) {
        // 直接从 trade 读取路由参数, 不再需要 container 中间层
        LeshuaSdkCredential credential = leshuaIsvConfigAssembler.buildConfig(
                context.getTrade().getMchNo(), context.getTrade().getChannelMchNo(), context.getTrade().getCapability());
        return leshuaSyncService.sync(context.getTrade(), credential);
    }
}
