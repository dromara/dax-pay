package cn.daxpay.open.channel.sheng.strategy.pay;

import cn.daxpay.open.channel.sheng.client.credential.ShengSdkCredential;
import cn.daxpay.open.channel.sheng.service.config.ShengConfigAssembler;
import cn.daxpay.open.channel.sheng.service.payment.ShengCloseService;
import cn.daxpay.open.payment.strategy.pay.AbsPayCloseStrategy;
import cn.daxpay.open.payment.strategy.pay.PayStrategyContext;
import cn.daxpay.open.payment.trade.order.entity.PayTrade;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import cn.daxpay.open.platform.core.enums.pay.pay.CloseTypeEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 盛付通支付关闭策略
///
/// 从 trade 读取通道路由参数(channelMchNo / capability),
/// 组装通道凭证(委托 [ShengConfigAssembler]), 关闭执行委托给 [ShengCloseService]。
///
/// useCancel 语义透传给子应用: true=当日撤单(reverseOrder), false=关单(closeOrder)。
@Slf4j
@Service
@RequiredArgsConstructor
public class ShengPayCloseStrategy extends AbsPayCloseStrategy {

    private final ShengCloseService shengCloseService;
    private final ShengConfigAssembler shengConfigAssembler;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.SHENG_PAY;
    }

    @Override
    public CloseTypeEnum doClose(PayStrategyContext context, boolean useCancel) {
        // 直接从 trade 读取路由参数, 不再需要 container 中间层
        PayTrade trade = context.getTrade();

        // 组装通道调用凭证
        ShengSdkCredential credential = shengConfigAssembler.buildConfig(
                trade.getMchNo(), context.getChannelMchNo(), context.getCapability());

        return shengCloseService.close(trade, credential, useCancel);
    }
}
