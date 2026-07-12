package cn.daxpay.open.channel.hkrt.strategy;

import cn.daxpay.open.channel.hkrt.client.credential.HkrtSdkCredential;
import cn.daxpay.open.channel.hkrt.service.isv.HkrtIsvConfigAssembler;
import cn.daxpay.open.channel.hkrt.service.payment.HkrtSyncService;
import cn.daxpay.open.payment.core.strategy.pay.PayStrategyContext;
import cn.daxpay.open.payment.core.strategy.sync.AbsSyncPayOrderStrategy;
import cn.daxpay.open.payment.core.trade.runtime.bo.PaySyncResultBo;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 海科融通服务商支付同步策略
///
/// 从上下文容器读取通道路由参数(channelMchNo / capability),
/// 组装通道凭证(委托 [HkrtIsvConfigAssembler]), 同步执行委托给 [HkrtSyncService]。
@Slf4j
@Service
@RequiredArgsConstructor
public class HkrtSyncStrategy extends AbsSyncPayOrderStrategy {

    private final HkrtSyncService hkrtSyncService;
    private final HkrtIsvConfigAssembler hkrtIsvConfigAssembler;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.HKRT_PAY;
    }

    @Override
    public PaySyncResultBo doSync(PayStrategyContext context) {
        // 直接从 trade 读取路由参数, 不再需要 container 中间层

        // 组装通道调用凭证
        HkrtSdkCredential credential = hkrtIsvConfigAssembler.buildConfig(
                context.getTrade().getMchNo(), context.getChannelMchNo(), context.getCapability());

        return hkrtSyncService.sync(context.getTrade(), credential);
    }
}
