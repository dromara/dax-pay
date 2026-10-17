package cn.daxpay.open.channel.duolabao.strategy.refund;

import cn.daxpay.open.channel.duolabao.service.payment.refund.DuolabaoRefundSyncService;
import cn.daxpay.open.payment.strategy.refund.AbsSyncRefundStrategy;
import cn.daxpay.open.payment.trade.runtime.bo.RefundResultBo;
import cn.daxpay.open.payment.trade.order.entity.RefundOrder;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 哆啦宝直连退款同步策略
///
/// 哆啦宝无退款查询接口, 恒软失败(退款为同步受理即成功, 无需查询)。
@Slf4j
@Service
@RequiredArgsConstructor
public class DuolabaoDirectSyncRefundStrategy extends AbsSyncRefundStrategy {

    private final DuolabaoRefundSyncService duolabaoRefundSyncService;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.DUOLABAO_PAY;
    }

    @Override
    public RefundResultBo doSync(RefundOrder refundOrder) {
        return duolabaoRefundSyncService.sync();
    }
}
