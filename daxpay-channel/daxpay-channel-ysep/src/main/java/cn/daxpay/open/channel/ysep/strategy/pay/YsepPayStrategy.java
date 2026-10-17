package cn.daxpay.open.channel.ysep.strategy.pay;

import cn.daxpay.open.channel.ysep.client.credential.YsepSdkCredential;
import cn.daxpay.open.channel.ysep.service.config.YsepConfigAssembler;
import cn.daxpay.open.channel.ysep.service.payment.YsepPayService;
import cn.daxpay.open.payment.strategy.pay.AbsNormalPayStrategy;
import cn.daxpay.open.payment.strategy.pay.PayStrategyContext;
import cn.daxpay.open.payment.trade.runtime.bo.PayTradeResultBo;
import cn.daxpay.open.payment.unipay.param.trade.pay.NormalPayParam;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 银盛支付策略(ysep_pay, 开放平台)
///
/// 银盛支付(ProductEnum.YSEP_PAY)下发起支付的具体执行策略。
/// 配置组装在 [#doBeforePay] 阶段完成(委托 [YsepConfigAssembler]), 支付执行委托给 [YsepPayService]。
///
/// 主应用透传 payMethod + openId/appId, 由子应用分发到开放平台对应接口族。
@Slf4j
@Service
@RequiredArgsConstructor
public class YsepPayStrategy extends AbsNormalPayStrategy {

    private final YsepPayService ysepPayService;
    private final YsepConfigAssembler ysepConfigAssembler;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.YSEP_PAY;
    }

    /// 支付前预处理: 从支付参数读取通道路由参数, 组装通道凭证写入上下文
    /// 配置缺失在此阶段抛异常, fail-fast, 不会进入 doPay
    @Override
    public void doBeforePay(PayStrategyContext context) {
        NormalPayParam payParam = context.getPayParam();
        YsepSdkCredential credential = ysepConfigAssembler.buildConfig(
                payParam.getMchNo(), payParam.getChannelMchNo(), payParam.getCapability());
        context.setChannelConfig(credential);
        // 微信 JSAPI/小程序: 尽力解析微信应用回填 channelAppId(未命中不阻断)
        ysepConfigAssembler.resolveWxAppIfRequired(payParam);
    }

    @Override
    public PayTradeResultBo doPay(PayStrategyContext context) {
        // 直接使用 doBeforePay 预组装的通道凭证
        YsepSdkCredential credential = context.getChannelConfig(YsepSdkCredential.class);
        return ysepPayService.pay(context.getTrade(), context.getPayParam(), credential, ProductEnum.YSEP_PAY);
    }
}
