package cn.daxpay.open.channel.fuyou.strategy;

import cn.daxpay.open.channel.fuyou.client.credential.FuyouSdkCredential;
import cn.daxpay.open.channel.fuyou.service.isv.FuyouIsvConfigAssembler;
import cn.daxpay.open.channel.fuyou.service.payment.FuyouCloseService;
import cn.daxpay.open.payment.core.strategy.pay.AbsPayCloseStrategy;
import cn.daxpay.open.payment.core.strategy.pay.PayStrategyContext;
import cn.daxpay.open.payment.core.trade.entity.NormalPayOrder;
import cn.daxpay.open.payment.core.trade.entity.PayTrade;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import cn.daxpay.open.platform.core.enums.pay.pay.CloseTypeEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 富友服务商支付关闭策略
///
/// 富友仅提供关单接口(`/closeorder`), 无撤销接口, useCancel 参数在 service 层被忽略。
@Slf4j
@Service
@RequiredArgsConstructor
public class FuyouCloseStrategy extends AbsPayCloseStrategy {

    private final FuyouCloseService fuyouCloseService;
    private final FuyouIsvConfigAssembler fuyouIsvConfigAssembler;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.FUYOU_PAY;
    }

    @Override
    public CloseTypeEnum doClose(PayStrategyContext context, boolean useCancel) {
        NormalPayOrder normalOrder = context.getContainer();
        String channelMchNo = normalOrder != null ? normalOrder.getChannelMchNo() : null;
        String capability = normalOrder != null ? normalOrder.getCapability() : null;
        PayTrade trade = context.getTrade();

        FuyouSdkCredential credential = fuyouIsvConfigAssembler.buildConfig(
                trade.getMchNo(), channelMchNo, capability);
        return fuyouCloseService.close(trade, credential, useCancel);
    }
}
