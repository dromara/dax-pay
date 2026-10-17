package cn.daxpay.open.channel.baofu.strategy.pay;

import cn.daxpay.open.channel.baofu.client.credential.BaofuSdkCredential;
import cn.daxpay.open.channel.baofu.service.direct.BaofuDirectConfigAssembler;
import cn.daxpay.open.channel.baofu.service.payment.pay.BaofuPayService;
import cn.daxpay.open.payment.strategy.pay.AbsNormalPayStrategy;
import cn.daxpay.open.payment.strategy.pay.PayStrategyContext;
import cn.daxpay.open.payment.trade.runtime.bo.PayTradeResultBo;
import cn.daxpay.open.payment.unipay.param.trade.pay.NormalPayParam;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 宝付直连支付策略
///
/// 宝付支付(ProductEnum.BAOFU_PAY)下发起支付的具体执行策略。
/// 配置组装在 [#doBeforePay] 阶段完成(委托 [BaofuDirectConfigAssembler]),
/// 支付执行委托给 [BaofuPayService]。
@Slf4j
@Service
@RequiredArgsConstructor
public class BaofuDirectPayStrategy extends AbsNormalPayStrategy {

    private final BaofuPayService baofuPayService;
    private final BaofuDirectConfigAssembler baofuDirectConfigAssembler;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.BAOFU_PAY;
    }

    /// 支付前预处理: 组装直连通道凭证写入上下文
    @Override
    public void doBeforePay(PayStrategyContext context) {
        NormalPayParam payParam = context.getPayParam();
        BaofuSdkCredential credential = baofuDirectConfigAssembler.buildConfig(
                payParam.getMchNo(), payParam.getChannelMchNo(), payParam.getCapability());
        context.setChannelConfig(credential);
    }

    @Override
    public PayTradeResultBo doPay(PayStrategyContext context) {
        BaofuSdkCredential credential = context.getChannelConfig(BaofuSdkCredential.class);
        return baofuPayService.pay(context.getTrade(), context.getPayParam(), credential);
    }
}
