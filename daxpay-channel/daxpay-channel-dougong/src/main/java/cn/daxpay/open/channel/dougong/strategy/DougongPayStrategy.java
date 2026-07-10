package cn.daxpay.open.channel.dougong.strategy;

import cn.daxpay.open.channel.dougong.client.credential.DougongSdkCredential;
import cn.daxpay.open.channel.dougong.service.isv.DougongIsvConfigAssembler;
import cn.daxpay.open.channel.dougong.service.payment.DougongPayService;
import cn.daxpay.open.payment.core.strategy.pay.AbsNormalPayStrategy;
import cn.daxpay.open.payment.core.strategy.pay.PayStrategyContext;
import cn.daxpay.open.payment.core.trade.bo.PayTradeResultBo;
import cn.daxpay.open.payment.unipay.param.trade.pay.NormalPayParam;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 斗拱服务商支付策略
///
/// 配置组装在 [#doBeforePay] 阶段完成(委托 [DougongIsvConfigAssembler]),
/// 支付执行委托给 [DougongPayService]。
///
/// 斗拱为聚合服务商模式, 仅有一个产品 DOUGONG_PAY(覆盖微信/支付宝/银联)。
@Slf4j
@Service
@RequiredArgsConstructor
public class DougongPayStrategy extends AbsNormalPayStrategy {

    private final DougongPayService dougongPayService;
    private final DougongIsvConfigAssembler dougongIsvConfigAssembler;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.DOUGONG_PAY;
    }

    /// 支付前预处理: 从容器读取通道路由参数, 组装通道凭证写入上下文
    @Override
    public void doBeforePay(PayStrategyContext context) {
        NormalPayParam payParam = context.getPayParam();
        DougongSdkCredential credential = dougongIsvConfigAssembler.buildConfig(
                payParam.getMchNo(), payParam.getChannelMchNo(), payParam.getCapability());
        context.setChannelConfig(credential);
    }

    @Override
    public PayTradeResultBo doPay(PayStrategyContext context) {
        DougongSdkCredential credential = context.getChannelConfig(DougongSdkCredential.class);
        return dougongPayService.pay(context.getTrade(), context.getPayParam(), credential);
    }
}
