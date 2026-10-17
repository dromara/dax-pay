package cn.daxpay.open.channel.yizhifubj.service.payment.refund;

import cn.daxpay.open.payment.trade.runtime.bo.RefundResultBo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 一直付退款同步业务服务
///
/// 一直付无独立退款状态查询接口(对齐参考实现能力: `/onlinePay/refund` 同步受理即终态),
/// 本服务不做通道调用, 恒返回软失败让退款单保持处理中, 终态以退款同步受理结果/退款回调为准。
@Slf4j
@Service
public class YizhifubjRefundSyncService {

    /// 同步退款状态(通道不支持查询, 软失败)
    public RefundResultBo sync() {
        return new RefundResultBo()
                .setSyncSuccess(false)
                .setSyncErrorMsg("一直付通道不支持退款状态查询, 以退款受理结果与退款回调为准");
    }
}
