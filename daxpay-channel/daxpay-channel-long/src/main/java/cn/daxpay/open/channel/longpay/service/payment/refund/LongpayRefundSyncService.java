package cn.daxpay.open.channel.longpay.service.payment.refund;

import cn.daxpay.open.payment.trade.runtime.bo.RefundResultBo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 建行龙支付退款同步业务服务
///
/// 建行龙支付聚合线无退款接口(退款经商户服务平台或外联平台, 见 [LongpayRefundService]),
/// 本服务不做通道调用, 恒返回软失败。
@Slf4j
@Service
public class LongpayRefundSyncService {

    /// 同步退款状态(通道未对接退款, 软失败)
    public RefundResultBo sync() {
        return new RefundResultBo()
                .setSyncSuccess(false)
                .setSyncErrorMsg("建行龙支付退款暂未对接, 无退款状态可查询");
    }
}
