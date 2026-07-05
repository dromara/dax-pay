package cn.daxpay.open.channel.douyin.strategy;

import cn.daxpay.open.channel.douyin.client.credential.DouyinSdkCredential;
import cn.daxpay.open.channel.douyin.service.direct.DouyinDirectConfigAssembler;
import cn.daxpay.open.channel.douyin.service.payment.close.DouyinCloseService;
import cn.daxpay.open.payment.core.strategy.pay.PayStrategyContext;
import cn.daxpay.open.payment.core.trade.entity.NormalPayOrder;
import cn.daxpay.open.payment.core.trade.entity.PayTrade;
import cn.daxpay.open.payment.core.strategy.pay.AbsPayCloseStrategy;
import cn.daxpay.open.platform.core.enums.pay.pay.CloseTypeEnum;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 抖音直连支付关闭策略
///
/// 抖音直连模式(ProductEnum.DOUYIN_PAY)下的支付关闭策略。
/// 从上下文容器读取通道路由参数(channelMchNo / capability),
/// 组装通道凭证(委托 [DouyinDirectConfigAssembler]), 关闭执行委托给 [DouyinCloseService]。
@Slf4j
@Service
@RequiredArgsConstructor
public class DouyinDirectCloseStrategy extends AbsPayCloseStrategy {

    private final DouyinCloseService douyinCloseService;
    private final DouyinDirectConfigAssembler douyinDirectConfigAssembler;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.DOUYIN_PAY;
    }

    @Override
    public CloseTypeEnum doClose(PayStrategyContext context, boolean useCancel) {
        NormalPayOrder normalOrder = context.getContainer();
        String channelMchNo = normalOrder != null ? normalOrder.getChannelMchNo() : null;
        String capability = normalOrder != null ? normalOrder.getCapability() : null;
        PayTrade trade = context.getTrade();

        DouyinSdkCredential credential = douyinDirectConfigAssembler.buildConfig(
                trade.getMchNo(), channelMchNo, capability);
        return douyinCloseService.close(trade, credential, useCancel);
    }
}
