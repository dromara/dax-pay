package cn.daxpay.open.channel.fuyou.strategy.pay;

import cn.daxpay.open.channel.fuyou.client.credential.FuyouDirectSdkCredential;
import cn.daxpay.open.channel.fuyou.service.direct.FuyouDirectConfigAssembler;
import cn.daxpay.open.channel.fuyou.service.payment.FuyouDirectCloseService;
import cn.daxpay.open.payment.strategy.pay.AbsPayCloseStrategy;
import cn.daxpay.open.payment.strategy.pay.PayStrategyContext;
import cn.daxpay.open.payment.trade.order.entity.PayTrade;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import cn.daxpay.open.platform.core.enums.pay.pay.CloseTypeEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 富友直连支付关闭策略
///
/// 富友直连仅提供关单接口(`/aggregatePay/closeOrder`), 无撤销接口, useCancel 参数在 service 层被忽略。
@Slf4j
@Service
@RequiredArgsConstructor
public class FuyouDirectCloseStrategy extends AbsPayCloseStrategy {

    private final FuyouDirectCloseService fuyouDirectCloseService;
    private final FuyouDirectConfigAssembler fuyouDirectConfigAssembler;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.FUYOU_DIRECT;
    }

    @Override
    public CloseTypeEnum doClose(PayStrategyContext context, boolean useCancel) {
        // 直接从 trade 读取路由参数, 不再需要 container 中间层
        PayTrade trade = context.getTrade();

        FuyouDirectSdkCredential credential = fuyouDirectConfigAssembler.buildConfig(
                trade.getMchNo(), context.getChannelMchNo(), context.getCapability());
        return fuyouDirectCloseService.close(trade, credential, useCancel);
    }
}
