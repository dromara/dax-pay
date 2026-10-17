package cn.daxpay.open.channel.jlpay.strategy.pay;

import cn.daxpay.open.channel.jlpay.client.credential.JlpaySdkCredential;
import cn.daxpay.open.channel.jlpay.service.direct.JlpayDirectConfigAssembler;
import cn.daxpay.open.channel.jlpay.service.payment.close.JlpayCloseService;
import cn.daxpay.open.payment.strategy.pay.AbsPayCloseStrategy;
import cn.daxpay.open.payment.strategy.pay.PayStrategyContext;
import cn.daxpay.open.payment.trade.order.entity.PayTrade;
import cn.daxpay.open.platform.core.enums.pay.pay.CloseTypeEnum;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 嘉联直连支付关闭策略
///
/// 嘉联仅有撤销接口(/open/trans/cancel), 关闭语义恒走撤销(见 [JlpayCloseService])。
@Slf4j
@Service
@RequiredArgsConstructor
public class JlpayDirectCloseStrategy extends AbsPayCloseStrategy {

    private final JlpayCloseService jlpayCloseService;
    private final JlpayDirectConfigAssembler jlpayDirectConfigAssembler;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.JL_PAY;
    }

    @Override
    public CloseTypeEnum doClose(PayStrategyContext context, boolean useCancel) {
        // 直接从 trade 读取路由参数, 不再需要 container 中间层
        PayTrade trade = context.getTrade();
        // 组装通道调用凭证
        JlpaySdkCredential credential = jlpayDirectConfigAssembler.buildConfig(
                trade.getMchNo(), context.getChannelMchNo(), context.getCapability());
        return jlpayCloseService.close(trade, credential, useCancel, context.getClientIp());
    }
}
