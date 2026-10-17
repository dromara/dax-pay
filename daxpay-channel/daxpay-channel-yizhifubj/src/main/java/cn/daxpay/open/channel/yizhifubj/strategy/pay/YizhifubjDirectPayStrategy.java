package cn.daxpay.open.channel.yizhifubj.strategy.pay;

import cn.daxpay.open.channel.yizhifubj.client.credential.YizhifubjSdkCredential;
import cn.daxpay.open.channel.yizhifubj.service.direct.YizhifubjDirectConfigAssembler;
import cn.daxpay.open.channel.yizhifubj.service.payment.pay.YizhifubjPayService;
import cn.daxpay.open.payment.strategy.pay.AbsNormalPayStrategy;
import cn.daxpay.open.payment.strategy.pay.PayStrategyContext;
import cn.daxpay.open.payment.trade.runtime.bo.PayTradeResultBo;
import cn.daxpay.open.payment.unipay.param.trade.pay.NormalPayParam;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 一直付直连支付策略
///
/// 一直付支付(ProductEnum.YIZHIFU_PAY)下发起支付的具体执行策略。
/// 配置组装在 [#doBeforePay] 阶段完成(委托 [YizhifubjDirectConfigAssembler]),
/// 支付执行委托给 [YizhifubjPayService]。
@Slf4j
@Service
@RequiredArgsConstructor
public class YizhifubjDirectPayStrategy extends AbsNormalPayStrategy {

    private final YizhifubjPayService yizhifubjPayService;
    private final YizhifubjDirectConfigAssembler yizhifubjDirectConfigAssembler;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.YIZHIFU_PAY;
    }

    /// 支付前预处理: 组装直连通道凭证写入上下文
    @Override
    public void doBeforePay(PayStrategyContext context) {
        NormalPayParam payParam = context.getPayParam();
        YizhifubjSdkCredential credential = yizhifubjDirectConfigAssembler.buildConfig(
                payParam.getMchNo(), payParam.getChannelMchNo(), payParam.getCapability());
        context.setChannelConfig(credential);
    }

    @Override
    public PayTradeResultBo doPay(PayStrategyContext context) {
        YizhifubjSdkCredential credential = context.getChannelConfig(YizhifubjSdkCredential.class);
        return yizhifubjPayService.pay(context.getTrade(), context.getPayParam(), credential);
    }
}
