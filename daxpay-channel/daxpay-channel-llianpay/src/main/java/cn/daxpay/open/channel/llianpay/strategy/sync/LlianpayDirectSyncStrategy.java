package cn.daxpay.open.channel.llianpay.strategy.sync;

import cn.daxpay.open.channel.llianpay.client.credential.LlianpaySdkCredential;
import cn.daxpay.open.channel.llianpay.service.direct.LlianpayDirectConfigAssembler;
import cn.daxpay.open.channel.llianpay.service.payment.sync.LlianpaySyncService;
import cn.daxpay.open.payment.strategy.pay.PayStrategyContext;
import cn.daxpay.open.payment.strategy.sync.AbsSyncPayOrderStrategy;
import cn.daxpay.open.payment.trade.runtime.bo.PaySyncResultBo;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 连连直连支付同步策略
@Slf4j
@Service
@RequiredArgsConstructor
public class LlianpayDirectSyncStrategy extends AbsSyncPayOrderStrategy {

    private final LlianpaySyncService llianpaySyncService;
    private final LlianpayDirectConfigAssembler llianpayDirectConfigAssembler;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.LLIAN_PAY;
    }

    @Override
    public PaySyncResultBo doSync(PayStrategyContext context) {
        // 直接从 trade 读取路由参数, 不再需要 container 中间层
        LlianpaySdkCredential credential = llianpayDirectConfigAssembler.buildConfig(
                context.getTrade().getMchNo(), context.getChannelMchNo(), context.getCapability());
        return llianpaySyncService.sync(context.getTrade(), credential);
    }
}
