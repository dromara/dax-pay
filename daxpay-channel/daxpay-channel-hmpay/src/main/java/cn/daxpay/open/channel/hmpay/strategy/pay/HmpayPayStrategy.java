package cn.daxpay.open.channel.hmpay.strategy.pay;

import cn.daxpay.open.channel.hmpay.client.credential.HmpaySdkCredential;
import cn.daxpay.open.channel.hmpay.service.isv.HmpayIsvConfigAssembler;
import cn.daxpay.open.channel.hmpay.service.payment.HmpayPayService;
import cn.daxpay.open.payment.strategy.pay.AbsNormalPayStrategy;
import cn.daxpay.open.payment.strategy.pay.PayStrategyContext;
import cn.daxpay.open.payment.trade.runtime.bo.PayTradeResultBo;
import cn.daxpay.open.payment.unipay.param.trade.pay.NormalPayParam;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 河马付服务商支付策略
///
/// 配置组装在 [#doBeforePay] 阶段完成(委托 [HmpayIsvConfigAssembler]),
/// 支付执行委托给 [HmpayPayService]。
@Slf4j
@Service
@RequiredArgsConstructor
public class HmpayPayStrategy extends AbsNormalPayStrategy {

    private final HmpayPayService hmpayPayService;
    private final HmpayIsvConfigAssembler hmpayIsvConfigAssembler;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.HM_PAY;
    }

    /// 支付前预处理: 从容器读取通道路由参数, 组装通道凭证写入上下文
    @Override
    public void doBeforePay(PayStrategyContext context) {
        NormalPayParam payParam = context.getPayParam();
        HmpaySdkCredential credential = hmpayIsvConfigAssembler.buildConfig(
                payParam.getMchNo(), payParam.getChannelMchNo(), payParam.getCapability());
        context.setChannelConfig(credential);
    }

    @Override
    public PayTradeResultBo doPay(PayStrategyContext context) {
        HmpaySdkCredential credential = context.getChannelConfig(HmpaySdkCredential.class);
        return hmpayPayService.pay(context.getTrade(), context.getPayParam(), credential);
    }
}
