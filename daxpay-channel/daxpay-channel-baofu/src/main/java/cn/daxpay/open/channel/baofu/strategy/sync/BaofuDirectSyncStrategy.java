package cn.daxpay.open.channel.baofu.strategy.sync;

import cn.daxpay.open.channel.baofu.client.credential.BaofuSdkCredential;
import cn.daxpay.open.channel.baofu.service.direct.BaofuDirectConfigAssembler;
import cn.daxpay.open.channel.baofu.service.payment.sync.BaofuSyncService;
import cn.daxpay.open.payment.strategy.pay.PayStrategyContext;
import cn.daxpay.open.payment.strategy.sync.AbsSyncPayOrderStrategy;
import cn.daxpay.open.payment.trade.runtime.bo.PaySyncResultBo;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 宝付直连支付同步策略
@Slf4j
@Service
@RequiredArgsConstructor
public class BaofuDirectSyncStrategy extends AbsSyncPayOrderStrategy {

    private final BaofuSyncService baofuSyncService;
    private final BaofuDirectConfigAssembler baofuDirectConfigAssembler;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.BAOFU_PAY;
    }

    @Override
    public PaySyncResultBo doSync(PayStrategyContext context) {
        // 直接从 trade 读取路由参数, 不再需要 container 中间层
        BaofuSdkCredential credential = baofuDirectConfigAssembler.buildConfig(
                context.getTrade().getMchNo(), context.getChannelMchNo(), context.getCapability());
        return baofuSyncService.sync(context.getTrade(), credential);
    }
}
