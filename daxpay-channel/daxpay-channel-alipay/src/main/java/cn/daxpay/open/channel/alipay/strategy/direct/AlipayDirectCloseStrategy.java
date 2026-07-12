package cn.daxpay.open.channel.alipay.strategy.direct;

import cn.daxpay.open.channel.alipay.client.credential.AlipaySdkCredential;
import cn.daxpay.open.channel.alipay.service.direct.AlipayDirectConfigAssembler;
import cn.daxpay.open.channel.alipay.service.payment.close.AlipayCloseService;
import cn.daxpay.open.payment.core.strategy.pay.PayStrategyContext;
import cn.daxpay.open.payment.core.trade.order.entity.PayTrade;
import cn.daxpay.open.payment.core.strategy.pay.AbsPayCloseStrategy;
import cn.daxpay.open.platform.core.enums.pay.pay.CloseTypeEnum;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 支付宝直连支付关闭策略
///
/// 支付宝直连模式(ProductEnum.ALIPAY)下的支付关闭策略。
/// 从 trade 读取通道路由参数(channelMchNo / capability),
/// 组装通道凭证(委托 [AlipayDirectConfigAssembler]), 关闭执行委托给 [AlipayCloseService]。
@Slf4j
@Service
@RequiredArgsConstructor
public class AlipayDirectCloseStrategy extends AbsPayCloseStrategy {

    private final AlipayCloseService alipayCloseService;
    private final AlipayDirectConfigAssembler alipayDirectConfigAssembler;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.ALIPAY;
    }

    @Override
    public CloseTypeEnum doClose(PayStrategyContext context, boolean useCancel) {
        // 直接从 trade 读取路由参数, 不再需要 container 中间层
        PayTrade trade = context.getTrade();

        // 组装通道调用凭证
        AlipaySdkCredential credential = alipayDirectConfigAssembler.buildConfig(
                trade.getMchNo(), context.getChannelMchNo(), context.getCapability());

        return alipayCloseService.close(trade, credential, useCancel);
    }
}
