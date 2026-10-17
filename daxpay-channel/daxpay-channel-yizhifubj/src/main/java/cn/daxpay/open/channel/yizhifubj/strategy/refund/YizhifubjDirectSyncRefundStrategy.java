package cn.daxpay.open.channel.yizhifubj.strategy.refund;

import cn.daxpay.open.channel.yizhifubj.service.payment.refund.YizhifubjRefundSyncService;
import cn.daxpay.open.payment.strategy.refund.AbsSyncRefundStrategy;
import cn.daxpay.open.payment.trade.runtime.bo.RefundResultBo;
import cn.daxpay.open.payment.trade.order.entity.RefundOrder;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 一直付直连退款同步策略
///
/// 一直付无退款状态查询接口, 恒软失败(退款同步受理即终态, 回调兜底)。
@Slf4j
@Service
@RequiredArgsConstructor
public class YizhifubjDirectSyncRefundStrategy extends AbsSyncRefundStrategy {

    private final YizhifubjRefundSyncService yizhifubjRefundSyncService;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.YIZHIFU_PAY;
    }

    @Override
    public RefundResultBo doSync(RefundOrder refundOrder) {
        return yizhifubjRefundSyncService.sync();
    }
}
