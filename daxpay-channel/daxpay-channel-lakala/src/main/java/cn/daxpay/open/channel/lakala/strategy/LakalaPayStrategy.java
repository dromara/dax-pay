package cn.daxpay.open.channel.lakala.strategy;

import cn.daxpay.open.channel.lakala.client.credential.LakalaSdkCredential;
import cn.daxpay.open.channel.lakala.service.isv.LakalaIsvConfigAssembler;
import cn.daxpay.open.channel.lakala.service.payment.LakalaPayService;
import cn.daxpay.open.payment.core.strategy.pay.AbsNormalPayStrategy;
import cn.daxpay.open.payment.core.strategy.pay.PayStrategyContext;
import cn.daxpay.open.payment.core.trade.bo.PayTradeResultBo;
import cn.daxpay.open.payment.unipay.param.trade.pay.NormalPayParam;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 拉卡拉服务商支付策略
///
/// 拉卡拉支付(ProductEnum.LAKALA_PAY)下发起支付的具体执行策略。
/// 配置组装在 [#doBeforePay] 阶段完成(委托 [LakalaIsvConfigAssembler]), 支付执行委托给 [LakalaPayService]。
///
/// 拉卡拉为聚合服务商模式, 不区分直连/服务商, 仅有一个产品 LAKALA_PAY。
@Slf4j
@Service
@RequiredArgsConstructor
public class LakalaPayStrategy extends AbsNormalPayStrategy {

    private final LakalaPayService lakalaPayService;
    private final LakalaIsvConfigAssembler lakalaIsvConfigAssembler;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.LAKALA_PAY;
    }

    /// 支付前预处理: 从容器读取通道路由参数, 组装通道凭证写入上下文
    /// 配置缺失在此阶段抛异常, fail-fast, 不会进入 doPay
    @Override
    public void doBeforePay(PayStrategyContext context) {
        NormalPayParam payParam = context.getPayParam();
        LakalaSdkCredential credential = lakalaIsvConfigAssembler.buildConfig(
                payParam.getMchNo(), payParam.getChannelMchNo(), payParam.getCapability());
        context.setChannelConfig(credential);
    }

    @Override
    public PayTradeResultBo doPay(PayStrategyContext context) {
        // 直接使用 doBeforePay 预组装的通道凭证
        LakalaSdkCredential credential = context.getChannelConfig(LakalaSdkCredential.class);
        return lakalaPayService.pay(context.getTrade(), context.getPayParam(), credential);
    }
}
