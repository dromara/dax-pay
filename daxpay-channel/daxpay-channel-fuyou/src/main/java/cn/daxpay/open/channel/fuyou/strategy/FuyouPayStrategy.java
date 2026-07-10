package cn.daxpay.open.channel.fuyou.strategy;

import cn.daxpay.open.channel.fuyou.client.credential.FuyouSdkCredential;
import cn.daxpay.open.channel.fuyou.service.isv.FuyouIsvConfigAssembler;
import cn.daxpay.open.channel.fuyou.service.payment.FuyouPayService;
import cn.daxpay.open.payment.core.strategy.pay.AbsNormalPayStrategy;
import cn.daxpay.open.payment.core.strategy.pay.PayStrategyContext;
import cn.daxpay.open.payment.core.trade.bo.PayTradeResultBo;
import cn.daxpay.open.payment.unipay.param.trade.pay.NormalPayParam;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 富友服务商支付策略
///
/// 富友支付(ProductEnum.FUYOU_PAY)下发起支付的具体执行策略。
/// 配置组装在 [#doBeforePay] 阶段完成(委托 [FuyouIsvConfigAssembler]), 支付执行委托给 [FuyouPayService]。
///
/// 富友为聚合服务商模式, 不区分直连/服务商, 仅有一个产品 FUYOU_PAY。
@Slf4j
@Service
@RequiredArgsConstructor
public class FuyouPayStrategy extends AbsNormalPayStrategy {

    private final FuyouPayService fuyouPayService;
    private final FuyouIsvConfigAssembler fuyouIsvConfigAssembler;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.FUYOU_PAY;
    }

    /// 支付前预处理: 从容器读取通道路由参数, 组装通道凭证写入上下文
    @Override
    public void doBeforePay(PayStrategyContext context) {
        NormalPayParam payParam = context.getPayParam();
        FuyouSdkCredential credential = fuyouIsvConfigAssembler.buildConfig(
                payParam.getMchNo(), payParam.getChannelMchNo(), payParam.getCapability());
        context.setChannelConfig(credential);
    }

    @Override
    public PayTradeResultBo doPay(PayStrategyContext context) {
        FuyouSdkCredential credential = context.getChannelConfig(FuyouSdkCredential.class);
        return fuyouPayService.pay(context.getTrade(), context.getPayParam(), credential);
    }
}
