package cn.daxpay.open.channel.fuyou.strategy.sync;

import cn.daxpay.open.channel.fuyou.client.credential.FuyouRsaSdkCredential;
import cn.daxpay.open.channel.fuyou.service.payment.FuyouInternetSyncService;
import cn.daxpay.open.channel.fuyou.service.rsa.FuyouRsaConfigAssembler;
import cn.daxpay.open.payment.strategy.pay.PayStrategyContext;
import cn.daxpay.open.payment.strategy.sync.AbsSyncPayOrderStrategy;
import cn.daxpay.open.payment.trade.runtime.bo.PaySyncResultBo;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 富友互联网直连支付同步策略
@Slf4j
@Service
@RequiredArgsConstructor
public class FuyouInternetSyncStrategy extends AbsSyncPayOrderStrategy {

    private final FuyouInternetSyncService fuyouInternetSyncService;
    private final FuyouRsaConfigAssembler fuyouRsaConfigAssembler;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.FUYOU_INTERNET;
    }

    @Override
    public PaySyncResultBo doSync(PayStrategyContext context) {
        FuyouRsaSdkCredential credential = fuyouRsaConfigAssembler.buildConfig(context.getChannelMchNo());
        return fuyouInternetSyncService.sync(context.getTrade(), credential);
    }
}
