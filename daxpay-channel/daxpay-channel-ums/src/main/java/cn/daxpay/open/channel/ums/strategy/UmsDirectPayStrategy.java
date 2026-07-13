package cn.daxpay.open.channel.ums.strategy;

import cn.daxpay.open.channel.ums.client.credential.UmsSdkCredential;
import cn.daxpay.open.channel.ums.service.direct.UmsDirectConfigAssembler;
import cn.daxpay.open.channel.ums.service.payment.pay.UmsPayService;
import cn.daxpay.open.payment.strategy.pay.AbsNormalPayStrategy;
import cn.daxpay.open.payment.strategy.pay.PayStrategyContext;
import cn.daxpay.open.payment.trade.runtime.bo.PayTradeResultBo;
import cn.daxpay.open.payment.unipay.param.trade.pay.NormalPayParam;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 银联商务直连支付策略
///
/// 银联商务直连模式(ProductEnum.UMS_QRCODE)下发起支付的具体执行策略。
/// 配置组装在 [#doBeforePay] 阶段完成(委托 [UmsDirectConfigAssembler]),
/// 支付执行委托给 [UmsPayService]。
@Slf4j
@Service
@RequiredArgsConstructor
public class UmsDirectPayStrategy extends AbsNormalPayStrategy {

    private final UmsPayService umsPayService;
    private final UmsDirectConfigAssembler umsDirectConfigAssembler;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.UMS_QRCODE;
    }

    /// 支付前预处理: 组装直连通道凭证写入上下文
    @Override
    public void doBeforePay(PayStrategyContext context) {
        NormalPayParam payParam = context.getPayParam();
        UmsSdkCredential credential = umsDirectConfigAssembler.buildConfig(
                payParam.getMchNo(), payParam.getChannelMchNo(), payParam.getCapability());
        context.setChannelConfig(credential);
    }

    @Override
    public PayTradeResultBo doPay(PayStrategyContext context) {
        UmsSdkCredential credential = context.getChannelConfig(UmsSdkCredential.class);
        return umsPayService.pay(context.getTrade(), context.getPayParam(), credential);
    }
}
