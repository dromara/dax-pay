package cn.daxpay.open.channel.jlpay.service.payment.refund;

import cn.daxpay.open.payment.trade.runtime.bo.RefundResultBo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 嘉联退款同步业务服务
///
/// 嘉联无退款状态查询接口(对齐参考实现能力: /open/trans/refund 受理即同步终态, 无退款异步通知),
/// 本服务不做通道调用, 恒返回软失败; 退款单在受理时已达终态, 正常不会进入同步流程。
@Slf4j
@Service
public class JlpayRefundSyncService {

    /// 同步退款状态(通道不支持查询, 软失败)
    public RefundResultBo sync() {
        return new RefundResultBo()
                .setSyncSuccess(false)
                .setSyncErrorMsg("嘉联通道不支持退款状态查询(退款为同步终态)");
    }
}
