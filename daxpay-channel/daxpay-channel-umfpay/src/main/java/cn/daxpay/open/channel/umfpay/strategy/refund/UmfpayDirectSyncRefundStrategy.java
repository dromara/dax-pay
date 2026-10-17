package cn.daxpay.open.channel.umfpay.strategy.refund;

import cn.daxpay.open.channel.umfpay.service.payment.refund.UmfpayRefundSyncService;
import cn.daxpay.open.payment.strategy.refund.AbsSyncRefundStrategy;
import cn.daxpay.open.payment.trade.runtime.bo.RefundResultBo;
import cn.daxpay.open.payment.trade.order.entity.RefundOrder;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 联动优势直连退款同步策略
///
/// 联动优势无退款查询接口, 恒软失败(退款为同步终态, 见 [UmfpayRefundSyncService])。
@Slf4j
@Service
@RequiredArgsConstructor
public class UmfpayDirectSyncRefundStrategy extends AbsSyncRefundStrategy {

    private final UmfpayRefundSyncService umfpayRefundSyncService;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.UMF_PAY;
    }

    @Override
    public RefundResultBo doSync(RefundOrder refundOrder) {
        return umfpayRefundSyncService.sync();
    }
}
