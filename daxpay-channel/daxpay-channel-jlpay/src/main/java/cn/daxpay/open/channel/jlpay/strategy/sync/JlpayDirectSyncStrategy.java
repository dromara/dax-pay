package cn.daxpay.open.channel.jlpay.strategy.sync;

import cn.daxpay.open.channel.jlpay.client.credential.JlpaySdkCredential;
import cn.daxpay.open.channel.jlpay.service.direct.JlpayDirectConfigAssembler;
import cn.daxpay.open.channel.jlpay.service.payment.sync.JlpaySyncService;
import cn.daxpay.open.payment.strategy.pay.PayStrategyContext;
import cn.daxpay.open.payment.strategy.sync.AbsSyncPayOrderStrategy;
import cn.daxpay.open.payment.trade.runtime.bo.PaySyncResultBo;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 嘉联直连支付同步策略
@Slf4j
@Service
@RequiredArgsConstructor
public class JlpayDirectSyncStrategy extends AbsSyncPayOrderStrategy {

    private final JlpaySyncService jlpaySyncService;
    private final JlpayDirectConfigAssembler jlpayDirectConfigAssembler;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.JL_PAY;
    }

    @Override
    public PaySyncResultBo doSync(PayStrategyContext context) {
        // 直接从 trade 读取路由参数, 不再需要 container 中间层
        JlpaySdkCredential credential = jlpayDirectConfigAssembler.buildConfig(
                context.getTrade().getMchNo(), context.getChannelMchNo(), context.getCapability());
        return jlpaySyncService.sync(context.getTrade(), credential);
    }
}
