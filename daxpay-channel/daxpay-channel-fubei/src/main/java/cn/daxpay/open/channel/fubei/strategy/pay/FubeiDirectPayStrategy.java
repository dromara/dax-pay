package cn.daxpay.open.channel.fubei.strategy.pay;

import cn.daxpay.open.channel.fubei.client.credential.FubeiSdkCredential;
import cn.daxpay.open.channel.fubei.service.direct.FubeiDirectConfigAssembler;
import cn.daxpay.open.channel.fubei.service.payment.pay.FubeiPayService;
import cn.daxpay.open.payment.strategy.pay.AbsNormalPayStrategy;
import cn.daxpay.open.payment.strategy.pay.PayStrategyContext;
import cn.daxpay.open.payment.trade.runtime.bo.PayTradeResultBo;
import cn.daxpay.open.payment.unipay.param.trade.pay.NormalPayParam;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 付呗直连支付策略
///
/// 付呗支付(ProductEnum.FUBEI_PAY)下发起支付的具体执行策略。
/// 配置组装在 [#doBeforePay] 阶段完成(委托 [FubeiDirectConfigAssembler]),
/// 支付执行委托给 [FubeiPayService]。
@Slf4j
@Service
@RequiredArgsConstructor
public class FubeiDirectPayStrategy extends AbsNormalPayStrategy {

    private final FubeiPayService fubeiPayService;
    private final FubeiDirectConfigAssembler fubeiDirectConfigAssembler;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.FUBEI_PAY;
    }

    /// 支付前预处理: 组装直连通道凭证写入上下文
    @Override
    public void doBeforePay(PayStrategyContext context) {
        NormalPayParam payParam = context.getPayParam();
        FubeiSdkCredential credential = fubeiDirectConfigAssembler.buildConfig(
                payParam.getMchNo(), payParam.getChannelMchNo(), payParam.getCapability());
        context.setChannelConfig(credential);
    }

    @Override
    public PayTradeResultBo doPay(PayStrategyContext context) {
        FubeiSdkCredential credential = context.getChannelConfig(FubeiSdkCredential.class);
        return fubeiPayService.pay(context.getTrade(), context.getPayParam(), credential);
    }
}
