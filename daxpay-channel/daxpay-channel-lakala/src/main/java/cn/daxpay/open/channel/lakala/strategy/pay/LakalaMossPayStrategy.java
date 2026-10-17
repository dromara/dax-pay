package cn.daxpay.open.channel.lakala.strategy.pay;

import cn.daxpay.open.channel.lakala.client.credential.MossSdkCredential;
import cn.daxpay.open.channel.lakala.service.isv.LakalaMossConfigAssembler;
import cn.daxpay.open.channel.lakala.service.moss.LakalaMossPayService;
import cn.daxpay.open.payment.strategy.pay.AbsNormalPayStrategy;
import cn.daxpay.open.payment.strategy.pay.PayStrategyContext;
import cn.daxpay.open.payment.trade.runtime.bo.PayTradeResultBo;
import cn.daxpay.open.payment.unipay.param.trade.pay.NormalPayParam;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 拉卡拉 MOSS 支付策略
///
/// 拉卡拉MOSS(ProductEnum.LAKALA_MOSS)下发起支付的具体执行策略。
/// 配置组装在 [#doBeforePay] 阶段完成(委托 [LakalaMossConfigAssembler]), 支付执行委托给 [LakalaMossPayService]。
@Slf4j
@Service
@RequiredArgsConstructor
public class LakalaMossPayStrategy extends AbsNormalPayStrategy {

    private final LakalaMossPayService lakalaMossPayService;
    private final LakalaMossConfigAssembler lakalaMossConfigAssembler;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.LAKALA_MOSS;
    }

    /// 支付前预处理: 从容器读取通道路由参数, 组装通道凭证写入上下文
    /// 配置缺失在此阶段抛异常, fail-fast, 不会进入 doPay
    @Override
    public void doBeforePay(PayStrategyContext context) {
        NormalPayParam payParam = context.getPayParam();
        MossSdkCredential credential = lakalaMossConfigAssembler.buildConfig(
                payParam.getMchNo(), payParam.getChannelMchNo(), payParam.getCapability());
        context.setChannelConfig(credential);
        // 微信 JSAPI 解析微信应用(sub_appid)回填 channelAppId, 供子应用上送与订单落库; 未命中不阻断
        lakalaMossConfigAssembler.resolveWxAppIfRequired(payParam);
    }

    @Override
    public PayTradeResultBo doPay(PayStrategyContext context) {
        // 直接使用 doBeforePay 预组装的通道凭证
        MossSdkCredential credential = context.getChannelConfig(MossSdkCredential.class);
        return lakalaMossPayService.pay(context.getTrade(), context.getPayParam(), credential);
    }
}
