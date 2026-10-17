package cn.daxpay.open.channel.youzan.strategy.pay;

import cn.daxpay.open.channel.youzan.service.payment.close.YouzanCloseService;
import cn.daxpay.open.payment.strategy.pay.AbsPayCloseStrategy;
import cn.daxpay.open.payment.strategy.pay.PayStrategyContext;
import cn.daxpay.open.payment.trade.order.entity.PayTrade;
import cn.daxpay.open.platform.core.enums.pay.pay.CloseTypeEnum;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 有赞直连支付关闭策略
///
/// 有赞统收单线无主动关单接口, 关单为本地语义(见 [YouzanCloseService])。
@Slf4j
@Service
@RequiredArgsConstructor
public class YouzanDirectCloseStrategy extends AbsPayCloseStrategy {

    private final YouzanCloseService youzanCloseService;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.YOUZAN_PAY;
    }

    @Override
    public CloseTypeEnum doClose(PayStrategyContext context, boolean useCancel) {
        // 直接从 trade 读取路由参数, 不再需要 container 中间层
        PayTrade trade = context.getTrade();
        return youzanCloseService.close(trade, useCancel);
    }
}
