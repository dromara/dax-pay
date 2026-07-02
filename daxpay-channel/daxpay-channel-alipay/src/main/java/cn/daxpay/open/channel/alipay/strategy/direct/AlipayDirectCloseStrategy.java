package cn.daxpay.open.channel.alipay.strategy.direct;

import cn.daxpay.open.channel.alipay.client.credential.AlipaySdkCredential;
import cn.daxpay.open.channel.alipay.service.direct.AlipayDirectConfigAssembler;
import cn.daxpay.open.channel.alipay.service.payment.close.AlipayCloseService;
import cn.daxpay.open.payment.common.context.NormalPayContext;
import cn.daxpay.open.payment.pay.order.entity.NormalPayOrder;
import cn.daxpay.open.payment.pay.order.entity.PayTrade;
import cn.daxpay.open.payment.strategy.pay.AbsPayCloseStrategy;
import cn.daxpay.open.platform.core.enums.pay.pay.CloseTypeEnum;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 支付宝直连支付关闭策略
///
/// 支付宝直连模式(ProductEnum.ALIPAY)下的支付关闭策略。
/// 从上下文容器读取通道路由参数(channelMchNo / capability),
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
    public CloseTypeEnum doClose(NormalPayContext context, boolean useCancel) {
        // 从上下文容器读取通道路由参数, 用于凭证解析
        NormalPayOrder normalOrder = context.getContainer();
        String channelMchNo = normalOrder != null ? normalOrder.getChannelMchNo() : null;
        String capability = normalOrder != null ? normalOrder.getCapability() : null;
        PayTrade trade = context.getTrade();

        // 组装通道调用凭证
        AlipaySdkCredential credential = alipayDirectConfigAssembler.buildConfig(
                trade.getMchNo(), channelMchNo, capability);

        return alipayCloseService.close(trade, credential, useCancel);
    }
}
