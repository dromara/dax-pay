package cn.daxpay.open.channel.baofu.strategy.refund;

import cn.daxpay.open.channel.baofu.service.payment.refund.BaofuRefundSyncService;
import cn.daxpay.open.payment.strategy.refund.AbsSyncRefundStrategy;
import cn.daxpay.open.payment.trade.runtime.bo.RefundResultBo;
import cn.daxpay.open.payment.trade.order.entity.RefundOrder;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 宝付直连退款同步策略
///
/// 宝付退款为同步终态无退款查询接口, 恒软失败(仅超时未收到受理结果的边缘场景会触发, 待人工对账)。
@Slf4j
@Service
@RequiredArgsConstructor
public class BaofuDirectSyncRefundStrategy extends AbsSyncRefundStrategy {

    private final BaofuRefundSyncService baofuRefundSyncService;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.BAOFU_PAY;
    }

    @Override
    public RefundResultBo doSync(RefundOrder refundOrder) {
        return baofuRefundSyncService.sync();
    }
}
