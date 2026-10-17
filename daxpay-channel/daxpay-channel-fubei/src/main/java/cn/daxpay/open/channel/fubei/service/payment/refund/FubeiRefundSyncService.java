package cn.daxpay.open.channel.fubei.service.payment.refund;

import cn.daxpay.open.payment.trade.runtime.bo.RefundResultBo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 付呗退款同步业务服务
///
/// 付呗无退款状态查询接口(对齐参考实现能力: fbpay.order.refund 同步受理即终态),
/// 本服务不做通道调用, 恒返回软失败; 正常退款单在受理时已落终态, 不会进入同步查询链路。
@Slf4j
@Service
public class FubeiRefundSyncService {

    /// 同步退款状态(通道不支持查询, 软失败)
    public RefundResultBo sync() {
        return new RefundResultBo()
                .setSyncSuccess(false)
                .setSyncErrorMsg("付呗通道不支持退款状态查询, 退款以同步受理结果为准");
    }
}
