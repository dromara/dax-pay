package org.dromara.daxpay.payment.pay.bo.reconcile;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/// # 对账文件解析记录
///
@Data
@Accessors(chain = true)
public class ReconcileResolveResultBo {

    /// 通道交易明细
    private List<ChannelReconcileTradeBo> channelTrades;

    /// 原始对账文件URL
    private String originalFileUrl;

}
