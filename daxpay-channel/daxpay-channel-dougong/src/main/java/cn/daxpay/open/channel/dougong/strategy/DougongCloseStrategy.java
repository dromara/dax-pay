package cn.daxpay.open.channel.dougong.strategy;

import cn.daxpay.open.channel.dougong.client.credential.DougongSdkCredential;
import cn.daxpay.open.channel.dougong.service.isv.DougongIsvConfigAssembler;
import cn.daxpay.open.channel.dougong.service.payment.DougongCloseService;
import cn.daxpay.open.payment.core.strategy.pay.AbsPayCloseStrategy;
import cn.daxpay.open.payment.core.strategy.pay.PayStrategyContext;
import cn.daxpay.open.payment.core.trade.entity.NormalPayOrder;
import cn.daxpay.open.payment.core.trade.entity.PayTrade;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import cn.daxpay.open.platform.core.enums.pay.pay.CloseTypeEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 斗拱服务商支付关闭策略
///
/// 注意: 汇付仅提供关单接口, 无撤销接口(useCancel 参数由 service 层忽略)。
@Slf4j
@Service
@RequiredArgsConstructor
public class DougongCloseStrategy extends AbsPayCloseStrategy {

    private final DougongCloseService dougongCloseService;
    private final DougongIsvConfigAssembler dougongIsvConfigAssembler;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.DOUGONG_PAY;
    }

    @Override
    public CloseTypeEnum doClose(PayStrategyContext context, boolean useCancel) {
        NormalPayOrder normalOrder = context.getContainer();
        String channelMchNo = normalOrder != null ? normalOrder.getChannelMchNo() : null;
        String capability = normalOrder != null ? normalOrder.getCapability() : null;
        PayTrade trade = context.getTrade();

        DougongSdkCredential credential = dougongIsvConfigAssembler.buildConfig(
                trade.getMchNo(), channelMchNo, capability);

        String clientIp = normalOrder != null ? normalOrder.getClientIp() : null;
        return dougongCloseService.close(trade, credential, useCancel, clientIp);
    }
}
