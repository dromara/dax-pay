package cn.daxpay.open.channel.xingyi.strategy.pay;

import cn.daxpay.open.channel.xingyi.service.payment.close.XingyiCloseService;
import cn.daxpay.open.payment.strategy.pay.AbsPayCloseStrategy;
import cn.daxpay.open.payment.strategy.pay.PayStrategyContext;
import cn.daxpay.open.payment.trade.order.entity.PayTrade;
import cn.daxpay.open.platform.core.enums.pay.pay.CloseTypeEnum;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 星驿付直连支付关闭策略
///
/// 星驿付驿云付线无主动关单接口, 关单为本地语义(见 [XingyiCloseService])。
@Slf4j
@Service
@RequiredArgsConstructor
public class XingyiDirectCloseStrategy extends AbsPayCloseStrategy {

    private final XingyiCloseService xingyiCloseService;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.XINGYI_PAY;
    }

    @Override
    public CloseTypeEnum doClose(PayStrategyContext context, boolean useCancel) {
        // 直接从 trade 读取路由参数, 不再需要 container 中间层
        PayTrade trade = context.getTrade();
        return xingyiCloseService.close(trade, useCancel);
    }
}
