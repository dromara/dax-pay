package cn.daxpay.open.channel.alipay.strategy.isv.pay;

import cn.daxpay.open.channel.alipay.client.credential.AlipaySdkCredential;
import cn.daxpay.open.channel.alipay.service.isv.AlipayIsvConfigAssembler;
import cn.daxpay.open.channel.alipay.service.payment.close.AlipayCloseService;
import cn.daxpay.open.payment.strategy.pay.PayStrategyContext;
import cn.daxpay.open.payment.trade.order.entity.PayTrade;
import cn.daxpay.open.payment.strategy.pay.AbsPayCloseStrategy;
import cn.daxpay.open.platform.core.enums.pay.pay.CloseTypeEnum;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 支付宝服务商支付关闭策略
///
/// 支付宝服务商模式(ProductEnum.ALIPAY_ISV)下的支付关闭策略。
/// 组装通道凭证(委托 [AlipayIsvConfigAssembler]), 关闭执行委托给 [AlipayCloseService]。
@Slf4j
@Service
@RequiredArgsConstructor
public class AlipayIsvCloseStrategy extends AbsPayCloseStrategy {

    private final AlipayCloseService alipayCloseService;
    private final AlipayIsvConfigAssembler alipayIsvConfigAssembler;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.ALIPAY_ISV;
    }

    @Override
    public CloseTypeEnum doClose(PayStrategyContext context, boolean useCancel) {
        PayTrade trade = context.getTrade();
        // 组装服务商模式通道调用凭证(含应用授权令牌)
        AlipaySdkCredential credential = alipayIsvConfigAssembler.buildConfig(trade.getMchNo());
        return alipayCloseService.close(trade, credential, useCancel);
    }
}
