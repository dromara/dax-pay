package cn.daxpay.open.channel.fuyou.strategy.pay;

import cn.daxpay.open.channel.fuyou.client.credential.FuyouDirectSdkCredential;
import cn.daxpay.open.channel.fuyou.service.direct.FuyouDirectConfigAssembler;
import cn.daxpay.open.channel.fuyou.service.payment.FuyouDirectPayService;
import cn.daxpay.open.payment.strategy.pay.AbsNormalPayStrategy;
import cn.daxpay.open.payment.strategy.pay.PayStrategyContext;
import cn.daxpay.open.payment.trade.runtime.bo.PayTradeResultBo;
import cn.daxpay.open.payment.unipay.param.trade.pay.NormalPayParam;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 富友直连支付策略
///
/// 富友商户前置(ProductEnum.FUYOU_DIRECT)下发起支付的具体执行策略。
/// 配置组装在 [#doBeforePay] 阶段完成(委托 [FuyouDirectConfigAssembler]), 支付执行委托给 [FuyouDirectPayService]。
@Slf4j
@Service
@RequiredArgsConstructor
public class FuyouDirectPayStrategy extends AbsNormalPayStrategy {

    private final FuyouDirectPayService fuyouDirectPayService;
    private final FuyouDirectConfigAssembler fuyouDirectConfigAssembler;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.FUYOU_DIRECT;
    }

    /// 支付前预处理: 从容器读取通道路由参数, 组装通道凭证写入上下文
    @Override
    public void doBeforePay(PayStrategyContext context) {
        NormalPayParam payParam = context.getPayParam();
        FuyouDirectSdkCredential credential = fuyouDirectConfigAssembler.buildConfig(
                payParam.getMchNo(), payParam.getChannelMchNo(), payParam.getCapability());
        context.setChannelConfig(credential);
        // 微信 JSAPI/小程序解析微信应用(sub_appid)回填 channelAppId, 供子应用上送与订单落库; 未命中不阻断
        fuyouDirectConfigAssembler.resolveWxAppIfRequired(payParam);
    }

    @Override
    public PayTradeResultBo doPay(PayStrategyContext context) {
        FuyouDirectSdkCredential credential = context.getChannelConfig(FuyouDirectSdkCredential.class);
        return fuyouDirectPayService.pay(context.getTrade(), context.getPayParam(), credential);
    }
}
