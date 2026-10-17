package cn.daxpay.open.channel.cpcn.strategy.pay;

import cn.daxpay.open.channel.cpcn.service.payment.close.CpcnCloseService;
import cn.daxpay.open.payment.strategy.pay.AbsPayCloseStrategy;
import cn.daxpay.open.payment.strategy.pay.PayStrategyContext;
import cn.daxpay.open.payment.trade.order.entity.PayTrade;
import cn.daxpay.open.platform.core.enums.pay.pay.CloseTypeEnum;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 中金直连支付关闭策略
///
/// 中金线无主动关单接口, 关单为本地语义(见 [CpcnCloseService])。
@Slf4j
@Service
@RequiredArgsConstructor
public class CpcnDirectCloseStrategy extends AbsPayCloseStrategy {

    private final CpcnCloseService cpcnCloseService;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.CPCN_PAY;
    }

    @Override
    public CloseTypeEnum doClose(PayStrategyContext context, boolean useCancel) {
        // 直接从 trade 读取路由参数, 不再需要 container 中间层
        PayTrade trade = context.getTrade();
        return cpcnCloseService.close(trade, useCancel);
    }
}
