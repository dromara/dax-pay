package cn.daxpay.open.channel.epaylinks.strategy.pay;

import cn.daxpay.open.channel.epaylinks.client.credential.EpaylinksSdkCredential;
import cn.daxpay.open.channel.epaylinks.service.direct.EpaylinksDirectConfigAssembler;
import cn.daxpay.open.channel.epaylinks.service.payment.pay.EpaylinksPayService;
import cn.daxpay.open.payment.strategy.pay.AbsNormalPayStrategy;
import cn.daxpay.open.payment.strategy.pay.PayStrategyContext;
import cn.daxpay.open.payment.trade.runtime.bo.PayTradeResultBo;
import cn.daxpay.open.payment.unipay.param.trade.pay.NormalPayParam;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 易票联直连支付策略
///
/// 易票联支付(ProductEnum.EPAY_LINKS)下发起支付的具体执行策略。
/// 配置组装在 [#doBeforePay] 阶段完成(委托 [EpaylinksDirectConfigAssembler]),
/// 支付执行委托给 [EpaylinksPayService]。
@Slf4j
@Service
@RequiredArgsConstructor
public class EpaylinksDirectPayStrategy extends AbsNormalPayStrategy {

    private final EpaylinksPayService epaylinksPayService;
    private final EpaylinksDirectConfigAssembler epaylinksDirectConfigAssembler;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.EPAY_LINKS;
    }

    /// 支付前预处理: 组装直连通道凭证写入上下文
    @Override
    public void doBeforePay(PayStrategyContext context) {
        NormalPayParam payParam = context.getPayParam();
        EpaylinksSdkCredential credential = epaylinksDirectConfigAssembler.buildConfig(
                payParam.getMchNo(), payParam.getChannelMchNo(), payParam.getCapability());
        context.setChannelConfig(credential);
    }

    @Override
    public PayTradeResultBo doPay(PayStrategyContext context) {
        EpaylinksSdkCredential credential = context.getChannelConfig(EpaylinksSdkCredential.class);
        return epaylinksPayService.pay(context.getTrade(), context.getPayParam(), credential);
    }
}
