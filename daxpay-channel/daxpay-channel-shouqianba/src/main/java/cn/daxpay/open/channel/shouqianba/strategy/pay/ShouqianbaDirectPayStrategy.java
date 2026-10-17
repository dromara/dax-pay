package cn.daxpay.open.channel.shouqianba.strategy.pay;

import cn.daxpay.open.channel.shouqianba.client.credential.ShouqianbaSdkCredential;
import cn.daxpay.open.channel.shouqianba.service.direct.ShouqianbaDirectConfigAssembler;
import cn.daxpay.open.channel.shouqianba.service.payment.pay.ShouqianbaPayService;
import cn.daxpay.open.payment.strategy.pay.AbsNormalPayStrategy;
import cn.daxpay.open.payment.strategy.pay.PayStrategyContext;
import cn.daxpay.open.payment.trade.runtime.bo.PayTradeResultBo;
import cn.daxpay.open.payment.unipay.param.trade.pay.NormalPayParam;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 收钱吧直连支付策略
///
/// 收钱吧支付(ProductEnum.SHOUQIANBA)下发起支付的具体执行策略。
/// 配置组装在 [#doBeforePay] 阶段完成(委托 [ShouqianbaDirectConfigAssembler]),
/// 支付执行委托给 [ShouqianbaPayService]。
@Slf4j
@Service
@RequiredArgsConstructor
public class ShouqianbaDirectPayStrategy extends AbsNormalPayStrategy {

    private final ShouqianbaPayService shouqianbaPayService;
    private final ShouqianbaDirectConfigAssembler shouqianbaDirectConfigAssembler;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.SHOUQIANBA;
    }

    /// 支付前预处理: 组装直连通道凭证写入上下文
    @Override
    public void doBeforePay(PayStrategyContext context) {
        NormalPayParam payParam = context.getPayParam();
        ShouqianbaSdkCredential credential = shouqianbaDirectConfigAssembler.buildConfig(
                payParam.getMchNo(), payParam.getChannelMchNo(), payParam.getCapability());
        context.setChannelConfig(credential);
    }

    @Override
    public PayTradeResultBo doPay(PayStrategyContext context) {
        ShouqianbaSdkCredential credential = context.getChannelConfig(ShouqianbaSdkCredential.class);
        return shouqianbaPayService.pay(context.getTrade(), context.getPayParam(), credential);
    }
}
