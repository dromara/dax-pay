package cn.daxpay.open.channel.yisheng.service.payment.refund;

import cn.daxpay.open.payment.trade.runtime.bo.RefundResultBo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 易生退款同步业务服务
///
/// 易生退款为同步终态(受理即成功), 无独立退款状态查询接口(对齐参考实现能力),
/// 本服务不做通道调用, 恒返回软失败让退款单保持处理中, 仅超时未收到受理结果的边缘场景会触发, 待人工对账。
@Slf4j
@Service
public class YishengRefundSyncService {

    /// 同步退款状态(通道不支持退款查询, 软失败)
    public RefundResultBo sync() {
        return new RefundResultBo()
                .setSyncSuccess(false)
                .setSyncErrorMsg("易生退款为同步终态无退款查询接口, 请对账确认退款结果");
    }
}
