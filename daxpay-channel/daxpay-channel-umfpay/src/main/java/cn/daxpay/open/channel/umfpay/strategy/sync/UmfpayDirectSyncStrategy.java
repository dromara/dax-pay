package cn.daxpay.open.channel.umfpay.strategy.sync;

import cn.daxpay.open.channel.umfpay.service.payment.sync.UmfpaySyncService;
import cn.daxpay.open.payment.strategy.pay.PayStrategyContext;
import cn.daxpay.open.payment.strategy.sync.AbsSyncPayOrderStrategy;
import cn.daxpay.open.payment.trade.runtime.bo.PaySyncResultBo;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 联动优势直连支付同步策略
///
/// 联动优势老网关无订单查询接口, 恒软失败(订单保持处理中, 终态以异步通知为准, 见 [UmfpaySyncService])。
@Slf4j
@Service
@RequiredArgsConstructor
public class UmfpayDirectSyncStrategy extends AbsSyncPayOrderStrategy {

    private final UmfpaySyncService umfpaySyncService;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.UMF_PAY;
    }

    @Override
    public PaySyncResultBo doSync(PayStrategyContext context) {
        return umfpaySyncService.sync();
    }
}
