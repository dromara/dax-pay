package cn.daxpay.open.channel.sheng.strategy.sync;

import cn.daxpay.open.channel.sheng.client.credential.ShengSdkCredential;
import cn.daxpay.open.channel.sheng.service.config.ShengIsvConfigAssembler;
import cn.daxpay.open.channel.sheng.service.payment.ShengSyncService;
import cn.daxpay.open.payment.strategy.pay.PayStrategyContext;
import cn.daxpay.open.payment.strategy.sync.AbsSyncPayOrderStrategy;
import cn.daxpay.open.payment.trade.runtime.bo.PaySyncResultBo;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 盛付通服务商(ISV)支付同步策略
///
/// 从 trade 读取通道路由参数(channelMchNo / capability),
/// 组装通道凭证(委托 [ShengIsvConfigAssembler]: 服务商密钥全局一份 + 绑定行子商户身份),
/// 同步执行委托给 [ShengSyncService](两模式共用, 聚合 API 同网关同端点同签名)。
///
/// 注意: getProduct 必须返回 SHENG_ISV(策略工厂对同产品码静默吞并, 误写会运行时走错凭证源)。
@Slf4j
@Service
@RequiredArgsConstructor
public class ShengIsvSyncStrategy extends AbsSyncPayOrderStrategy {

    private final ShengSyncService shengSyncService;
    private final ShengIsvConfigAssembler shengIsvConfigAssembler;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.SHENG_ISV;
    }

    @Override
    public PaySyncResultBo doSync(PayStrategyContext context) {
        // 直接从 trade 读取路由参数, 不再需要 container 中间层
        // 组装通道调用凭证
        ShengSdkCredential credential = shengIsvConfigAssembler.buildConfig(
                context.getTrade().getMchNo(), context.getChannelMchNo(), context.getCapability());

        return shengSyncService.sync(context.getTrade(), credential);
    }
}
