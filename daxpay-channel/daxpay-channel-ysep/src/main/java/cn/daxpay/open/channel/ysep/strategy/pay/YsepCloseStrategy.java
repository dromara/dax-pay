package cn.daxpay.open.channel.ysep.strategy.pay;

import cn.daxpay.open.channel.ysep.client.credential.YsepSdkCredential;
import cn.daxpay.open.channel.ysep.service.config.YsepConfigAssembler;
import cn.daxpay.open.channel.ysep.service.payment.YsepCloseService;
import cn.daxpay.open.payment.strategy.pay.AbsPayCloseStrategy;
import cn.daxpay.open.payment.strategy.pay.PayStrategyContext;
import cn.daxpay.open.payment.trade.order.entity.PayTrade;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import cn.daxpay.open.platform.core.enums.pay.pay.CloseTypeEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 银盛支付关闭策略(ysep_pay)
///
/// 从 trade 读取通道路由参数, 组装通道凭证(委托 [YsepConfigAssembler]),
/// 关闭执行委托给 [YsepCloseService](走 ysepay.online.trade.close)。
@Slf4j
@Service
@RequiredArgsConstructor
public class YsepCloseStrategy extends AbsPayCloseStrategy {

    private final YsepCloseService ysepCloseService;
    private final YsepConfigAssembler ysepConfigAssembler;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.YSEP_PAY;
    }

    @Override
    public CloseTypeEnum doClose(PayStrategyContext context, boolean useCancel) {
        PayTrade trade = context.getTrade();

        // 组装通道调用凭证
        YsepSdkCredential credential = ysepConfigAssembler.buildConfig(
                trade.getMchNo(), context.getChannelMchNo(), context.getCapability());

        return ysepCloseService.close(trade, credential, ProductEnum.YSEP_PAY);
    }
}
