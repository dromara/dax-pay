package cn.daxpay.open.channel.shouqianba.strategy.pay;

import cn.daxpay.open.channel.shouqianba.service.payment.close.ShouqianbaCloseService;
import cn.daxpay.open.payment.strategy.pay.AbsPayCloseStrategy;
import cn.daxpay.open.payment.strategy.pay.PayStrategyContext;
import cn.daxpay.open.payment.trade.order.entity.PayTrade;
import cn.daxpay.open.platform.core.enums.pay.pay.CloseTypeEnum;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 收钱吧直连支付关闭策略
///
/// 收钱吧 upay v2 无主动关单接口, 关单为本地语义(见 [ShouqianbaCloseService])。
@Slf4j
@Service
@RequiredArgsConstructor
public class ShouqianbaDirectCloseStrategy extends AbsPayCloseStrategy {

    private final ShouqianbaCloseService shouqianbaCloseService;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.SHOUQIANBA;
    }

    @Override
    public CloseTypeEnum doClose(PayStrategyContext context, boolean useCancel) {
        // 直接从 trade 读取路由参数, 不再需要 container 中间层
        PayTrade trade = context.getTrade();
        return shouqianbaCloseService.close(trade, useCancel);
    }
}
