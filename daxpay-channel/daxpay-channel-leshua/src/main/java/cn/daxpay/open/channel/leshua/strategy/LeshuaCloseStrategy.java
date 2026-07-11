package cn.daxpay.open.channel.leshua.strategy;

import cn.daxpay.open.channel.leshua.client.credential.LeshuaSdkCredential;
import cn.daxpay.open.channel.leshua.service.isv.LeshuaIsvConfigAssembler;
import cn.daxpay.open.channel.leshua.service.payment.LeshuaCloseService;
import cn.daxpay.open.payment.core.strategy.pay.AbsPayCloseStrategy;
import cn.daxpay.open.payment.core.strategy.pay.PayStrategyContext;
import cn.daxpay.open.payment.core.trade.entity.PayTrade;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import cn.daxpay.open.platform.core.enums.pay.pay.CloseTypeEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 乐刷服务商支付关闭策略
///
/// 组装通道凭证(委托 [LeshuaIsvConfigAssembler]), 关闭执行委托给 [LeshuaCloseService]。
@Slf4j
@Service
@RequiredArgsConstructor
public class LeshuaCloseStrategy extends AbsPayCloseStrategy {

    private final LeshuaCloseService leshuaCloseService;
    private final LeshuaIsvConfigAssembler leshuaIsvConfigAssembler;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.LESHUA_PAY;
    }

    @Override
    public CloseTypeEnum doClose(PayStrategyContext context, boolean useCancel) {
        PayTrade trade = context.getTrade();
        // 直接从 trade 读取路由参数, 不再需要 container 中间层
        LeshuaSdkCredential credential = leshuaIsvConfigAssembler.buildConfig(
                trade.getMchNo(), trade.getChannelMchNo(), trade.getCapability());
        return leshuaCloseService.close(trade, credential, useCancel, trade.getClientIp());
    }
}
