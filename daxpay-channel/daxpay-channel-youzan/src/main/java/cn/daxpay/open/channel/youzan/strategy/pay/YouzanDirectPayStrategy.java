package cn.daxpay.open.channel.youzan.strategy.pay;

import cn.daxpay.open.channel.youzan.client.credential.YouzanSdkCredential;
import cn.daxpay.open.channel.youzan.service.direct.YouzanDirectConfigAssembler;
import cn.daxpay.open.channel.youzan.service.payment.pay.YouzanPayService;
import cn.daxpay.open.payment.strategy.pay.AbsNormalPayStrategy;
import cn.daxpay.open.payment.strategy.pay.PayStrategyContext;
import cn.daxpay.open.payment.trade.runtime.bo.PayTradeResultBo;
import cn.daxpay.open.payment.unipay.param.trade.pay.NormalPayParam;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 有赞直连支付策略
///
/// 有赞支付(ProductEnum.YOUZAN_PAY)下发起支付的具体执行策略。
/// 配置组装在 [#doBeforePay] 阶段完成(委托 [YouzanDirectConfigAssembler]),
/// 支付执行委托给 [YouzanPayService]。
@Slf4j
@Service
@RequiredArgsConstructor
public class YouzanDirectPayStrategy extends AbsNormalPayStrategy {

    private final YouzanPayService youzanPayService;
    private final YouzanDirectConfigAssembler youzanDirectConfigAssembler;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.YOUZAN_PAY;
    }

    /// 支付前预处理: 组装直连通道凭证写入上下文
    @Override
    public void doBeforePay(PayStrategyContext context) {
        NormalPayParam payParam = context.getPayParam();
        YouzanSdkCredential credential = youzanDirectConfigAssembler.buildConfig(
                payParam.getMchNo(), payParam.getChannelMchNo(), payParam.getCapability());
        context.setChannelConfig(credential);
    }

    @Override
    public PayTradeResultBo doPay(PayStrategyContext context) {
        YouzanSdkCredential credential = context.getChannelConfig(YouzanSdkCredential.class);
        return youzanPayService.pay(context.getTrade(), context.getPayParam(), credential);
    }
}
