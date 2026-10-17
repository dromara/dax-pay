package cn.daxpay.open.channel.kuaiqian.strategy.pay;

import cn.daxpay.open.channel.kuaiqian.client.credential.KuaiqianSdkCredential;
import cn.daxpay.open.channel.kuaiqian.service.direct.KuaiqianDirectConfigAssembler;
import cn.daxpay.open.channel.kuaiqian.service.payment.pay.KuaiqianPayService;
import cn.daxpay.open.payment.strategy.pay.AbsNormalPayStrategy;
import cn.daxpay.open.payment.strategy.pay.PayStrategyContext;
import cn.daxpay.open.payment.trade.runtime.bo.PayTradeResultBo;
import cn.daxpay.open.payment.unipay.param.trade.pay.NormalPayParam;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 快钱直连支付策略
///
/// 快钱支付(ProductEnum.KUAIQIAN_PAY)下发起支付的具体执行策略。
/// 配置组装在 [#doBeforePay] 阶段完成(委托 [KuaiqianDirectConfigAssembler]),
/// 支付执行委托给 [KuaiqianPayService]。
@Slf4j
@Service
@RequiredArgsConstructor
public class KuaiqianDirectPayStrategy extends AbsNormalPayStrategy {

    private final KuaiqianPayService kuaiqianPayService;
    private final KuaiqianDirectConfigAssembler kuaiqianDirectConfigAssembler;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.KUAIQIAN_PAY;
    }

    /// 支付前预处理: 组装直连通道凭证写入上下文
    @Override
    public void doBeforePay(PayStrategyContext context) {
        NormalPayParam payParam = context.getPayParam();
        KuaiqianSdkCredential credential = kuaiqianDirectConfigAssembler.buildConfig(
                payParam.getMchNo(), payParam.getChannelMchNo(), payParam.getCapability());
        context.setChannelConfig(credential);
    }

    @Override
    public PayTradeResultBo doPay(PayStrategyContext context) {
        KuaiqianSdkCredential credential = context.getChannelConfig(KuaiqianSdkCredential.class);
        return kuaiqianPayService.pay(context.getTrade(), context.getPayParam(), credential);
    }
}
