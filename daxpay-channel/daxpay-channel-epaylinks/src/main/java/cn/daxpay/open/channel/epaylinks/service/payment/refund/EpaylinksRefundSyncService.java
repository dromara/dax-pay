package cn.daxpay.open.channel.epaylinks.service.payment.refund;

import cn.daxpay.open.payment.trade.runtime.bo.RefundResultBo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 易票联退款同步业务服务
///
/// 易票联 Refund/V2 为同步受理即终态语义(对齐参考实现能力: 无退款状态查询接口、无退款异步通知),
/// 退款终态在发起时已确定, 本服务不做通道调用, 恒返回软失败让调用方保持既有终态。
@Slf4j
@Service
public class EpaylinksRefundSyncService {

    /// 同步退款状态(通道不支持查询, 软失败)
    public RefundResultBo sync() {
        return new RefundResultBo()
                .setSyncSuccess(false)
                .setSyncErrorMsg("易票联通道不支持退款状态查询(退款为同步受理即终态)");
    }
}
