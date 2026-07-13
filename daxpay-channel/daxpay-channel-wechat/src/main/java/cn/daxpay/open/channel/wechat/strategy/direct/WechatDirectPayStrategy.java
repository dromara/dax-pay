package cn.daxpay.open.channel.wechat.strategy.direct;

import cn.daxpay.open.channel.wechat.client.credential.WechatSdkCredential;
import cn.daxpay.open.channel.wechat.service.direct.WechatDirectConfigAssembler;
import cn.daxpay.open.channel.wechat.service.payment.pay.WechatPayService;
import cn.daxpay.open.payment.strategy.pay.AbsNormalPayStrategy;
import cn.daxpay.open.payment.strategy.pay.PayStrategyContext;
import cn.daxpay.open.payment.trade.runtime.bo.PayTradeResultBo;
import cn.daxpay.open.payment.unipay.param.trade.pay.NormalPayParam;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 微信直连支付策略
///
/// 微信直连模式(ProductEnum.WECHAT_PAY)下发起支付的具体执行策略。
/// 配置组装在 [#doBeforePay] 阶段完成(委托 [WechatDirectConfigAssembler]), 支付执行委托给 [WechatPayService]。
@Slf4j
@Service
@RequiredArgsConstructor
public class WechatDirectPayStrategy extends AbsNormalPayStrategy {

    private final WechatPayService wechatPayService;
    private final WechatDirectConfigAssembler wechatDirectConfigAssembler;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.WECHAT_PAY;
    }

    /// 支付前预处理: 从容器读取通道路由参数, 组装直连通道凭证写入上下文
    /// 配置缺失在此阶段抛异常, fail-fast, 不会进入 doPay
    @Override
    public void doBeforePay(PayStrategyContext context) {
        // 组装直连通道凭证(只依赖请求参数, 订单尚未创建)
        NormalPayParam payParam = context.getPayParam();
        WechatSdkCredential credential = wechatDirectConfigAssembler.buildConfig(
                payParam.getMchNo(), payParam.getChannelMchNo(), payParam.getCapability());
        context.setChannelConfig(credential);
    }

    @Override
    public PayTradeResultBo doPay(PayStrategyContext context) {
        // 直接使用 doBeforePay 预组装的通道凭证
        WechatSdkCredential credential = context.getChannelConfig(WechatSdkCredential.class);
        return wechatPayService.pay(context.getTrade(), context.getPayParam(), credential);
    }
}
