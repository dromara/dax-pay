package cn.daxpay.open.channel.helipay.strategy.pay;

import cn.daxpay.open.channel.helipay.service.payment.HelipayCloseService;
import cn.daxpay.open.payment.strategy.pay.AbsPayCloseStrategy;
import cn.daxpay.open.payment.strategy.pay.PayStrategyContext;
import cn.daxpay.open.payment.trade.order.entity.PayTrade;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import cn.daxpay.open.platform.core.enums.pay.pay.CloseTypeEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 合利宝直连支付关闭策略
///
/// 合利宝扫码产品线无主动关单接口, 关单为本地语义(见 [HelipayCloseService])。
@Slf4j
@Service
@RequiredArgsConstructor
public class HelipayDirectCloseStrategy extends AbsPayCloseStrategy {

    private final HelipayCloseService helipayCloseService;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.HELI_PAY;
    }

    @Override
    public CloseTypeEnum doClose(PayStrategyContext context, boolean useCancel) {
        // 直接从 trade 读取路由参数
        PayTrade trade = context.getTrade();
        return helipayCloseService.close(trade, useCancel);
    }
}
