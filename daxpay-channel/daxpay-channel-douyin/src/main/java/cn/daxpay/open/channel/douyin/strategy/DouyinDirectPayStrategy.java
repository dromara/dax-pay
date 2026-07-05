package cn.daxpay.open.channel.douyin.strategy;

import cn.daxpay.open.channel.douyin.client.credential.DouyinSdkCredential;
import cn.daxpay.open.channel.douyin.service.direct.DouyinDirectConfigAssembler;
import cn.daxpay.open.channel.douyin.service.payment.pay.DouyinPayService;
import cn.daxpay.open.payment.core.strategy.pay.PayStrategyContext;
import cn.daxpay.open.payment.core.trade.bo.PayTradeResultBo;
import cn.daxpay.open.payment.core.strategy.pay.AbsNormalPayStrategy;
import cn.daxpay.open.payment.unipay.param.trade.pay.NormalPayParam;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 抖音支付直连支付策略
///
/// 抖音直连模式(ProductEnum.DOUYIN_PAY)下发起支付的具体执行策略。
/// 配置组装在 [doBeforePay] 阶段完成(委托 [DouyinDirectConfigAssembler]), 支付执行委托给 [DouyinPayService]。
@Slf4j
@Service
@RequiredArgsConstructor
public class DouyinDirectPayStrategy extends AbsNormalPayStrategy {

    private final DouyinPayService douyinPayService;
    private final DouyinDirectConfigAssembler douyinDirectConfigAssembler;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.DOUYIN_PAY;
    }

    /// 支付前预处理: 从容器读取通道路由参数, 组装直连通道凭证写入上下文
    @Override
    public void doBeforePay(PayStrategyContext context) {
        NormalPayParam payParam = context.getPayParam();
        DouyinSdkCredential credential = douyinDirectConfigAssembler.buildConfig(
                payParam.getMchNo(), payParam.getChannelMchNo(), payParam.getCapability());
        context.setChannelConfig(credential);
    }

    @Override
    public PayTradeResultBo doPay(PayStrategyContext context) {
        // 直接使用 doBeforePay 预组装的通道凭证
        DouyinSdkCredential credential = context.getChannelConfig(DouyinSdkCredential.class);
        return douyinPayService.pay(context.getTrade(), context.getPayParam(), credential);
    }
}
