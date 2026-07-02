package cn.daxpay.open.channel.alipay.strategy.direct;

import cn.daxpay.open.channel.alipay.client.credential.AlipaySdkCredential;
import cn.daxpay.open.channel.alipay.service.direct.AlipayDirectConfigAssembler;
import cn.daxpay.open.channel.alipay.service.payment.close.AlipayCloseService;
import cn.daxpay.open.payment.pay.order.dao.NormalPayOrderManager;
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
/// 从容器层 NormalPayOrder 读取通道路由参数(channelMchNo / capability),
/// 组装通道凭证(委托 [AlipayDirectConfigAssembler]), 关闭执行委托给 [AlipayCloseService]。
@Slf4j
@Service
@RequiredArgsConstructor
public class AlipayDirectCloseStrategy extends AbsPayCloseStrategy {

    private final AlipayCloseService alipayCloseService;
    private final AlipayDirectConfigAssembler alipayDirectConfigAssembler;
    private final NormalPayOrderManager normalPayOrderManager;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.ALIPAY;
    }

    @Override
    public CloseTypeEnum doClose(PayTrade trade, boolean useCancel) {
        // 从容器层读取通道路由参数, 用于凭证解析
        NormalPayOrder normalOrder = normalPayOrderManager.findById(trade.getContainerId()).orElse(null);
        String channelMchNo = normalOrder != null ? normalOrder.getChannelMchNo() : null;
        String capability = normalOrder != null ? normalOrder.getCapability() : null;

        // 组装通道调用凭证
        AlipaySdkCredential credential = alipayDirectConfigAssembler.buildConfig(
                trade.getMchNo(), channelMchNo, capability);

        return alipayCloseService.close(trade, credential, useCancel);
    }
}
