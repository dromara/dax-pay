package cn.daxpay.open.channel.douyin.strategy.sync;

import cn.daxpay.open.channel.douyin.client.credential.DouyinSdkCredential;
import cn.daxpay.open.channel.douyin.service.direct.DouyinDirectConfigAssembler;
import cn.daxpay.open.channel.douyin.service.payment.sync.DouyinSyncService;
import cn.daxpay.open.payment.strategy.pay.PayStrategyContext;
import cn.daxpay.open.payment.trade.runtime.bo.PaySyncResultBo;
import cn.daxpay.open.payment.strategy.sync.AbsSyncPayOrderStrategy;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 抖音直连支付同步策略
///
/// 抖音直连模式(ProductEnum.DOUYIN_PAY)下的支付同步策略。
/// 从交易凭证读取通道路由参数(channelMchNo / capability),
/// 组装通道凭证(委托 [DouyinDirectConfigAssembler]), 同步执行委托给 [DouyinSyncService]。
@Slf4j
@Service
@RequiredArgsConstructor
public class DouyinDirectSyncStrategy extends AbsSyncPayOrderStrategy {

    private final DouyinSyncService douyinSyncService;
    private final DouyinDirectConfigAssembler douyinDirectConfigAssembler;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.DOUYIN_PAY;
    }

    @Override
    public PaySyncResultBo doSync(PayStrategyContext context) {
        // 直接从 trade 读取路由参数, 不再需要 container 中间层
        DouyinSdkCredential credential = douyinDirectConfigAssembler.buildConfig(
                context.getTrade().getMchNo(), context.getChannelMchNo(), context.getCapability());
        return douyinSyncService.sync(context.getTrade(), credential);
    }
}
