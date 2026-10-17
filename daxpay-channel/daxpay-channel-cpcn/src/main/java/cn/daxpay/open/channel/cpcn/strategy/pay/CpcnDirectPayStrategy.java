package cn.daxpay.open.channel.cpcn.strategy.pay;

import cn.daxpay.open.channel.cpcn.client.credential.CpcnSdkCredential;
import cn.daxpay.open.channel.cpcn.service.direct.CpcnDirectConfigAssembler;
import cn.daxpay.open.channel.cpcn.service.payment.pay.CpcnPayService;
import cn.daxpay.open.payment.strategy.pay.AbsNormalPayStrategy;
import cn.daxpay.open.payment.strategy.pay.PayStrategyContext;
import cn.daxpay.open.payment.trade.runtime.bo.PayTradeResultBo;
import cn.daxpay.open.payment.unipay.param.trade.pay.NormalPayParam;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 中金直连支付策略
///
/// 中金支付(ProductEnum.CPCN_PAY)下发起支付的具体执行策略。
/// 配置组装在 [#doBeforePay] 阶段完成(委托 [CpcnDirectConfigAssembler]),
/// 支付执行委托给 [CpcnPayService]。
@Slf4j
@Service
@RequiredArgsConstructor
public class CpcnDirectPayStrategy extends AbsNormalPayStrategy {

    private final CpcnPayService cpcnPayService;
    private final CpcnDirectConfigAssembler cpcnDirectConfigAssembler;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.CPCN_PAY;
    }

    /// 支付前预处理: 组装直连通道凭证写入上下文
    @Override
    public void doBeforePay(PayStrategyContext context) {
        NormalPayParam payParam = context.getPayParam();
        CpcnSdkCredential credential = cpcnDirectConfigAssembler.buildConfig(
                payParam.getMchNo(), payParam.getChannelMchNo(), payParam.getCapability());
        context.setChannelConfig(credential);
    }

    @Override
    public PayTradeResultBo doPay(PayStrategyContext context) {
        CpcnSdkCredential credential = context.getChannelConfig(CpcnSdkCredential.class);
        return cpcnPayService.pay(context.getTrade(), context.getPayParam(), credential);
    }
}
