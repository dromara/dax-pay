package cn.daxpay.open.channel.longpay.strategy.sync;

import cn.daxpay.open.channel.longpay.service.payment.sync.LongpaySyncService;
import cn.daxpay.open.payment.strategy.pay.PayStrategyContext;
import cn.daxpay.open.payment.strategy.sync.AbsSyncPayOrderStrategy;
import cn.daxpay.open.payment.trade.runtime.bo.PaySyncResultBo;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 建行龙支付直连支付同步策略
///
/// 订单查询交易码待建行正式文档确认(见 [LongpaySyncService]), 恒软失败, 支付终态以异步通知为准。
@Slf4j
@Service
@RequiredArgsConstructor
public class LongpayDirectSyncStrategy extends AbsSyncPayOrderStrategy {

    private final LongpaySyncService longpaySyncService;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.LONG_PAY;
    }

    @Override
    public PaySyncResultBo doSync(PayStrategyContext context) {
        return longpaySyncService.sync();
    }
}
