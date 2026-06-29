package cn.daxpay.open.channel.alipay.strategy.direct;

import cn.daxpay.open.channel.alipay.service.direct.AlipayDirectConfigAssembler;
import cn.daxpay.open.channel.alipay.service.pay.AlipayPayService;
import cn.daxpay.open.payment.common.context.PayContext;
import cn.daxpay.open.payment.pay.bo.PayTradeResultBo;
import cn.daxpay.open.payment.strategy.pay.AbsPayStrategy;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 支付宝直连支付策略
///
/// 支付宝直连模式(ProductEnum.ALIPAY)下发起支付的具体执行策略。
/// 负责直连配置组装(委托 [AlipayDirectConfigAssembler]), 支付执行委托给 [AlipayPayService]。
///
@Slf4j
@Service
@RequiredArgsConstructor
public class AlipayDirectPayStrategy extends AbsPayStrategy {

    private final AlipayPayService alipayPayService;
    private final AlipayDirectConfigAssembler alipayDirectConfigAssembler;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.ALIPAY;
    }

    @Override
    public PayTradeResultBo doPay(PayContext context) {
        return alipayPayService.pay(context.getTrade(), context.getPayParam(),
                alipayDirectConfigAssembler.buildConfig(
                        context.getTrade().getMchNo(),
                        context.getPayParam().getChannelMchNo(),
                        context.getPayParam().getCapability()));
    }
}
