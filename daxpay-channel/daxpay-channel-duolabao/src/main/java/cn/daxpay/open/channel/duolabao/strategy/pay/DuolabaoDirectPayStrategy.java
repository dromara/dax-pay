package cn.daxpay.open.channel.duolabao.strategy.pay;

import cn.daxpay.open.channel.duolabao.client.credential.DuolabaoSdkCredential;
import cn.daxpay.open.channel.duolabao.service.direct.DuolabaoDirectConfigAssembler;
import cn.daxpay.open.channel.duolabao.service.payment.pay.DuolabaoPayService;
import cn.daxpay.open.payment.strategy.pay.AbsNormalPayStrategy;
import cn.daxpay.open.payment.strategy.pay.PayStrategyContext;
import cn.daxpay.open.payment.trade.runtime.bo.PayTradeResultBo;
import cn.daxpay.open.payment.unipay.param.trade.pay.NormalPayParam;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 哆啦宝直连支付策略
///
/// 哆啦宝支付(ProductEnum.DUOLABAO_PAY)下发起支付的具体执行策略。
/// 配置组装在 [#doBeforePay] 阶段完成(委托 [DuolabaoDirectConfigAssembler]),
/// 支付执行委托给 [DuolabaoPayService]。
@Slf4j
@Service
@RequiredArgsConstructor
public class DuolabaoDirectPayStrategy extends AbsNormalPayStrategy {

    private final DuolabaoPayService duolabaoPayService;

    private final DuolabaoDirectConfigAssembler duolabaoDirectConfigAssembler;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.DUOLABAO_PAY;
    }

    /// 支付前预处理: 组装直连通道凭证写入上下文
    @Override
    public void doBeforePay(PayStrategyContext context) {
        NormalPayParam payParam = context.getPayParam();
        DuolabaoSdkCredential credential = duolabaoDirectConfigAssembler.buildConfig(
                payParam.getMchNo(), payParam.getChannelMchNo(), payParam.getCapability());
        context.setChannelConfig(credential);
    }

    @Override
    public PayTradeResultBo doPay(PayStrategyContext context) {
        DuolabaoSdkCredential credential = context.getChannelConfig(DuolabaoSdkCredential.class);
        return duolabaoPayService.pay(context.getTrade(), context.getPayParam(), credential);
    }
}
