package cn.daxpay.open.channel.fuyou.strategy.pay;

import cn.daxpay.open.channel.fuyou.client.credential.FuyouRsaSdkCredential;
import cn.daxpay.open.channel.fuyou.service.payment.FuyouH5PayService;
import cn.daxpay.open.channel.fuyou.service.rsa.FuyouRsaConfigAssembler;
import cn.daxpay.open.payment.strategy.pay.AbsNormalPayStrategy;
import cn.daxpay.open.payment.strategy.pay.PayStrategyContext;
import cn.daxpay.open.payment.trade.runtime.bo.PayTradeResultBo;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 富友 H5 聚合收银台支付策略
///
/// 富友 H5 聚合收银台(ProductEnum.FUYOU_H5)下发起支付的具体执行策略。
/// 配置组装在 [#doBeforePay] 阶段完成(委托 [FuyouRsaConfigAssembler]),
/// 支付执行委托给 [FuyouH5PayService](aggpcpay token, JSON + RSA 加密信封, 返回收银台链接 token_url)。
///
/// 收银台 WeAppletPay 由富友侧小程序承接, 无需解析微信应用。
@Slf4j
@Service
@RequiredArgsConstructor
public class FuyouH5PayStrategy extends AbsNormalPayStrategy {

    private final FuyouH5PayService fuyouH5PayService;
    private final FuyouRsaConfigAssembler fuyouRsaConfigAssembler;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.FUYOU_H5;
    }

    /// 支付前预处理: 从容器读取通道路由参数, 组装通道凭证写入上下文
    @Override
    public void doBeforePay(PayStrategyContext context) {
        FuyouRsaSdkCredential credential = fuyouRsaConfigAssembler.buildConfig(
                context.getPayParam().getChannelMchNo());
        context.setChannelConfig(credential);
    }

    @Override
    public PayTradeResultBo doPay(PayStrategyContext context) {
        FuyouRsaSdkCredential credential = context.getChannelConfig(FuyouRsaSdkCredential.class);
        return fuyouH5PayService.pay(context.getTrade(), context.getPayParam(), credential);
    }
}
