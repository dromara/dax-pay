package cn.daxpay.open.channel.kuaiqian.strategy.sync;

import cn.daxpay.open.channel.kuaiqian.client.credential.KuaiqianSdkCredential;
import cn.daxpay.open.channel.kuaiqian.service.direct.KuaiqianDirectConfigAssembler;
import cn.daxpay.open.channel.kuaiqian.service.payment.sync.KuaiqianSyncService;
import cn.daxpay.open.payment.strategy.pay.PayStrategyContext;
import cn.daxpay.open.payment.strategy.sync.AbsSyncPayOrderStrategy;
import cn.daxpay.open.payment.trade.runtime.bo.PaySyncResultBo;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 快钱直连支付同步策略
@Slf4j
@Service
@RequiredArgsConstructor
public class KuaiqianDirectSyncStrategy extends AbsSyncPayOrderStrategy {

    private final KuaiqianSyncService kuaiqianSyncService;
    private final KuaiqianDirectConfigAssembler kuaiqianDirectConfigAssembler;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.KUAIQIAN_PAY;
    }

    @Override
    public PaySyncResultBo doSync(PayStrategyContext context) {
        // 直接从 trade 读取路由参数, 不再需要 container 中间层
        KuaiqianSdkCredential credential = kuaiqianDirectConfigAssembler.buildConfig(
                context.getTrade().getMchNo(), context.getChannelMchNo(), context.getCapability());
        return kuaiqianSyncService.sync(context.getTrade(), credential);
    }
}
