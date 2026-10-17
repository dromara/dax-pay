package cn.daxpay.open.channel.xingyi.strategy.refund;

import cn.daxpay.open.channel.xingyi.service.payment.refund.XingyiRefundSyncService;
import cn.daxpay.open.payment.strategy.refund.AbsSyncRefundStrategy;
import cn.daxpay.open.payment.trade.runtime.bo.RefundResultBo;
import cn.daxpay.open.payment.trade.order.entity.RefundOrder;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 星驿付直连退款同步策略
///
/// 星驿付驿云付线无退款查询接口(退款为同步受理即成功), 恒软失败。
@Slf4j
@Service
@RequiredArgsConstructor
public class XingyiDirectSyncRefundStrategy extends AbsSyncRefundStrategy {

    private final XingyiRefundSyncService xingyiRefundSyncService;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.XINGYI_PAY;
    }

    @Override
    public RefundResultBo doSync(RefundOrder refundOrder) {
        return xingyiRefundSyncService.sync();
    }
}
