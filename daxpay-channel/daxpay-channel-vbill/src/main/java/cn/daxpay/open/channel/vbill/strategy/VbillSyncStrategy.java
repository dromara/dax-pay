package cn.daxpay.open.channel.vbill.strategy;

import cn.daxpay.open.channel.vbill.client.credential.VbillSdkCredential;
import cn.daxpay.open.channel.vbill.service.isv.VbillIsvConfigAssembler;
import cn.daxpay.open.channel.vbill.service.payment.VbillSyncService;
import cn.daxpay.open.payment.core.strategy.pay.PayStrategyContext;
import cn.daxpay.open.payment.core.strategy.sync.AbsSyncPayOrderStrategy;
import cn.daxpay.open.payment.core.trade.bo.PaySyncResultBo;
import cn.daxpay.open.payment.core.trade.entity.NormalPayOrder;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 随行付服务商支付同步策略
///
/// 从上下文容器读取通道路由参数(channelMchNo / capability),
/// 组装通道凭证(委托 [VbillIsvConfigAssembler]), 同步执行委托给 [VbillSyncService]。
@Slf4j
@Service
@RequiredArgsConstructor
public class VbillSyncStrategy extends AbsSyncPayOrderStrategy {

    private final VbillSyncService vbillSyncService;
    private final VbillIsvConfigAssembler vbillIsvConfigAssembler;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.VBILL_PAY;
    }

    @Override
    public PaySyncResultBo doSync(PayStrategyContext context) {
        // 从上下文容器读取通道路由参数
        NormalPayOrder normalOrder = context.getContainer();
        String channelMchNo = normalOrder != null ? normalOrder.getChannelMchNo() : null;
        String capability = normalOrder != null ? normalOrder.getCapability() : null;

        // 组装通道调用凭证
        VbillSdkCredential credential = vbillIsvConfigAssembler.buildConfig(
                context.getTrade().getMchNo(), channelMchNo, capability);

        return vbillSyncService.sync(context.getTrade(), credential);
    }
}
