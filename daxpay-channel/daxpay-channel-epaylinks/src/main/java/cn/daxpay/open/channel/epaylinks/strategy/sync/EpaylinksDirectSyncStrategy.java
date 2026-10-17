package cn.daxpay.open.channel.epaylinks.strategy.sync;

import cn.daxpay.open.channel.epaylinks.client.credential.EpaylinksSdkCredential;
import cn.daxpay.open.channel.epaylinks.service.direct.EpaylinksDirectConfigAssembler;
import cn.daxpay.open.channel.epaylinks.service.payment.sync.EpaylinksSyncService;
import cn.daxpay.open.payment.strategy.pay.PayStrategyContext;
import cn.daxpay.open.payment.strategy.sync.AbsSyncPayOrderStrategy;
import cn.daxpay.open.payment.trade.runtime.bo.PaySyncResultBo;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 易票联直连支付同步策略
@Slf4j
@Service
@RequiredArgsConstructor
public class EpaylinksDirectSyncStrategy extends AbsSyncPayOrderStrategy {

    private final EpaylinksSyncService epaylinksSyncService;
    private final EpaylinksDirectConfigAssembler epaylinksDirectConfigAssembler;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.EPAY_LINKS;
    }

    @Override
    public PaySyncResultBo doSync(PayStrategyContext context) {
        // 直接从 trade 读取路由参数, 不再需要 container 中间层
        EpaylinksSdkCredential credential = epaylinksDirectConfigAssembler.buildConfig(
                context.getTrade().getMchNo(), context.getChannelMchNo(), context.getCapability());
        return epaylinksSyncService.sync(context.getTrade(), credential);
    }
}
