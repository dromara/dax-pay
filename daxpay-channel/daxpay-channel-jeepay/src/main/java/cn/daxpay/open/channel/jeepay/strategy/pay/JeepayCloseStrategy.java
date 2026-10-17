package cn.daxpay.open.channel.jeepay.strategy.pay;

import cn.daxpay.open.channel.jeepay.client.credential.JeepaySdkCredential;
import cn.daxpay.open.channel.jeepay.service.config.JeepayConfigAssembler;
import cn.daxpay.open.channel.jeepay.service.payment.JeepayCloseService;
import cn.daxpay.open.payment.strategy.pay.AbsPayCloseStrategy;
import cn.daxpay.open.payment.strategy.pay.PayStrategyContext;
import cn.daxpay.open.payment.trade.order.entity.PayTrade;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import cn.daxpay.open.platform.core.enums.pay.pay.CloseTypeEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # Jeepay支付关闭策略
///
/// 从 trade 读取通道路由参数(channelMchNo / capability),
/// 组装通道凭证(委托 [JeepayConfigAssembler]), 关闭执行委托给 [JeepayCloseService]。
///
/// Jeepay 关单为标准官方接口(`/api/pay/closeOrder`), 失败正常抛错由框架处理,
/// 终态由后续查单确认(区别于易支付「异常亦视为已关闭」的历史容错约定)。
@Slf4j
@Service
@RequiredArgsConstructor
public class JeepayCloseStrategy extends AbsPayCloseStrategy {

    private final JeepayCloseService jeepayCloseService;

    private final JeepayConfigAssembler jeepayConfigAssembler;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.JEE_PAY;
    }

    @Override
    public CloseTypeEnum doClose(PayStrategyContext context, boolean useCancel) {
        // 直接从 trade 读取路由参数
        PayTrade trade = context.getTrade();

        // 组装通道调用凭证
        JeepaySdkCredential credential = jeepayConfigAssembler.buildConfig(
                trade.getMchNo(), context.getChannelMchNo(), context.getCapability());

        return jeepayCloseService.close(trade, credential, useCancel);
    }
}
