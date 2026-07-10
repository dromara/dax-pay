package cn.daxpay.open.channel.hkrt.strategy;

import cn.daxpay.open.channel.hkrt.client.credential.HkrtSdkCredential;
import cn.daxpay.open.channel.hkrt.service.isv.HkrtIsvConfigAssembler;
import cn.daxpay.open.channel.hkrt.service.payment.HkrtPayService;
import cn.daxpay.open.payment.core.strategy.pay.AbsNormalPayStrategy;
import cn.daxpay.open.payment.core.strategy.pay.PayStrategyContext;
import cn.daxpay.open.payment.core.trade.bo.PayTradeResultBo;
import cn.daxpay.open.payment.unipay.param.trade.pay.NormalPayParam;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 海科融通服务商支付策略
///
/// 海科融通支付(ProductEnum.HKRT_PAY)下发起支付的具体执行策略。
/// 配置组装在 [#doBeforePay] 阶段完成(委托 [HkrtIsvConfigAssembler]), 支付执行委托给 [HkrtPayService]。
///
/// 海科融通为聚合服务商模式, 仅有一个产品 HKRT_PAY。
@Slf4j
@Service
@RequiredArgsConstructor
public class HkrtPayStrategy extends AbsNormalPayStrategy {

    private final HkrtPayService hkrtPayService;
    private final HkrtIsvConfigAssembler hkrtIsvConfigAssembler;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.HKRT_PAY;
    }

    /// 支付前预处理: 从容器读取通道路由参数, 组装通道凭证写入上下文
    /// 配置缺失在此阶段抛异常, fail-fast, 不会进入 doPay
    @Override
    public void doBeforePay(PayStrategyContext context) {
        NormalPayParam payParam = context.getPayParam();
        HkrtSdkCredential credential = hkrtIsvConfigAssembler.buildConfig(
                payParam.getMchNo(), payParam.getChannelMchNo(), payParam.getCapability());
        context.setChannelConfig(credential);
    }

    @Override
    public PayTradeResultBo doPay(PayStrategyContext context) {
        // 直接使用 doBeforePay 预组装的通道凭证
        HkrtSdkCredential credential = context.getChannelConfig(HkrtSdkCredential.class);
        return hkrtPayService.pay(context.getTrade(), context.getPayParam(), credential);
    }
}
