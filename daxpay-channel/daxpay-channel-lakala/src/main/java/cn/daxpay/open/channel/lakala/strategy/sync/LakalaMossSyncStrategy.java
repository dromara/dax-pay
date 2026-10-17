package cn.daxpay.open.channel.lakala.strategy.sync;

import cn.daxpay.open.channel.lakala.client.credential.MossSdkCredential;
import cn.daxpay.open.channel.lakala.service.isv.LakalaMossConfigAssembler;
import cn.daxpay.open.channel.lakala.service.moss.LakalaMossSyncService;
import cn.daxpay.open.payment.strategy.pay.PayStrategyContext;
import cn.daxpay.open.payment.strategy.sync.AbsSyncPayOrderStrategy;
import cn.daxpay.open.payment.trade.runtime.bo.PaySyncResultBo;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 拉卡拉 MOSS 支付同步策略
///
/// 从 trade 读取通道路由参数(channelMchNo / capability),
/// 组装通道凭证(委托 [LakalaMossConfigAssembler]), 同步执行委托给 [LakalaMossSyncService]。
@Slf4j
@Service
@RequiredArgsConstructor
public class LakalaMossSyncStrategy extends AbsSyncPayOrderStrategy {

    private final LakalaMossSyncService lakalaMossSyncService;
    private final LakalaMossConfigAssembler lakalaMossConfigAssembler;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.LAKALA_MOSS;
    }

    @Override
    public PaySyncResultBo doSync(PayStrategyContext context) {
        // 直接从 trade 读取路由参数, 不再需要 container 中间层
        // 组装通道调用凭证
        MossSdkCredential credential = lakalaMossConfigAssembler.buildConfig(
                context.getTrade().getMchNo(), context.getChannelMchNo(), context.getCapability());

        return lakalaMossSyncService.sync(context.getTrade(), credential);
    }
}
