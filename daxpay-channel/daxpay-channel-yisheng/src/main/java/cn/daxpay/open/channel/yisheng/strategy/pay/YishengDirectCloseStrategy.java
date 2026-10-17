package cn.daxpay.open.channel.yisheng.strategy.pay;

import cn.daxpay.open.channel.yisheng.service.payment.close.YishengCloseService;
import cn.daxpay.open.payment.strategy.pay.AbsPayCloseStrategy;
import cn.daxpay.open.payment.strategy.pay.PayStrategyContext;
import cn.daxpay.open.payment.trade.order.entity.PayTrade;
import cn.daxpay.open.platform.core.enums.pay.pay.CloseTypeEnum;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 易生直连支付关闭策略
///
/// 易生无主动关单接口, 关单为本地语义(见 [YishengCloseService])。
@Slf4j
@Service
@RequiredArgsConstructor
public class YishengDirectCloseStrategy extends AbsPayCloseStrategy {

    private final YishengCloseService yishengCloseService;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.YISHENG_PAY;
    }

    @Override
    public CloseTypeEnum doClose(PayStrategyContext context, boolean useCancel) {
        // 直接从 trade 读取路由参数, 不再需要 container 中间层
        PayTrade trade = context.getTrade();
        return yishengCloseService.close(trade, useCancel);
    }
}
