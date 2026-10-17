package cn.daxpay.open.channel.shouqianba.strategy.sync;

import cn.daxpay.open.channel.shouqianba.client.credential.ShouqianbaSdkCredential;
import cn.daxpay.open.channel.shouqianba.service.direct.ShouqianbaDirectConfigAssembler;
import cn.daxpay.open.channel.shouqianba.service.payment.sync.ShouqianbaSyncService;
import cn.daxpay.open.payment.strategy.pay.PayStrategyContext;
import cn.daxpay.open.payment.strategy.sync.AbsSyncPayOrderStrategy;
import cn.daxpay.open.payment.trade.runtime.bo.PaySyncResultBo;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 收钱吧直连支付同步策略
@Slf4j
@Service
@RequiredArgsConstructor
public class ShouqianbaDirectSyncStrategy extends AbsSyncPayOrderStrategy {

    private final ShouqianbaSyncService shouqianbaSyncService;
    private final ShouqianbaDirectConfigAssembler shouqianbaDirectConfigAssembler;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.SHOUQIANBA;
    }

    @Override
    public PaySyncResultBo doSync(PayStrategyContext context) {
        // 直接从 trade 读取路由参数, 不再需要 container 中间层
        ShouqianbaSdkCredential credential = shouqianbaDirectConfigAssembler.buildConfig(
                context.getTrade().getMchNo(), context.getChannelMchNo(), context.getCapability());
        return shouqianbaSyncService.sync(context.getTrade(), credential);
    }
}
