package cn.daxpay.open.channel.xingyi.strategy.pay;

import cn.daxpay.open.channel.xingyi.client.credential.XingyiSdkCredential;
import cn.daxpay.open.channel.xingyi.service.direct.XingyiDirectConfigAssembler;
import cn.daxpay.open.channel.xingyi.service.payment.pay.XingyiPayService;
import cn.daxpay.open.payment.strategy.pay.AbsNormalPayStrategy;
import cn.daxpay.open.payment.strategy.pay.PayStrategyContext;
import cn.daxpay.open.payment.trade.runtime.bo.PayTradeResultBo;
import cn.daxpay.open.payment.unipay.param.trade.pay.NormalPayParam;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 星驿付直连支付策略
///
/// 星驿付支付(ProductEnum.XINGYI_PAY)下发起支付的具体执行策略。
/// 配置组装在 [#doBeforePay] 阶段完成(委托 [XingyiDirectConfigAssembler]),
/// 支付执行委托给 [XingyiPayService]。
@Slf4j
@Service
@RequiredArgsConstructor
public class XingyiDirectPayStrategy extends AbsNormalPayStrategy {

    private final XingyiPayService xingyiPayService;

    private final XingyiDirectConfigAssembler xingyiDirectConfigAssembler;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.XINGYI_PAY;
    }

    /// 支付前预处理: 组装直连通道凭证写入上下文
    @Override
    public void doBeforePay(PayStrategyContext context) {
        NormalPayParam payParam = context.getPayParam();
        XingyiSdkCredential credential = xingyiDirectConfigAssembler.buildConfig(
                payParam.getMchNo(), payParam.getChannelMchNo(), payParam.getCapability());
        context.setChannelConfig(credential);
    }

    @Override
    public PayTradeResultBo doPay(PayStrategyContext context) {
        XingyiSdkCredential credential = context.getChannelConfig(XingyiSdkCredential.class);
        return xingyiPayService.pay(context.getTrade(), context.getPayParam(), credential);
    }
}
