package cn.daxpay.open.channel.easypay.strategy.sync;

import cn.daxpay.open.channel.easypay.client.credential.EasyPaySdkCredential;
import cn.daxpay.open.channel.easypay.service.config.EasyPayConfigAssembler;
import cn.daxpay.open.channel.easypay.service.payment.EasyPaySyncService;
import cn.daxpay.open.payment.strategy.pay.PayStrategyContext;
import cn.daxpay.open.payment.strategy.sync.AbsSyncPayOrderStrategy;
import cn.daxpay.open.payment.trade.runtime.bo.PaySyncResultBo;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 易支付支付同步策略
///
/// 从 trade 读取通道路由参数(channelMchNo / capability),
/// 组装通道凭证(委托 [EasyPayConfigAssembler]), 同步执行委托给 [EasyPaySyncService]。
@Slf4j
@Service
@RequiredArgsConstructor
public class EasyPaySyncStrategy extends AbsSyncPayOrderStrategy {

    private final EasyPaySyncService easyPaySyncService;

    private final EasyPayConfigAssembler easyPayConfigAssembler;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.EASY_PAY;
    }

    @Override
    public PaySyncResultBo doSync(PayStrategyContext context) {
        // 直接从 trade 读取路由参数
        // 组装通道调用凭证
        EasyPaySdkCredential credential = easyPayConfigAssembler.buildConfig(
                context.getTrade().getMchNo(), context.getChannelMchNo(), context.getCapability());

        return easyPaySyncService.sync(context.getTrade(), credential);
    }
}
