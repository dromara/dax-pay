package cn.daxpay.open.channel.baofu.strategy.pay;

import cn.daxpay.open.channel.baofu.service.payment.close.BaofuCloseService;
import cn.daxpay.open.payment.strategy.pay.AbsPayCloseStrategy;
import cn.daxpay.open.payment.strategy.pay.PayStrategyContext;
import cn.daxpay.open.payment.trade.order.entity.PayTrade;
import cn.daxpay.open.platform.core.enums.pay.pay.CloseTypeEnum;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 宝付直连支付关闭策略
///
/// 宝付聚合线无主动关单接口, 关单为本地语义(见 [BaofuCloseService])。
@Slf4j
@Service
@RequiredArgsConstructor
public class BaofuDirectCloseStrategy extends AbsPayCloseStrategy {

    private final BaofuCloseService baofuCloseService;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.BAOFU_PAY;
    }

    @Override
    public CloseTypeEnum doClose(PayStrategyContext context, boolean useCancel) {
        // 直接从 trade 读取路由参数, 不再需要 container 中间层
        PayTrade trade = context.getTrade();
        return baofuCloseService.close(trade, useCancel);
    }
}
