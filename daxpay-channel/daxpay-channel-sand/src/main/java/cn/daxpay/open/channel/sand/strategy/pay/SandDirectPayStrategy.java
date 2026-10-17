package cn.daxpay.open.channel.sand.strategy.pay;

import cn.daxpay.open.channel.sand.client.credential.SandSdkCredential;
import cn.daxpay.open.channel.sand.service.direct.SandDirectConfigAssembler;
import cn.daxpay.open.channel.sand.service.payment.pay.SandPayService;
import cn.daxpay.open.payment.strategy.pay.AbsNormalPayStrategy;
import cn.daxpay.open.payment.strategy.pay.PayStrategyContext;
import cn.daxpay.open.payment.trade.runtime.bo.PayTradeResultBo;
import cn.daxpay.open.payment.unipay.param.trade.pay.NormalPayParam;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 杉德直连支付策略
///
/// 杉德支付(ProductEnum.SAND_PAY)下发起支付的具体执行策略。
/// 配置组装在 [#doBeforePay] 阶段完成(委托 [SandDirectConfigAssembler]),
/// 支付执行委托给 [SandPayService]。
@Slf4j
@Service
@RequiredArgsConstructor
public class SandDirectPayStrategy extends AbsNormalPayStrategy {

    private final SandPayService sandPayService;
    private final SandDirectConfigAssembler sandDirectConfigAssembler;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.SAND_PAY;
    }

    /// 支付前预处理: 组装直连通道凭证写入上下文
    @Override
    public void doBeforePay(PayStrategyContext context) {
        NormalPayParam payParam = context.getPayParam();
        SandSdkCredential credential = sandDirectConfigAssembler.buildConfig(
                payParam.getMchNo(), payParam.getChannelMchNo(), payParam.getCapability());
        context.setChannelConfig(credential);
    }

    @Override
    public PayTradeResultBo doPay(PayStrategyContext context) {
        SandSdkCredential credential = context.getChannelConfig(SandSdkCredential.class);
        return sandPayService.pay(context.getTrade(), context.getPayParam(), credential);
    }
}
