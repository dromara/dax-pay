package cn.daxpay.open.channel.hmpay.strategy;

import cn.daxpay.open.channel.hmpay.client.credential.HmpaySdkCredential;
import cn.daxpay.open.channel.hmpay.service.isv.HmpayIsvConfigAssembler;
import cn.daxpay.open.channel.hmpay.service.payment.HmpaySyncService;
import cn.daxpay.open.payment.core.strategy.pay.PayStrategyContext;
import cn.daxpay.open.payment.core.strategy.sync.AbsSyncPayOrderStrategy;
import cn.daxpay.open.payment.core.trade.runtime.bo.PaySyncResultBo;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 河马付服务商支付同步策略
@Slf4j
@Service
@RequiredArgsConstructor
public class HmpaySyncStrategy extends AbsSyncPayOrderStrategy {

    private final HmpaySyncService hmpaySyncService;
    private final HmpayIsvConfigAssembler hmpayIsvConfigAssembler;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.HM_PAY;
    }

    @Override
    public PaySyncResultBo doSync(PayStrategyContext context) {
        // 直接从 trade 读取路由参数, 不再需要 container 中间层
        HmpaySdkCredential credential = hmpayIsvConfigAssembler.buildConfig(
                context.getTrade().getMchNo(), context.getChannelMchNo(), context.getCapability());

        return hmpaySyncService.sync(context.getTrade(), credential);
    }
}
