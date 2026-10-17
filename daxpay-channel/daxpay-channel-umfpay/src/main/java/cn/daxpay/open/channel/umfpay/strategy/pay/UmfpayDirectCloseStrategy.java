package cn.daxpay.open.channel.umfpay.strategy.pay;

import cn.daxpay.open.channel.umfpay.service.payment.close.UmfpayCloseService;
import cn.daxpay.open.payment.strategy.pay.AbsPayCloseStrategy;
import cn.daxpay.open.payment.strategy.pay.PayStrategyContext;
import cn.daxpay.open.payment.trade.order.entity.PayTrade;
import cn.daxpay.open.platform.core.enums.pay.pay.CloseTypeEnum;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 联动优势直连支付关闭策略
///
/// 联动优势老网关无主动关单接口, 关单为本地语义(见 [UmfpayCloseService])。
@Slf4j
@Service
@RequiredArgsConstructor
public class UmfpayDirectCloseStrategy extends AbsPayCloseStrategy {

    private final UmfpayCloseService umfpayCloseService;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.UMF_PAY;
    }

    @Override
    public CloseTypeEnum doClose(PayStrategyContext context, boolean useCancel) {
        // 直接从 trade 读取路由参数, 不再需要 container 中间层
        PayTrade trade = context.getTrade();
        return umfpayCloseService.close(trade, useCancel);
    }
}
