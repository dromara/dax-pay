package cn.daxpay.open.channel.epaylinks.strategy.pay;

import cn.daxpay.open.channel.epaylinks.service.payment.close.EpaylinksCloseService;
import cn.daxpay.open.payment.strategy.pay.AbsPayCloseStrategy;
import cn.daxpay.open.payment.strategy.pay.PayStrategyContext;
import cn.daxpay.open.payment.trade.order.entity.PayTrade;
import cn.daxpay.open.platform.core.enums.pay.pay.CloseTypeEnum;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 易票联直连支付关闭策略
///
/// 易票联 EFPS 支付线无主动关单接口, 关单为本地语义(见 [EpaylinksCloseService])。
@Slf4j
@Service
@RequiredArgsConstructor
public class EpaylinksDirectCloseStrategy extends AbsPayCloseStrategy {

    private final EpaylinksCloseService epaylinksCloseService;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.EPAY_LINKS;
    }

    @Override
    public CloseTypeEnum doClose(PayStrategyContext context, boolean useCancel) {
        // 直接从 trade 读取路由参数, 不再需要 container 中间层
        PayTrade trade = context.getTrade();
        return epaylinksCloseService.close(trade, useCancel);
    }
}
