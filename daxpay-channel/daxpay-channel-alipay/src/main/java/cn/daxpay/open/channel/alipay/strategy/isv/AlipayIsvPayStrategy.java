package cn.daxpay.open.channel.alipay.strategy.isv;

import cn.daxpay.open.channel.alipay.service.isv.AlipayIsvConfigAssembler;
import cn.daxpay.open.channel.alipay.service.payment.pay.AlipayPayService;
import cn.daxpay.open.payment.common.context.PayStrategyContext;
import cn.daxpay.open.payment.pay.bo.PayTradeResultBo;
import cn.daxpay.open.payment.strategy.pay.AbsPayStrategy;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 支付宝服务商支付策略
///
/// 支付宝服务商模式(ProductEnum.ALIPAY_ISV)下发起支付的具体执行策略。
/// 负责服务商配置组装(含应用授权令牌, 委托 [AlipayIsvConfigAssembler]), 支付执行委托给 [AlipayPayService]。
@Slf4j
@Service
@RequiredArgsConstructor
public class AlipayIsvPayStrategy extends AbsPayStrategy {

    private final AlipayPayService alipayPayService;
    private final AlipayIsvConfigAssembler alipayIsvConfigAssembler;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.ALIPAY_ISV;
    }

    @Override
    public PayTradeResultBo doPay(PayStrategyContext context) {
        // 组装服务商模式通道调用凭证(含应用授权令牌), 支付执行委托 AlipayPayService
        return alipayPayService.pay(context.getTrade(), context.getPayParam(),
                alipayIsvConfigAssembler.buildConfig(context.getTrade().getMchNo()));
    }
}
