package cn.daxpay.open.channel.wechat.strategy.isv;

import cn.daxpay.open.channel.wechat.client.credential.WechatSdkCredential;
import cn.daxpay.open.channel.wechat.service.isv.WechatIsvConfigAssembler;
import cn.daxpay.open.channel.wechat.service.payment.isv.WechatIsvPayService;
import cn.daxpay.open.payment.core.strategy.pay.AbsNormalPayStrategy;
import cn.daxpay.open.payment.core.strategy.pay.PayStrategyContext;
import cn.daxpay.open.payment.core.trade.bo.PayTradeResultBo;
import cn.daxpay.open.payment.unipay.param.trade.pay.NormalPayParam;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 微信服务商支付策略
///
/// 微信服务商模式(ProductEnum.WECHAT_ISV)下发起支付的具体执行策略。
/// 配置组装在 [doBeforePay] 阶段完成(委托 [WechatIsvConfigAssembler]), 支付执行委托给 [WechatPayService]。
@Slf4j
@Service
@RequiredArgsConstructor
public class WechatIsvPayStrategy extends AbsNormalPayStrategy {

    private final WechatIsvPayService wechatIsvPayService;
    private final WechatIsvConfigAssembler wechatIsvConfigAssembler;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.WECHAT_ISV;
    }

    /// 支付前预处理: 从请求参数读取通道路由参数, 组装服务商通道凭证写入上下文
    /// 配置缺失在此阶段抛异常, fail-fast, 不会进入 doPay
    @Override
    public void doBeforePay(PayStrategyContext context) {
        NormalPayParam payParam = context.getPayParam();
        WechatSdkCredential credential = wechatIsvConfigAssembler.buildConfig(
                payParam.getMchNo(), payParam.getChannelMchNo(), payParam.getCapability());
        context.setChannelConfig(credential);
    }

    @Override
    public PayTradeResultBo doPay(PayStrategyContext context) {
        // 直接使用 doBeforePay 预组装的服务商通道凭证
        WechatSdkCredential credential = context.getChannelConfig(WechatSdkCredential.class);
        return wechatIsvPayService.pay(context.getTrade(), context.getPayParam(), credential);
    }
}
