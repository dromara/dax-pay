package cn.daxpay.open.channel.umfpay.strategy.pay;

import cn.daxpay.open.channel.umfpay.client.credential.UmfpaySdkCredential;
import cn.daxpay.open.channel.umfpay.service.direct.UmfpayDirectConfigAssembler;
import cn.daxpay.open.channel.umfpay.service.payment.pay.UmfpayPayService;
import cn.daxpay.open.payment.strategy.pay.AbsNormalPayStrategy;
import cn.daxpay.open.payment.strategy.pay.PayStrategyContext;
import cn.daxpay.open.payment.trade.runtime.bo.PayTradeResultBo;
import cn.daxpay.open.payment.unipay.param.trade.pay.NormalPayParam;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 联动优势直连支付策略
///
/// 联动优势支付(ProductEnum.UMF_PAY)下发起支付的具体执行策略。
/// 配置组装在 [#doBeforePay] 阶段完成(委托 [UmfpayDirectConfigAssembler]),
/// 支付执行委托给 [UmfpayPayService]。
@Slf4j
@Service
@RequiredArgsConstructor
public class UmfpayDirectPayStrategy extends AbsNormalPayStrategy {

    private final UmfpayPayService umfpayPayService;
    private final UmfpayDirectConfigAssembler umfpayDirectConfigAssembler;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.UMF_PAY;
    }

    /// 支付前预处理: 组装直连通道凭证写入上下文
    @Override
    public void doBeforePay(PayStrategyContext context) {
        NormalPayParam payParam = context.getPayParam();
        UmfpaySdkCredential credential = umfpayDirectConfigAssembler.buildConfig(
                payParam.getMchNo(), payParam.getChannelMchNo(), payParam.getCapability());
        context.setChannelConfig(credential);
    }

    @Override
    public PayTradeResultBo doPay(PayStrategyContext context) {
        UmfpaySdkCredential credential = context.getChannelConfig(UmfpaySdkCredential.class);
        return umfpayPayService.pay(context.getTrade(), context.getPayParam(), credential);
    }
}
