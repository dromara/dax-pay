package cn.daxpay.open.channel.cpcn.service.payment.refund;

import cn.daxpay.open.payment.trade.runtime.bo.RefundResultBo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 中金退款同步业务服务
///
/// 中金线无退款状态查询接口(对齐参考实现能力: 5021 受理后仅经 NoticeURL 异步通知结果,
/// 5016 查询仅面向支付交易), 本服务不做通道调用, 恒返回软失败让退款单保持处理中, 终态以退款回调为准。
@Slf4j
@Service
public class CpcnRefundSyncService {

    /// 同步退款状态(通道不支持查询, 软失败)
    public RefundResultBo sync() {
        return new RefundResultBo()
                .setSyncSuccess(false)
                .setSyncErrorMsg("中金通道不支持退款状态查询, 等待退款异步通知确认");
    }
}
